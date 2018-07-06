package com.blackfez.apis.darksky;

import java.io.IOException;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public class DarkSkyApiWrapperClient {

	public static void main(String[] args) throws ClassNotFoundException, IOException, ConfigurationException {
		ConfigurationManager cm = new ConfigurationManager( "DSKWrapperClient" );
		ZipCodeApiWrapper zwrap = new ZipCodeApiWrapper( cm );
		DarkSkyApiWrapper wrap = new DarkSkyApiWrapper( cm, zwrap );
		System.out.println( wrap.retrieveCurrentWeatherForZip( "51503" ) );
	}

}
