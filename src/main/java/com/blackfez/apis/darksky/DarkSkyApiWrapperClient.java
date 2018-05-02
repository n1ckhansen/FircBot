package com.blackfez.apis.darksky;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.configuration2.ex.ConfigurationException;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DarkSkyApiWrapperClient {

	public static void main(String[] args) throws ClassNotFoundException, IOException, ConfigurationException {
		ConfigurationManager cm = new ConfigurationManager( "DSKWrapperClient" );
		ZipCodeApiWrapper zwrap = new ZipCodeApiWrapper( cm );
		DarkSkyApiWrapper wrap = new DarkSkyApiWrapper( cm, zwrap );
		System.out.println( wrap.retrieveCurrentWeatherForZip( "51503" ) );
	}

}
