package com.blackfez.models.weather;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jayway.jsonpath.JsonPath;

public class Forecast implements Serializable {
	
	private static final long serialVersionUID = 1L;
 	public transient final String DEGREE = "\u00b0";
 	public transient final String HIGH = "\u21D1";
 	public transient final String LOW = "\u21D3";
 	public transient final String PERCENT = "\u0025";
 	
	private Calendar TIMESTAMP;
	private String jsonForecast;

	public Forecast() {
		this.setTimestamp( Calendar.getInstance() );
	}
	
	public String getCurrentApparentTemp() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.apparentTemperature" ) ) ) );
	}
	
	public String getCurrentCloudCoverage() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.cloudCover" ) ) * 100 ) );
	}
	
	public String getCurrentDescription() {
		return getJPath( "$.currently.summary" ).replaceAll( "\"", "" );
	}
	
	public String getCurrentDewPoint() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.dewPoint" ) ) ) );
	}
	
	public String getCurrentHumidity() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.humidity" ) ) * 100 ) );
	}

	public String getCurrentPressure() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.pressure" ) ) ) );
	}

	public String getCurrentTemp() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.temperature" ) ) ) );
	}

	public String getCurrentWindDirection() {
		Integer dir = Integer.parseInt( getJPath( "$.currently.windBearing" ) ) % 360;
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
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.windGust" ) ) ) );
	}
	
	public String getCurrentWindSpeed() {
		return Long.toString( Math.round( Double.parseDouble( getJPath( "$.currently.windSpeed" ) ) ) );
	}
	
	public String getDailySummary() {
		return getJPath( "$.daily.summary" );
	}
	
	public String getDailySummaryForDay( Integer day ) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis( Long.parseLong( getJPath( "$.daily.data[ " + day + " ].time" ) ) * 1000L );
		return new SimpleDateFormat( "EEE" ).format( cal.getTime() ) + ": " + getJPath( "$.daily.data[ " + day + " ].summary" );
	}
	
	public String getHighForDay( Integer day ) {
		StringBuffer sb = new StringBuffer();
		sb.append( HIGH );
		sb.append( Math.round( Float.parseFloat( getJPath( "$.daily.data[ " + day + " ].temperatureHigh" ) ) ) );
		sb.append( DEGREE );
		sb.append( "F " );
		return sb.toString();
	}
	
	public String getLowForDay( Integer day ) {
		StringBuffer sb = new StringBuffer();
		sb.append( LOW );
		sb.append( Math.round( Float.parseFloat( getJPath( "$.daily.data[ " + day + " ].temperatureLow" ) ) ) );
		sb.append( DEGREE );
		sb.append( "F " );
		return sb.toString();
	}

	public String getJsonForecast() {
		return this.jsonForecast;
	}
	
	public boolean isCurrent() {
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
	
	private String getJPath( String exp ) {
		return JsonPath.read( getJsonForecast(), exp ).toString();
	}
}
