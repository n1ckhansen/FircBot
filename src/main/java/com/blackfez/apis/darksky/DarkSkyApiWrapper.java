package com.blackfez.apis.darksky;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
import com.blackfez.fezcore.utilities.IO.URLUtils;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;


public class DarkSkyApiWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient final String DSURL = "https://api.darksky.net/forecast/";
	private transient final String DSKEY_KEY = "darkSkyWrapper/darkSkyKey";
	private transient final String DSCACHE_KEY = "darkSkyWrapper/cacheFile";
	private transient ZipCodeApiWrapper zipWrap;
	private transient ConfigurationManager cm;
	private transient String DSKEY;
		
	private Map<Location,Forecast> CACHE;

	@SuppressWarnings("unchecked")
	public DarkSkyApiWrapper( ConfigurationManager configManager, ZipCodeApiWrapper zcw ) throws ClassNotFoundException, IOException {
		cm = configManager;
		zipWrap = zcw;
		DSKEY = cm.getStringValue( DSKEY_KEY, "37a15c6db486688f88dff78d57d2edb4" );
		File f = new File( cm.getStringValue( DSCACHE_KEY, "dskWrapperCache.xml" ) );
		if( !f.exists() ) {
			CACHE = new HashMap<Location,Forecast>();
			serializeTheStuff();
		}
		else
			CACHE = (Map<Location,Forecast>)ObjectSerializerIO.LoadObject( cm.getStringValue( DSCACHE_KEY ) );
	}
	
	public Forecast getForcastForZip( String zip ) {
		Location loc = zipWrap.getLocation(zip);
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
			f.setJsonForecast( URLUtils.GetUrlResult( url ) );
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
		Location loc = zipWrap.getLocation( zip );
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
		Location loc = zipWrap.getLocation( zip );
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
		ObjectSerializerIO.WriteObject( cm.getStringValue( DSCACHE_KEY ), CACHE );
	}
	
}
