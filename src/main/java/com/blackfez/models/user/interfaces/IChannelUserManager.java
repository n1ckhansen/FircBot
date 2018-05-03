package com.blackfez.models.user.interfaces;

import java.util.Map;
import java.util.Set;

import org.jibble.pircbot.User;

public interface IChannelUserManager {

	public void addChannelUser( String nic, IChannelUser user ); 
	
	public void addUser( String nic, IChannelUser user, String channel );
	
	public void addUserToChannel( String channel, IChannelUser user );

	public IChannelUser getChannelUser( String nic );
	
	public Map<String,Set<IChannelUser>> getChannelUserTracker();

	public Map<String,IChannelUser> getUserMap();
	
	public Set<IChannelUser> getUsersForChannel( String channel );
	
	public void processOnUserList( String channel, User[] users );

	public void removeChannelUser( String nic );
	
	public void removeUserFromChannel( String channel, IChannelUser user );
	
	public void removeUser( IChannelUser user );
	
	public void setChannelUserTracker( Map<String,Set<IChannelUser>> map );

	public void setUserMap( Map<String,IChannelUser> map );

}
