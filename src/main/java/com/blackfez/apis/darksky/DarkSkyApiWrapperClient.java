package com.blackfez.apis.darksky;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.blackfez.models.geolocation.Location;
import com.blackfez.models.weather.Forecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DarkSkyApiWrapperClient {

	public static void main(String[] args) throws FileNotFoundException {
		DarkSkyApiWrapper wrap = DarkSkyApiWrapper.getInstance();
		System.out.println( wrap.retrieveCurrentWeatherForZip( "51503" ) );
	}

}
