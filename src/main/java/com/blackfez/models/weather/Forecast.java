package com.blackfez.models.weather;

import java.util.Calendar;

import javax.json.JsonObject;

import com.jayway.jsonpath.JsonPath;

public class Forecast {
	
	private Calendar TIMESTAMP;
	private JsonObject jsonForecast;
 	public final String DEGREE = "\u00b0";
 	public final String PERCENT = "\u0025";
	
	public Forecast() {
		this.TIMESTAMP = Calendar.getInstance();
	}
	
	public String getCurrentApparentTemp() {
		return JsonPath.read( this.jsonForecast, "$.currently.apparentTemperature" ).toString();
	}
	
	public String getCurrentCloudCoverage() {
		Double d = Double.parseDouble( JsonPath.read( this.jsonForecast, "$.currently.cloudCover" ).toString() );
		d = d * 100;
		Long l = Math.round( d );
		return l.toString();
	}
	
	public String getCurrentDescription() {
		return JsonPath.read( this.jsonForecast, "$.currently.summary" ).toString().replaceAll( "\"", "" );
	}
	
	public String getCurrentDewPoint() {
		return JsonPath.read( this.jsonForecast, "$.currently.dewPoint" ).toString();
	}
	
	public String getCurrentHumidity() {
		Double d = Double.parseDouble( JsonPath.read( this.jsonForecast, "$.currently.humidity" ).toString() );
		d = d * 100;
		Long l = Math.round( d );
		return l.toString();
	}
	
	public String getCurrentPressure() {
		return JsonPath.read( this.jsonForecast, "$.currently.pressure" ).toString();
	}

	public String getCurrentTemp() {
		return JsonPath.read( this.jsonForecast,  "$.currently.temperature" ).toString();
	}

	public String getCurrentWindDirection() {
		Integer dir = Integer.parseInt( JsonPath.read( this.jsonForecast, "$.currently.windBearing" ).toString() ) % 360;
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
		return JsonPath.read( this.jsonForecast, "$.currently.windGust" ).toString();
	}
	
	public String getCurrentWindSpeed() {
		return JsonPath.read(  this.jsonForecast, "$.currently.windSpeed" ).toString();
	}

	public JsonObject getJsonForecast() {
		return this.jsonForecast;
	}
	
	public boolean isCurrent() {
		Calendar now = Calendar.getInstance();
		return now.compareTo(TIMESTAMP) < 3600000;
	}
	
	public void setJsonForecast( JsonObject jf ) {
		this.jsonForecast = jf;
	}

}
