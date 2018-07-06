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
		return Strings.isNullOrEmpty( this.CITY ) ? "" : this.CITY;
	}

	public String getLatitude() {
		return Strings.isNullOrEmpty( this.LATITUDE ) ? "" : this.LATITUDE.replaceAll("\"", "" );
	}
	
	public String getLongitude() {
		return Strings.isNullOrEmpty( this.LONGITUDE ) ? "" : this.LONGITUDE.replaceAll("\"", "" );
	}
	
	public String getState() {
		return Strings.isNullOrEmpty( this.STATE ) ? "" : this.STATE.replaceAll("\"", "" );
	}
	
	public String getZip() {
		return Strings.isNullOrEmpty( this.ZIP ) ? "" : this.ZIP.replaceAll("\"", "" );
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
}
