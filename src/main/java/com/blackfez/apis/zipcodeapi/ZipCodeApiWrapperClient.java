package com.blackfez.apis.zipcodeapi;

import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public class ZipCodeApiWrapperClient {

	public static void main(String[] args) {
		ZipCodeApiWrapper zipwrap;
		try {
			zipwrap = new ZipCodeApiWrapper( new ConfigurationManager( "zipWrapTest.xml" ) );
			System.out.println( "zipwrap is " + zipwrap.toString() );
			System.out.println( "lat is '" + zipwrap.getLatitude("51503") + "'" );
			System.out.println( "lng is '" + zipwrap.getLongitude("51503") + "'" );
			System.out.println( "zipwrap is " + zipwrap.toString() );
			System.out.println( "lat is '" + zipwrap.getLatitude("51503") + "'" );
			System.out.println( "lng is '" + zipwrap.getLongitude("51503") + "'" );
		} catch (ClassNotFoundException | IOException | ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
