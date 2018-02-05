package com.blackfez.models.user;

import java.util.HashSet;
import java.util.Set;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;

public class ChannelUser {
	
	private Location LOCATION; 
	private User USER;
	private Set<String> CHANNELS;
	
	public ChannelUser( User user) { 
		this.setUser( user );
		this.CHANNELS = new HashSet<String>(); 
	}
	
	public void addChannel( String channel ) {
		this.CHANNELS.add( channel.replaceAll( "\"", "" ) );
	}
	
	public Set<String> getChannels() {
		return this.CHANNELS;
	}
	
	public Location getLocation() {
		return this.LOCATION;
	}

	public String getNic() {
		return this.USER.getNick();
	}
	
	public void removeChannel( String channel ) {
		this.CHANNELS.remove( channel.replaceAll( "\"", "" ) );
	}
	
	public void setLocation( Location loc ) {
		this.LOCATION = loc;
	}
	
	public void setUser( User user ) {
		this.USER = user;
	}

}
