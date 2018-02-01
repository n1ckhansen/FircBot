package com.blackfez.models.geolocation;

import java.io.Serializable;

public class Location implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String LATITUDE;
	private String LONGITUDE;
	private String ZIP;
	
	public Location() {}
	
	public String getLatitude() {
		this.checkResults();
		return this.LATITUDE.replaceAll("\"", "" );
	}
	
	public String getLongitude() {
		this.checkResults();
		return this.LONGITUDE.replaceAll("\"", "" );
	}
	
	public String getZip() {
		return this.ZIP.replaceAll("\"", "" );
	}
	
	public boolean isZipSet() {
		return !com.google.common.base.Strings.isNullOrEmpty( this.ZIP );
	}
	
	public void setLatitude( String lat ) {
		this.LATITUDE = lat;
	}
	
	public void setLongitude( String lon ) {
		this.LONGITUDE = lon;
	}
	
	public void setZip( String zip ) {
		this.ZIP = zip;
	}
	
	private void checkResults() {
		if( com.google.common.base.Strings.isNullOrEmpty(this.ZIP ) ) {
			throw new NullPointerException( "Attempting to find location of ZIP that is not set." );
		}
	}
}
