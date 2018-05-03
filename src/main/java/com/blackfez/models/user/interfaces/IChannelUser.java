package com.blackfez.models.user.interfaces;

import java.io.Serializable;
import java.util.Set;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;

public interface IChannelUser extends Serializable {
	
	public Location getLocation();

	public String getNic();
	
	public User getPuser();
	
	public void setLocation( Location loc );
	
	public void setNic( String nic );
	
	public void setPuser( User user );
	
}
