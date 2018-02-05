package com.blackfez.apis.zipcodeapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

import com.blackfez.models.geolocation.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ZipCodeApiWrapper {
	
	/**
	 * Relating to what we actually want to be doing...
	 */
	private final String API = "WtH14qwX8hJ75fsnRKjiZRCZVNvPNwJR8RvqPvDBVNebRN9QctOFS5RJQtjwCtHB";
	private final String URI = "https://www.zipcodeapi.com/rest/";
	private final String INFO = "/info.json/";
	private final String UNITS = "/degrees";
	private Map<String,Location> LOOKUPS = new HashMap<String,Location>();
	
	private ZipCodeApiWrapper() {
		File lookups = new File( "lookups.ser" );
		if( lookups.exists() ) {
			try {
				JsonReader reader = Json.createReader( new FileInputStream( "lookups.ser" ) );
				JsonObject jObject = reader.readObject();
				for( String key : jObject.keySet() ) {
					Location loc = new Location();
					loc.setCity( jObject.get( key ). asJsonObject().getString( "CITY" ).toString() );
					loc.setLatitude( jObject.get(key).asJsonObject().get("LATITUDE").toString() );
					loc.setLongitude( jObject.get(key).asJsonObject().get("LONGITUDE").toString() );
					loc.setState( jObject.get( key ).asJsonObject().get( "STATE" ).toString() );
					loc.setZip( jObject.get(key).asJsonObject().get("ZIP").toString() );
					this.LOOKUPS.put(key, loc);
				}
			} 
			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getLatitude( String zip ) {
		return this.getLocation( zip ).getLatitude();
	}
	
	public Location getLocation( String zip ) {
		// TODO: let's do some error checking on the zip string some day
		if( !this.LOOKUPS.containsKey( zip ) ) {
			this.LOOKUPS.put(zip, this.lookupZip( zip ) );
		}
		return this.LOOKUPS.get( zip );
	}
	
	public String getLongitude( String zip ) {
		return this.getLocation( zip ).getLongitude();
	}
	
	private Location lookupZip( String zip ) {
		Location loc = new Location();
		loc.setZip(zip);
		URL url;
		try {
			url = new URL( String.format("%s%s%s%s%s", URI, API, INFO, zip, UNITS ) );
			InputStream is = url.openStream();
			JsonReader reader = Json.createReader( is );
			JsonObject results = reader.readObject();
			loc.setCity( results.get("city").toString() );
			loc.setLatitude(results.get("lat").toString());
			loc.setLongitude( results.get( "lng" ).toString() );
			loc.setState( results.get( "state" ).toString() );
			this.LOOKUPS.put(zip, loc);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			FileWriter writer = new FileWriter( "lookups.ser" );
			writer.write( gson.toJson( this.LOOKUPS) );
			writer.close();
		} 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return loc;
	}
	
	private static class Singleton {
		private static final ZipCodeApiWrapper INSTANCE = new ZipCodeApiWrapper();
	}
	
	public static ZipCodeApiWrapper getInstance() {
		return Singleton.INSTANCE;
	}
	
	private Object readResolve() throws ObjectStreamException {
		return Singleton.INSTANCE;
	}

}
