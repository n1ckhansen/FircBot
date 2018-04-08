package com.blackfez.models.user;

import java.util.HashMap;
import java.util.Map;

import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;
import com.blackfez.models.user.interfaces.IChannelUserManager;

public class ChannelUserManager implements IChannelUserManager {
	
	private Map<String,IChannelUser> userMap;
	
	public ChannelUserManager( ) {
		userMap = new HashMap<String,IChannelUser>();
	}
	
	@Override
	public Map<String, IChannelUser> getUserMap() {
		return userMap;
	}

	@Override
	public IChannelUser getUser(String nic) {
		return userMap.get( nic );
	}

	@Override
	public void addChannelUser(String nic, IChannelUser user) {
		if( !userMap.containsKey( nic ) )
			userMap.put( nic, user );
		else {
			//raise some kind of error?
			//do we care?
		}
	}

	@Override
	public void removeChannelUser(String nic) {
		if( userMap.containsKey( nic ) )
			userMap.remove( nic );
		//raise error of some kind if not containskey?
	}

	@Override
	public void updateUserLocation(String nic, Location loc) {
		if( userMap.containsKey( nic ) ) {
			userMap.get( nic ).setLocation( loc );
		}
	}

	@Override
	public void addUserChannel(String nic, String channel) {
		if( userMap.containsKey( nic ) ) {
			userMap.get( nic ).addChannel( channel );
		}
	}

	@Override
	public void removeUserChannel(String nic, String channel) {
		userMap.get( nic ).removeChannel( channel );
	}
}
