package com.blackfez.models.weather;

import java.io.Serializable;
import java.util.Calendar;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;

public class Forecast implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar TIMESTAMP;
	private String jsonForecast;
 	public transient final String DEGREE = "\u00b0";
 	public transient final String PERCENT = "\u0025";
	
	public Forecast() {
		this.setTimestamp( Calendar.getInstance() );
	}
	
	public String getCurrentApparentTemp() {
		
		return JsonPath.read( getJsonForecast(), "$.currently.apparentTemperature" ).toString();
	}
	
	public String getCurrentCloudCoverage() {
		Double d = Double.parseDouble( JsonPath.read( getJsonForecast(), "$.currently.cloudCover" ).toString() );
		d = d * 100;
		Long l = Math.round( d );
		return l.toString();
	}
	
	public String getCurrentDescription() {
		return JsonPath.read( getJsonForecast(), "$.currently.summary" ).toString().replaceAll( "\"", "" );
	}
	
	public String getCurrentDewPoint() {
		return JsonPath.read( getJsonForecast(), "$.currently.dewPoint" ).toString();
	}
	
	public String getCurrentHumidity() {
		Double d = Double.parseDouble( JsonPath.read( getJsonForecast(), "$.currently.humidity" ).toString() );
		d = d * 100;
		Long l = Math.round( d );
		return l.toString();
	}
	
	public String getCurrentPressure() {
		return JsonPath.read( getJsonForecast(), "$.currently.pressure" ).toString();
	}

	public String getCurrentTemp() {
		return JsonPath.read( getJsonForecast(),  "$.currently.temperature" ).toString();
	}

	public String getCurrentWindDirection() {
		Integer dir = Integer.parseInt( JsonPath.read( getJsonForecast(), "$.currently.windBearing" ).toString() ) % 360;
		if( 348 <= dir || dir < 12  )
			return "N";
		else if( 12 <= dir && dir < 35 )
			return "NNE";
		else if ( 35 <= dir && dir < 58 )
			return "NE";
		else if ( 58 <= dir && dir < 81 ) 
			return "ENE";
		else if ( 81 <= dir && dir < 104 )
			return "E";
		else if ( 104 <= dir && dir < 127 )
			return "ESE";
		else if ( 127 < dir && dir < 150 )
			return "SE";
		else if ( 150 <= dir && dir < 173 ) 
			return "SSE";
		else if ( 173 <= dir && dir < 196 )
			return "S";
		else if ( 196 <= dir && dir < 219 )
			return "SSW";
		else if ( 219 <= dir && dir < 242 )
			return "SW";
		else if ( 242 <= dir && dir < 265 ) 
			return "WSW";
		else if ( 265 <= dir && dir < 278 )
			return "W";
		else if ( 278 <= dir && dir < 301 )
			return "WNW";
		else if ( 301 <= dir && dir < 324 )
			return "NW";
		else if ( 324 <= dir && dir < 348 )
			return "NNW";
		else 
			return null;
	}
	
	public String getCurrentWindGust() {
		return JsonPath.read( getJsonForecast(), "$.currently.windGust" ).toString();
	}
	
	public String getCurrentWindSpeed() {
		return JsonPath.read(  getJsonForecast(), "$.currently.windSpeed" ).toString();
	}

	public String getJsonForecast() {
		return this.jsonForecast;
	}
	
	public boolean isCurrent() {
		System.out.println( "NOW: " + Calendar.getInstance().getTimeInMillis() );
		System.out.println( "TIMESTAMP: " + TIMESTAMP.getTimeInMillis() );
		System.out.println( "Math: " + ( Calendar.getInstance().getTimeInMillis() - TIMESTAMP.getTimeInMillis() ) / 100L / 60L / 60L );
		return ( Calendar.getInstance().getTimeInMillis() - TIMESTAMP.getTimeInMillis() ) / 1000L / 60L / 60L < 1;
	}
	
	public void setJsonForecast( String results ) {
		this.jsonForecast = results;
	}
	
	public Calendar getTimestamp() {
		return this.TIMESTAMP;
	}
	
	public void setTimestamp( Calendar ts ) {
		this.TIMESTAMP = ts;
	}
}
