package com.blackfez.models.user.interfaces;

import java.util.Map;

import com.blackfez.models.geolocation.Location;

public interface IChannelUserManager {
	
	public Map<String,IChannelUser> getUserMap();
	
	public IChannelUser getUser( String nic );
	
	public void addChannelUser( String nic, IChannelUser user );
	
	public void removeChannelUser( String nic );
	
	public void updateUserLocation( String nic, Location loc );
	
	public void addUserChannel( String nic, String channel );
	
	public void removeUserChannel( String nic, String channel );

}
