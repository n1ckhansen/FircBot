package com.blackfez.models.user;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jibble.pircbot.User;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;
import com.blackfez.models.user.interfaces.IChannelUserManager;

public class ChannelUserManager implements IChannelUserManager {

	private ConfigurationManager cm;
	private static final String MAP_FILE_KEY = "dataFiles/channelUserMap";
	private static final String TRACKER_FILE_KEY = "dataFiles/channelUserTracker";
	private Map<String,IChannelUser> userMap;
	private Map<String,Set<IChannelUser>> channelUserTracker;
	
	public ChannelUserManager( ConfigurationManager configManager ) {
		cm = configManager;
		initChanUserMap();
		initChanUserTracker();
	}
	
	public void addChannelUser( String nic, IChannelUser user ) {
		if( userMap.containsKey( nic ) )
			return;
		userMap.put( nic, user );
		serializeUserMap();
	}
	
	public void addUser( String nic, IChannelUser user, String channel ) {
		addChannelUser( nic, user );
		addUserToChannel( channel, user);
	}
	
	public void addUserToChannel( String channel, IChannelUser user ) {
		if( !channelUserTracker.containsKey( channel ) )
			channelUserTracker.put( channel, new HashSet<IChannelUser>() );
		for( IChannelUser u : channelUserTracker.get( channel ) ) {
			if( u.getNic().equals( user.getNic() ) )
				return;
		}
		channelUserTracker.get( channel ).add( user );
		serializeChannelUserTracker();
	}
	
	public IChannelUser getChannelUser( String nic ) {
		return userMap.get( nic );
	}
	
	public Map<String,Set<IChannelUser>> getChannelUserTracker() {
		return this.channelUserTracker;
	}
	
	public Map<String,IChannelUser> getUserMap() {
		return this.userMap;
	}
	
	public Set<IChannelUser> getUsersForChannel( String channel ) {
		if( channelUserTracker.containsKey( channel ) )
			return channelUserTracker.get( channel );
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void initChanUserMap() {
		File f = new File( cm.getStringValue(MAP_FILE_KEY, "channelUserMap.xml" ) );
		if( !f.exists() ) {
			this.setUserMap( new HashMap<String,IChannelUser>() );
			serializeUserMap();
		}
		try {
			setUserMap( (Map<String,IChannelUser>) ObjectSerializerIO.LoadObject( f ) );
		} 
		catch (ClassNotFoundException | IOException e) {
			System.out.println( "Unable to load user/User map from disk.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initChanUserTracker() {
		File f = new File( cm.getStringValue( TRACKER_FILE_KEY, "channelUserTracker.xml" ) );
		if( !f.exists() ) {
			this.setChannelUserTracker( new HashMap<String, Set<IChannelUser>>() );
			try {
				ObjectSerializerIO.WriteObject( f, getChannelUserTracker() );
			}
			catch( IOException e ) {
				System.out.println( "Unable to serialize channel/user map.  Exiting application" );
				e.printStackTrace();
				System.exit( 1 );
			}
		}
		try {
			setChannelUserTracker( (Map<String,Set<IChannelUser>>) ObjectSerializerIO.LoadObject( f ) );
		}
		catch (ClassNotFoundException | IOException e) {
			System.out.println( "Unable to load channel/User map from disk.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public void processOnUserList( String channel, User[] users ) {
		for( User u : users ) {
			if( !userMap.containsKey( u.getNick() ) ) {
				ChannelUser user = new ChannelUser( u.getNick() );
				addChannelUser( u.getNick(), user );
			}
			addUserToChannel( channel, getChannelUser( u.getNick() ) );
		}
	}
	
	public void removeChannelUser( String nic ) {
		userMap.remove( nic );
	}
	
	public void removeUserFromChannel( String channel, IChannelUser user ) {
		channelUserTracker.get( channel ).remove( user );
	}
	
	public void removeUser( IChannelUser user ) {
		if( userMap.containsKey( user.getNic() ) )
			removeChannelUser( user.getNic() );
		for( String channel : channelUserTracker.keySet() ) {
			if( channelUserTracker.get( channel ).contains( user ) )
				removeUserFromChannel( channel, user );
		}
	}
	
	private void serializeChannelUserTracker() {
		File f = new File( cm.getStringValue( TRACKER_FILE_KEY ) );
		try {
			ObjectSerializerIO.WriteObject( f, this.getChannelUserTracker() );
		}
		catch (IOException e) {
			System.out.println( "Unable to serialize channel/User map.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	private void serializeUserMap() {
		File f = new File( cm.getStringValue(MAP_FILE_KEY ) );
		try {
			ObjectSerializerIO.WriteObject( f, this.getUserMap() );
		}
		catch (IOException e) {
			System.out.println( "Unable to serialize user/User map.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public void setChannelUserLocation( String nic, Location loc ) {
		userMap.get( nic ).setLocation( loc );
		serializeUserMap();
		serializeChannelUserTracker();
	}
	
	public void setChannelUserTracker( Map<String,Set<IChannelUser>> map ) {
		this.channelUserTracker = map;
	}
	
	public void setUserMap( Map<String,IChannelUser> map ) {
		this.userMap = map;
	}

}
