package com.blackfez.apis.zipcodeapi;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.blackfez.applications.fircbot.utilities.IOUtils;
import com.blackfez.models.geolocation.*;
import com.google.gson.JsonParser;

public final class ZipCodeApiWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public transient static final String ZCWFILE = "zipCache.ser";
	private transient final String API = "WtH14qwX8hJ75fsnRKjiZRCZVNvPNwJR8RvqPvDBVNebRN9QctOFS5RJQtjwCtHB";
	private transient final String URI = "https://www.zipcodeapi.com/rest/";
	private transient final String INFO = "/info.json/";
	private transient final String UNITS = "/degrees";
	private transient static ZipCodeApiWrapper INSTANCE = null;
	
	private Map<String,Location> LOOKUPS = new HashMap<String,Location>();

	private ZipCodeApiWrapper() {}
	
	public static ZipCodeApiWrapper getInstance() {
		if( null != INSTANCE ) {
			return INSTANCE;
		}
		synchronized( ZipCodeApiWrapper.class ) {
			if( null != INSTANCE )
				return INSTANCE;
			File f = new File( ZCWFILE );
			if( !f.exists() ) {
				INSTANCE = new ZipCodeApiWrapper();
				return INSTANCE;
			}
			try {
				INSTANCE = (ZipCodeApiWrapper) IOUtils.LoadObject( ZCWFILE );
			}
			catch( IOException | ClassNotFoundException e ) {
				INSTANCE = new ZipCodeApiWrapper();
			}
			return INSTANCE;
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
			JsonParser parser = new JsonParser();
			JsonObject results = (JsonObject) parser.parse( IOUtils.GetUrlResult( url ) ); 
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
	
	public void serializeTheStuff() throws IOException {
		IOUtils.WriteObject( ZCWFILE,  this );
	}

}
