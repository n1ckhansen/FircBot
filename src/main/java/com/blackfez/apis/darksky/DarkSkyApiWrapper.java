package com.blackfez.apis.darksky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class DarkSkyApiWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	public transient static final String DSKFILE = "dskForecasts.ser";
	private transient final String DSKEY = "37a15c6db486688f88dff78d57d2edb4";
	private transient final String DSURL = "https://api.darksky.net/forecast/";
	private Map<Location,Forecast> CACHE;
	private transient final ZipCodeApiWrapper zipper = ZipCodeApiWrapper.getInstance();
	
	private DarkSkyApiWrapper() {
		if( this.CACHE == null )
			this.CACHE = new HashMap<Location,Forecast>();
	}
	
	public Forecast getForcastForZip( String zip ) {
		Location loc = zipper.getLocation(zip);
		Forecast forecast = this.getForcastForCoords( loc );
		return forecast;
	}
	
	public Map<Location,Forecast> getCache() {
		return this.CACHE;
	}
	
	public void setCache( Map<Location,Forecast> cache ) {
		this.CACHE = cache;
	}
	
	public Forecast getForcastForCoords( Location loc ) {
		if( this.getCache().containsKey( loc ) && CACHE.get( loc ).isCurrent() ) 
			return CACHE.get( loc );
		Forecast forecast = consultDarkSky( loc );
		
		return forecast;
	}
	
	private Forecast consultDarkSky( Location loc ) {
		Forecast f = new Forecast();
		try {
			URL url = new URL( String.format("%s%s/%s,%s", DSURL,DSKEY,loc.getLatitude(), loc.getLongitude() ) );
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader( is );
			BufferedReader br = new BufferedReader( isr );
			StringBuilder sb = new StringBuilder();
			String line;
			while( ( line = br.readLine() ) != null ) {
				sb.append( line );
			}
			f.setJsonForecast(  sb.toString() );
			this.CACHE.put( loc, f );
			this.serializeTheStuff();
		} 
		catch (MalformedURLException e) {
			System.out.println( "Dark Sky API URL malformed. Aborting consultation" );
			e.printStackTrace();
		} 
		catch (IOException e) {
			System.out.println( "Unable to serialize forecast cache.  Aborting serialization." );
			e.printStackTrace();
		}
		return f;
	}
	
	public String retrieveCurrentWeatherForZip( String zip ) {
		Location loc = zipper.getLocation( zip );
		Forecast f = this.getForcastForCoords( loc );
		StringBuilder blurb = new StringBuilder();
		blurb.append( String.format( "%s, %s %s: ", loc.getCity(), loc.getState(), loc.getZip() ) );
		blurb.append( String.format( "%s and %s%sF Feels like: %s%sF ", f.getCurrentDescription(), f.getCurrentTemp(), f.DEGREE, f.getCurrentApparentTemp(), f.DEGREE ) );
		blurb.append( String.format( "Winds: %smph from %s ", f.getCurrentWindSpeed(), f.getCurrentWindDirection()  ) );
		blurb.append( String.format( "Gusts: %smph. ", f.getCurrentWindGust() ) );
		blurb.append( String.format( "Clouds: %s%s ", f.getCurrentCloudCoverage(), f.PERCENT ) );
		blurb.append( String.format( "Dewpoint: %s%sF ", f.getCurrentDewPoint(), f.DEGREE ) );
		blurb.append( String.format( "Humidity: %s%s ", f.getCurrentHumidity(), f.DEGREE ) );
		blurb.append( String.format( "Pressure: %smbar ", f.getCurrentPressure() ) );
		return blurb.toString();
	}
	
	private static class Singleton {
		private static DarkSkyApiWrapper INSTANCE;
	}
	
	public static DarkSkyApiWrapper getInstance() {
		if( Singleton.INSTANCE == null ) {
			File f = new File( DSKFILE );
			if( !f.exists() )
				Singleton.INSTANCE = new DarkSkyApiWrapper();
			else {
				try {
					FileInputStream fis = new FileInputStream( f );
					ObjectInputStream ois = new ObjectInputStream( fis );
					Singleton.INSTANCE = (DarkSkyApiWrapper) ois.readObject();
					ois.close();
					fis.close();
				} 
				catch (IOException | ClassNotFoundException e) {
					Singleton.INSTANCE = new DarkSkyApiWrapper();
				}
			}
		}
		return Singleton.INSTANCE;

	}
	
	private Object readResolve() throws ObjectStreamException {
		return Singleton.INSTANCE;
	}

	public void serializeTheStuff() throws IOException {
		File f = new File( DSKFILE );
		FileOutputStream fos = new FileOutputStream( f );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		oos.writeObject( this );
		oos.close();
		fos.close();
	}
	

	
}
