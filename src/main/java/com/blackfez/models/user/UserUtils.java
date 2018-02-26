package com.blackfez.models.user;

import java.rmi.NoSuchObjectException;
import java.util.Map;

import com.blackfez.models.geolocation.Location;

public class UserUtils {
	
	public static ChannelUser UpdateUserLocation( String sender, String msg, Map<String,ChannelUser> chanUsers) throws NoSuchObjectException {
		String[] tokens =  msg.split( " " );
		if( chanUsers.containsKey( sender ) ) {
			ChannelUser cu = chanUsers.get( sender );
			if( tokens.length >= 2 ) {
				Location loc = new Location();
				loc.setZip( tokens[ 1 ] );
				cu.setLocation( loc );
			}
			if( cu.getLocation() == null )
				cu.setLocation( new Location() );
			return cu;
		}
		throw new NoSuchObjectException( String.format( "Could not find a ChannelUser for '%s'", sender ) );
	}

}
