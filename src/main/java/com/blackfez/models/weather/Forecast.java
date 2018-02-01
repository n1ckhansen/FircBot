package com.blackfez.models.weather;

import java.util.Calendar;

import com.blackfez.models.geolocation.Location;

public class Forecast {
	
	private Location LOCATION;
	private Calendar TIMESTAMP;
	
	public Forecast( Location loc ) {
		this.LOCATION = loc;
		this.TIMESTAMP = Calendar.getInstance();
	}
	
	public boolean isCurrent() {
		Calendar now = Calendar.getInstance();
		return now.compareTo(TIMESTAMP) < 3600000;
	}

}
