package com.blackfez.apis.zipcodeapi;

public class ZipCodeApiWrapperClient {

	public static void main(String[] args) {
		ZipCodeApiWrapper client = ZipCodeApiWrapper.getInstance();
		System.out.println( "client is " + client.toString() );
		System.out.println( "lat is '" + client.getLatitude("51503") + "'" );
		System.out.println( "lng is '" + client.getLongitude("51503") + "'" );
		client = ZipCodeApiWrapper.getInstance();
		System.out.println( "client is " + client.toString() );
		System.out.println( "lat is '" + client.getLatitude("51503") + "'" );
		System.out.println( "lng is '" + client.getLongitude("51503") + "'" );
	}

}
