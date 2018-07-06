package com.blackfez.models.user.interfaces;

import java.io.Serializable;

import com.blackfez.models.geolocation.Location;

public interface IChannelUser extends Serializable {
	
	public Location getLocation();

	public String getNic();
	
	public void setLocation( Location loc );
	
	public void setNic( String nic );
	
}
