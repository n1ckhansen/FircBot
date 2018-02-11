package com.blackfez.apis.zipcodeapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
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

public class ZipCodeApiWrapper implements Serializable {
	
	/**
	 * Relating to what we actually want to be doing...
	 */
	private static final long serialVersionUID = 1L;
	public transient static final String ZCWFILE = "zipCache.ser";
	private transient final String API = "WtH14qwX8hJ75fsnRKjiZRCZVNvPNwJR8RvqPvDBVNebRN9QctOFS5RJQtjwCtHB";
	private transient final String URI = "https://www.zipcodeapi.com/rest/";
	private transient final String INFO = "/info.json/";
	private transient final String UNITS = "/degrees";
	private Map<String,Location> LOOKUPS = new HashMap<String,Location>();
	
	private ZipCodeApiWrapper() {
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
			this.serializeTheStuff();
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
		private static ZipCodeApiWrapper INSTANCE;
	}
	
	public static ZipCodeApiWrapper getInstance() {
		if( Singleton.INSTANCE == null ) {
			File f = new File( ZCWFILE );
			if( !f.exists() ) 
				Singleton.INSTANCE = new ZipCodeApiWrapper();
			else {
				try {
					FileInputStream fis = new FileInputStream( f );
					ObjectInputStream ois = new ObjectInputStream( fis );
					Singleton.INSTANCE = (ZipCodeApiWrapper) ois.readObject();
				} 
				catch (IOException | ClassNotFoundException e) {
					Singleton.INSTANCE = new ZipCodeApiWrapper();
				}
			}
		}
		return Singleton.INSTANCE;
	}
	
	private Object readResolve() throws ObjectStreamException {
		return Singleton.INSTANCE;
	}

	public void serializeTheStuff() throws IOException {
		File f = new File( ZCWFILE );
		FileOutputStream fos = new FileOutputStream( f );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		oos.writeObject( this );
		oos.close();
		fos.close();
	}

}
