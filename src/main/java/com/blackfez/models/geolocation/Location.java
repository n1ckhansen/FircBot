package com.blackfez.models.geolocation;

import java.io.Serializable;

import com.google.common.base.Strings;

public class Location implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String LATITUDE;
	private String LONGITUDE;
	private String ZIP;
	private String CITY;
	private String STATE;
	
	public Location() {}
	
	public String getCity() {
		this.checkResults();
		return this.CITY.replaceAll("\"", "" );
	}

	public String getLatitude() {
		this.checkResults();
		return this.LATITUDE.replaceAll("\"", "" );
	}
	
	public String getLongitude() {
		this.checkResults();
		return this.LONGITUDE.replaceAll("\"", "" );
	}
	
	public String getState() {
		this.checkResults();
		return this.STATE.replaceAll("\"", "" );
	}
	
	public String getZip() {
		return this.ZIP.replaceAll("\"", "" );
	}
	
	public boolean isZipSet() {
		return !Strings.isNullOrEmpty( this.ZIP );
	}
	
	public void setCity( String city ) {
		this.CITY = city;
	}

	public void setLatitude( String lat ) {
		this.LATITUDE = lat;
	}
	
	public void setLongitude( String lon ) {
		this.LONGITUDE = lon;
	}
	
	public void setState( String state ) {
		this.STATE = state;
	}
	
	public void setZip( String zip ) {
		this.ZIP = zip;
	}
	
	private void checkResults() {
		if( Strings.isNullOrEmpty(this.ZIP ) ) {
			throw new NullPointerException( "Attempting to find location of ZIP that is not set." );
		}
	}
}
