package com.blackfez.apis.darksky;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;


public class DarkSkyApiWrapper {

	private final String DSKEY = "37a15c6db486688f88dff78d57d2edb4";
	private final String DSURL = "https://api.darksky.net/forecast/";
	private Map<Location,Forecast> CACHE;
	private final ZipCodeApiWrapper zipper = ZipCodeApiWrapper.getInstance();
	
	private DarkSkyApiWrapper() {
		this.CACHE = new HashMap<Location,Forecast>();
	}
	
	public Forecast getForcastForZip( String zip ) {
		Location loc = zipper.getLocation(zip);
		Forecast forecast = this.getForcastForCoords( loc );
		return forecast;
	}
	
	public Forecast getForcastForCoords( Location loc ) {
		if( CACHE.containsKey( loc ) && CACHE.get( loc ).isCurrent() ) 
			return CACHE.get( loc );
		Forecast forecast = consultDarkSky( loc );
		
		return forecast;
	}
	
	private Forecast consultDarkSky( Location loc ) {
		Forecast f = new Forecast();
		try {
			URL url = new URL( String.format("%s%s/%s,%s", DSURL,DSKEY,loc.getLatitude(), loc.getLongitude() ) );
			InputStream is = url.openStream();
			JsonReader reader = Json.createReader( is );
			JsonObject results = reader.readObject();
			f.setJsonForecast( results );
			this.CACHE.put( loc, f );

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
	}
	
	public String retrieveCurrentWeatherForZip( String zip ) {
		Location loc = zipper.getLocation( zip );
		Forecast f = this.getForcastForCoords( loc );
		StringBuilder blurb = new StringBuilder();
		blurb.append( String.format( "%s, %s %s: ", loc.getCity(), loc.getState().replaceAll("\\", ""), loc.getZip() ) );
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
		private static final DarkSkyApiWrapper INSTANCE = new DarkSkyApiWrapper();
	}
	
	public static DarkSkyApiWrapper getInstance() {
		return Singleton.INSTANCE;
	}
	
	private Object readResolve() throws ObjectStreamException {
		return Singleton.INSTANCE;
	}
	

	
}
