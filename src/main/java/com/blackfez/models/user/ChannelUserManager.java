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
	private transient Map<String,User> fuserToPuserMap;
	
	@SuppressWarnings("unchecked")
	public ChannelUserManager( ConfigurationManager configManager ) throws ClassNotFoundException, IOException {
		this.cm = configManager;
		File f = new File( cm.getString( mapFileKey,"channelUserMap.xml" ) );
		if( ! f.exists() ) {
			System.out.println( "chanuserfile did not exist.  Creating object." );
			this.setUserMap(new HashMap<String,IChannelUser>());
			System.out.println( "chanuser map created.  Let's save it.");
			ObjectSerializerIO.WriteObject( cm.getString( mapFileKey ), this.userMap );
			System.out.println( "Successfully saved it." );
		}
		this.setUserMap( (Map<String,IChannelUser>) ObjectSerializerIO.LoadObject( cm.getString( mapFileKey ) ) );
		System.out.println( "Successful load of just saved user map." );
		fuserToPuserMap = new HashMap<String,User>();
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

	@Override
	public void processOnUserList(String channel, User[] users) {
		System.out.println( "Processing OnUserList" );
		System.out.println( "Processing for channel " + channel );
		System.out.println( users.length );
		for( User u : users ) {
			if( this.userMap.containsKey( u.getNick() ) ) {
				System.out.println( "Found key for user " + u.getNick() );
				this.userMap.get( u.getNick() ).addChannel( channel );
				System.out.println( "Added channel " + channel + " for user " + userMap.get( u.getNick() ).getNic() );
				this.setUserForNic( u.getNick(), u );
				System.out.println( "Set User " + u.getNick() + " for user " + userMap.get( u.getNick() ).getNic() );
			}
			else {
				System.out.println( "Lets make a key for user " + u.getNick() );
				this.userMap.put( u.getNick(), new ChannelUser( u.getNick() ) );
				System.out.println( "Made user " + userMap.get( u.getNick() ) + " for user " + u.getNick() );
				this.userMap.get( u.getNick() ).addChannel( channel );
				System.out.println( "Added channel " + channel + " for user " + userMap.get( u.getNick() ).getNic() );
				this.setUserForNic( u.getNick(), u );
				System.out.println( "Set User " + u.getNick() + " for user " + userMap.get( u.getNick() ).getNic() );
			}
			try {
				saveUserMap();
			} 
			catch (IOException e) {
				System.out.println( "Failed to save map after processing user " + u.getNick());
				e.printStackTrace();
				System.exit( 1 );
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
	
	public void setUserMap( Map<String,IChannelUser> map ) throws IOException {
		this.userMap = map;
		this.saveUserMap();
	}

	@Override
	public void updateUserLocation(String nic, Location loc) {
		if( userMap.containsKey( nic ) ) {
			userMap.get( nic ).setLocation( loc );
		}
	}

	@Override
	public User getUserForNic(String nic) {
		return fuserToPuserMap.get( nic );
	}

	@Override
	public void setUserForNic(String nic, User user) {
		fuserToPuserMap.put( nic, user );
	}

}
