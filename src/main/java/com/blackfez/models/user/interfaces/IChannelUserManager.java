package com.blackfez.models.user.interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;

public interface IChannelUserManager extends Serializable {
	
	public IChannelUser getUser( String nic );

	public void addChannelUser( String nic, IChannelUser user );

	public void addUserChannel( String nic, String channel );

	public Map<String,IChannelUser> getUserMap();
	
	public void loadUserMap() throws IOException, ClassNotFoundException;
	
	public void processOnUserList( String channel, User[] users );
	
	public void removeChannelUser( String nic );

	public void removeUserChannel( String nic, String channel );
	
	public void updateUserLocation( String nic, Location loc );
	
	public void saveUserMap() throws IOException;

}
