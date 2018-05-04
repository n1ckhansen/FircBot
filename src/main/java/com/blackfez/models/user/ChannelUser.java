package com.blackfez.models.user;

import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;
import com.rometools.utils.Strings;

public class ChannelUser implements IChannelUser {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Location LOCATION; 
	private String nic;
	
	public ChannelUser() {
	}
	
	public ChannelUser( String nic ) { 
		this.setNic( nic );
	}
	
	public Location getLocation() {
		return this.LOCATION;
	}

	public String getNic() {
		return Strings.isNotEmpty( this.nic ) ? "" : this.nic;
	}
	
	public void setLocation( Location loc ) {
		this.LOCATION = loc;
	}
	
	public void setNic( String nic ) {
		this.nic = nic;
	}
	
}
