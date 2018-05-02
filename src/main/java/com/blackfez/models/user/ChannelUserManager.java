package com.blackfez.models.user;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jibble.pircbot.User;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.fezcore.utilities.IO.*;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;
import com.blackfez.models.user.interfaces.IChannelUserManager;

public class ChannelUserManager implements IChannelUserManager {
	
	private static final transient long serialVersionUID = 1L;
	private transient ConfigurationManager cm;
	private transient String mapFileKey = "dataFiles/channelUserMap";

	private Map<String,IChannelUser> userMap;
	
	public ChannelUserManager( ConfigurationManager configManager ) throws ClassNotFoundException, IOException {
		this.cm = configManager;
		loadUserMap();
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
	public void addUserChannel(String nic, String channel) {
		if( userMap.containsKey( nic ) ) {
			userMap.get( nic ).addChannel( channel );
		}
	}

	@Override
	public IChannelUser getUser(String nic) {
		return userMap.get( nic );
	}

	@Override
	public Map<String, IChannelUser> getUserMap() {
		return userMap;
	}

	@SuppressWarnings("unchecked")
	// which is bullshit because java generics have type erasure at runtime
	@Override
	public void loadUserMap() throws IOException, ClassNotFoundException {
		File f = new File( cm.getString( mapFileKey,"channelUserMap.xml" ) );
		if( ! f.exists() ) {
			this.setUserMap(new HashMap<String,IChannelUser>());
			ObjectSerializerIO.WriteObject( cm.getString( mapFileKey ), this.userMap );
		}
		Object payload = ObjectSerializerIO.LoadObject( cm.getString( mapFileKey ) );
		// at least we're checking it's fricking map, yo.
		if( payload instanceof Map<?,?> )  {
		    this.setUserMap((Map<String,IChannelUser>) payload );
		}
	}

	@Override
	public void processOnUserList(String channel, User[] users) {
		for( User u : users ) {
			if( this.userMap.containsKey( u.getNick() ) ) {
				this.userMap.get( u.getNick() ).addChannel( channel );
				this.userMap.get( u.getNick() ).setUser( u );
			}
			else {
				this.userMap.put( u.getNick(), new ChannelUser( u.getNick() ) );
				this.userMap.get( u.getNick() ).addChannel( channel );
				this.userMap.get( u.getNick() ).setUser( u );
			}
		}
	}

	@Override
	public void removeChannelUser(String nic) {
		if( userMap.containsKey( nic ) )
			userMap.remove( nic );
		//raise error of some kind if not containskey?
	}

	@Override
	public void removeUserChannel(String nic, String channel) {
		userMap.get( nic ).removeChannel( channel );
	}

	@Override
	public void saveUserMap() throws IOException {
		ObjectSerializerIO.WriteObject( cm.getString( mapFileKey ), this.userMap );
	}
	
	private void setUserMap( Map<String,IChannelUser> map ) {
		this.userMap = map;
	}

	@Override
	public void updateUserLocation(String nic, Location loc) {
		if( userMap.containsKey( nic ) ) {
			userMap.get( nic ).setLocation( loc );
		}
	}

}
