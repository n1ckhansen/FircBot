package com.blackfez.apis.zipcodeapi;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
import com.blackfez.fezcore.utilities.IO.URLUtils;
import com.blackfez.models.geolocation.*;
import com.google.gson.JsonParser;

public final class ZipCodeApiWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private transient final String LOOKUPS_KEY = "zipWrapper/lookupCacheFile";
	private transient final String API_KEY = "zipWrapper/apiKey";
	private transient ConfigurationManager cm;
	private transient String API;
	private transient final String URI = "https://www.zipcodeapi.com/rest/";
	private transient final String INFO = "/info.json/";
	private transient final String UNITS = "/degrees";
	
	private Map<String,Location> LOOKUPS;

	@SuppressWarnings("unchecked")
	public ZipCodeApiWrapper( ConfigurationManager configManager ) throws ClassNotFoundException, IOException {
		cm = configManager;
		API = cm.getStringValue( API_KEY, "WtH14qwX8hJ75fsnRKjiZRCZVNvPNwJR8RvqPvDBVNebRN9QctOFS5RJQtjwCtHB" );
		File f = new File( cm.getStringValue( LOOKUPS_KEY, "zipCodeCache.xml" ) );
		if( !f.exists() ) {
			LOOKUPS = new HashMap<String,Location>();
			serializeTheStuff();
		}
		else
			this.LOOKUPS = (Map<String,Location>)ObjectSerializerIO.LoadObject( cm.getStringValue( LOOKUPS_KEY ) );
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
			JsonObject results = (JsonObject) parser.parse( URLUtils.GetUrlResult( url ) ); 
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
		ObjectSerializerIO.WriteObject( cm.getStringValue(LOOKUPS_KEY),  LOOKUPS );
	}

}
