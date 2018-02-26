package com.blackfez.apis.darksky;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.IOUtils;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;


public class DarkSkyApiWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient static final String DSKFILE = "dskForecasts.ser";
	private transient final String DSKEY = "37a15c6db486688f88dff78d57d2edb4";
	private transient final String DSURL = "https://api.darksky.net/forecast/";
	private transient static DarkSkyApiWrapper INSTANCE = null;
	
	private Map<Location,Forecast> CACHE;

	private DarkSkyApiWrapper() {}
	
	public static DarkSkyApiWrapper getInstance() {
		if( null != INSTANCE ) 
			return INSTANCE;
		synchronized( DarkSkyApiWrapper.class ) {
			if( null != INSTANCE )
				return INSTANCE;
			File f = new File( DSKFILE );
			if( f.exists() ) {
				try {
					INSTANCE = (DarkSkyApiWrapper) IOUtils.LoadObject( DSKFILE );
				}
				catch( IOException | ClassNotFoundException e ) {
					INSTANCE = new DarkSkyApiWrapper();
					return INSTANCE;
				}
			}
			else {
				INSTANCE = new DarkSkyApiWrapper();

			}
			return INSTANCE;
		}
	}
	
	public Forecast getForcastForZip( String zip ) {
		Location loc = ZipCodeApiWrapper.getInstance().getLocation(zip);
		Forecast forecast = this.getForcastForCoords( loc );
		return forecast;
	}
	
	public Map<Location,Forecast> getCache() {
		if( null == CACHE )
			CACHE = new HashMap<Location,Forecast>();
		return this.CACHE;
	}
	
	public void setCache( Map<Location,Forecast> cache ) {
		this.CACHE = cache;
	}
	
	public Forecast getForcastForCoords( Location loc ) {
		Location f = null;
		for( Location l : this.getCache().keySet() ) {
			if( l.getZip().equals( loc.getZip() ) ) {
				f = l;
				break;
			}
		}
		if( null != f && this.getCache().get( f ).isCurrent() ) {
			return this.getCache().get( f );
		}
		else if( null != f && ! this.getCache().get( f ).isCurrent() ) {
			this.getCache().remove( f );
		}
		Forecast forecast = consultDarkSky( loc );
		return forecast;
	}
	
	private Forecast consultDarkSky( Location loc ) {
		Forecast f = new Forecast();
		try {
			URL url = new URL( String.format("%s%s/%s,%s", DSURL,DSKEY,loc.getLatitude(), loc.getLongitude() ) );
			f.setJsonForecast( IOUtils.GetUrlResult( url ) );
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
		Location loc = ZipCodeApiWrapper.getInstance().getLocation( zip );
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
	
	public String retrieveWeatherForecastForZip( String zip ) {
		Location loc = ZipCodeApiWrapper.getInstance().getLocation( zip );
		Forecast f = this.getForcastForCoords( loc );
		StringBuilder blurb = new StringBuilder();
		blurb.append( String.format( "%s, %s %s: ", loc.getCity(), loc.getState(), loc.getZip() ) );
		blurb.append( String.format( "%s ", f.getDailySummary() ) );
		int i = 0;
		while( i < 5 ) {
			blurb.append( String.format( "%s ", f.getDailySummaryForDay( i ) ) );
			blurb.append( String.format( "%s%s ", f.getHighForDay( i ), f.getLowForDay( i ) ) );
			i++;
		}
		return blurb.toString();
	}
	
	public void serializeTheStuff() throws IOException {
		IOUtils.WriteObject( DSKFILE, this );
	}
	
}
