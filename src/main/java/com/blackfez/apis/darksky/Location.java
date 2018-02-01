package com.blackfez.apis.darksky;

public class Location {

	private double latitude;
	private double longitude;
	
	public Location( String locString ) {
		boolean isZip = false;
		try {
			Integer.parseInt( locString );
			if( locString.length() == 5 ) {
				setLocationFromZip( locString );
			}
		}
		catch( NumberFormatException e ) {
			// Testing for zip.  Expecting this if it cannot convert
		}
	}
	
	private void setLocationFromZip( String zip ) {
		// TODO: make this happen--zip to lat,lon
	}
	
}
