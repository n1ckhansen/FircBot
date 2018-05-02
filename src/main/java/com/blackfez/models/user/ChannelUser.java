package com.blackfez.models.user;

import java.util.HashSet;
import java.util.Set;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;

public class ChannelUser implements IChannelUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Location LOCATION; 
	private Set<String> CHANNELS;
	private String nic;
	
	public ChannelUser( String nic ) { 
		this.setNic( nic );
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
		return this.nic;
	}
	
	public void removeChannel( String channel ) {
		this.CHANNELS.remove( channel.replaceAll( "\"", "" ) );
	}
	
	public void setLocation( Location loc ) {
		this.LOCATION = loc;
	}
	
	public void setNic( String nic ) {
		this.nic = nic;
	}
	
}
