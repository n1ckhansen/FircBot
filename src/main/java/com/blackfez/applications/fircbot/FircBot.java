package com.blackfez.applications.fircbot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.ChannelUser;


public class FircBot extends PircBot {
	
	private Map<User,ChannelUser> ChanUsers;
	
	public FircBot() {
		this.setName( "FircBot" );
		this.ChanUsers = new HashMap<User,ChannelUser>();
	}
	
	private User getUserByNic( String nic ) {
		for( User u : this.ChanUsers.keySet() ) {
			if( u.getNick().equals( nic ) ) {
				return u;
				
			}
		}
		return null;
	}
	
	public void onMessage( String channel, String sender, String login, String hostname, String message ) {
		if( message.equalsIgnoreCase( "time") ) {
			String time = new java.util.Date().toString();
			sendMessage( channel, sender + ": The time is now " + time );
		}
		else if( message.equalsIgnoreCase( "wf" ) ) {
			sendMessage( channel, sender + ": Working on implementing the weather forecast command" );
		}
		else if( message.startsWith( "wx" ) ) {
			String[] tokens = message.split( " " );
			if( this.ChanUsers.containsKey( this.getUserByNic( sender ) ) ) {
				User u = this.getUserByNic( sender );
				ChannelUser cu = this.ChanUsers.get( u );
				if( tokens.length >= 2 ) {
					Location loc = new Location();
					loc.setZip( tokens[ 1 ] );
					cu.setLocation( loc );
				}
				if( cu.getLocation() == null ) 
					cu.setLocation( new Location() );
				if( cu.getLocation().isZipSet() ) {
					DarkSkyApiWrapper dsw = DarkSkyApiWrapper.getInstance();
					sendMessage( channel, dsw.retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
				}
				else {
					sendMessage( channel, sender + ": try it with a zip code--wx 00000" );
				}
			}
			else
				sendMessage( channel, sender + ": Who TF are you?" );
		}
		else if( message.toLowerCase().startsWith( "http://twitter.com") || message.toLowerCase().startsWith( "https://twitter.com") ) {
			sendMessage( channel, sender + ": Working on the twit summary command" );
		}
		else if( message.toLowerCase().startsWith( "http://youtube.com") || message.toLowerCase().startsWith( "https://youtube.com") ) {
			sendMessage( channel, sender + ": Working on the YouTube summary command" );
		}
		
		else if( message.toLowerCase().contains("fuck" ) ) {
			sendMessage( channel, sender + ": That's not nice.  Go eat soap." );
			sendMessage( channel, "Shit, man, stop it." );
		}
	}
	
	public void onUserList( String channel, User[] users ) {
		for( User u : users ) {
			if( this.ChanUsers.containsKey( u ) ) {
				this.ChanUsers.get( u ).addChannel( channel );
				if( !u.getNick().equals( this.getName() ) )
					sendMessage( channel, u.getNick() + ": I already know about you" );
			}
			else {
				this.ChanUsers.put( u, new ChannelUser( u ) );
				this.ChanUsers.get( u ).addChannel( channel );
				if( !u.getNick().equals( this.getName() ) && !u.getNick().equals( "ChanServ" ) )
					sendMessage( channel, u.getNick() + ": I'm tracking you" );
			}
		}
	}
	
	public void onJoin( String channel, String sender, String login, String hostname ) {
		if( channel.replaceAll( "#","" ).toLowerCase() == "fezchat" ) {
			if( sender.toLowerCase().equals( "fezboy" ) )
				sendMessage( channel, sender + ": Welcome back, sir." );
			else if( !sender.equalsIgnoreCase( "fircbot" ) ) {
				sendMessage( channel, sender + ": s'up beyotch?!" );
				boolean fezout = true;
				for( User u : getUsers( channel ) ) {
					if( u.getNick().toLowerCase().equals( "fezboy" ) ) {
						fezout = false;
						break;
					}
				}
				if( fezout ) {
					sendMessage( channel, sender + ": fezboy is out. I'm logging so he'll see you were here." );
				}
			}
			else {
				sendMessage( channel, "I'm baaaack!" );
			}

		}
	}

}
