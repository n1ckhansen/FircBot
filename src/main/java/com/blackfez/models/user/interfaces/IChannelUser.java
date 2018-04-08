package com.blackfez.models.user.interfaces;

import java.util.Set;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;

public interface IChannelUser {
	
	public void addChannel( String channel ); 
	
	public Set<String> getChannels(); 
	
	public Location getLocation();

	public String getNic();
	
	public User getUser();
	
	public void removeChannel( String channel );
	
	public void setLocation( Location loc );
	
	public void setNic( String nic );
	
	public void setUser( User user );

}
