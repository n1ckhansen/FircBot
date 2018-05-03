package com.blackfez.models.user;

import org.jibble.pircbot.User;

import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;

public class ChannelUser implements IChannelUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Location LOCATION; 
	private String nic;
	private transient User puser;
	
	public ChannelUser() {
		this.setNic( null );
		puser = null;
	}
	
	public ChannelUser( String nic ) { 
		this.setNic( nic );
		puser = null;
	}
	
	public Location getLocation() {
		return this.LOCATION;
	}

	public String getNic() {
		return this.nic;
	}
	
	public User getPuser() {
		return puser;
	}
	
	public void setLocation( Location loc ) {
		this.LOCATION = loc;
	}
	
	public void setNic( String nic ) {
		this.nic = nic;
	}
	
	public void setPuser( User user ) {
		puser = user;
	}
	
}
