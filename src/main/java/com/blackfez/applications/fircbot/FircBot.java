package com.blackfez.applications.fircbot;

import java.io.File;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.IOUtils;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.ChannelUser;
import com.blackfez.models.user.UserUtils;


public class FircBot extends PircBot {
	
	private static final String CHANUSERSFILE = "chanUsers.ser";
	private Map<String,ChannelUser> ChanUsers;
	private static final String BOTNAME = "FircBot";
	
	public FircBot() {
		this.setName( BOTNAME );
		try {
			this.initChanUsers();
		} 
		catch (ClassNotFoundException | IOException e) {
			System.out.println( "Fatal error encountered when deserializing ChannelUser cache" );
			e.printStackTrace();
			System.exit( 1 );
		}
		ZipCodeApiWrapper.getInstance();
		DarkSkyApiWrapper.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	private void initChanUsers() throws IOException, ClassNotFoundException {
		File f = new File( CHANUSERSFILE );
		if( ! f.exists() ) {
			this.ChanUsers = new HashMap<String,ChannelUser>();
			IOUtils.WriteObject( CHANUSERSFILE, this.ChanUsers );
		}
		this.ChanUsers = (Map<String,ChannelUser>) IOUtils.LoadObject( CHANUSERSFILE );
	}
	
	public void onMessage( String channel, String sender, String login, String hostname, String message ) {
		if( message.equalsIgnoreCase( "time") ) {
			String time = new java.util.Date().toString();
			sendMessage( channel, sender + ": The time is now " + time );
		}
		else if( message.equalsIgnoreCase( "wf" ) ) {
			ChannelUser cu;
			try {
				cu = UserUtils.UpdateUserLocation(sender, message, this.ChanUsers );
				if( cu.getLocation().isZipSet() ) {
					DarkSkyApiWrapper dsw = DarkSkyApiWrapper.getInstance();
					sendMessage( channel, dsw.retrieveWeatherForecastForZip( cu.getLocation().getZip() ) );
				}
				else
					sendMessage( channel, sender + ": try it with a zip code--wf 00000" );
			} 
			catch (NoSuchObjectException e) {
				e.printStackTrace();
				System.out.println( "Aborting 'wf' command. " );
			}
		}
		else if( message.startsWith( "wx" ) ) {
			ChannelUser cu;
			try {
				cu = UserUtils.UpdateUserLocation(sender, message, this.ChanUsers );
				if( cu.getLocation().isZipSet() ) {
					DarkSkyApiWrapper dsw = DarkSkyApiWrapper.getInstance();
					sendMessage( channel, dsw.retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
				}
				else
					sendMessage( channel, sender + ": try it with a zip code--wx 00000" );
			}
			catch( NoSuchObjectException e ) {
				e.printStackTrace();
				System.out.println( "Aborting 'wx' command" );
			}
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
			if( this.ChanUsers.containsKey( u.getNick() ) ) {
				this.ChanUsers.get( u.getNick() ).addChannel( channel );
				this.ChanUsers.get( u.getNick() ).setUser( u );
				if( !u.getNick().equals( this.getName() ) )
					sendMessage( channel, u.getNick() + ": I already know about you" );
			}
			else {
				this.ChanUsers.put( u.getNick(), new ChannelUser( u.getNick() ) );
				this.ChanUsers.get( u.getNick() ).addChannel( channel );
				this.ChanUsers.get( u.getNick() ).setUser( u );
				if( !u.getNick().equals( this.getName() ) && !u.getNick().equals( "ChanServ" ) )
					sendMessage( channel, u.getNick() + ": I'm tracking you" );
			}
		}
		try {
			this.serializeTheStuff();
		} catch (IOException e) {
			System.out.println( "unable to serialze the stuff in onUserList()");
			e.printStackTrace();
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
	public void serializeTheStuff() throws IOException {
		IOUtils.WriteObject(CHANUSERSFILE, this.ChanUsers );
		DarkSkyApiWrapper.getInstance().serializeTheStuff();
		ZipCodeApiWrapper.getInstance().serializeTheStuff();
	}

}
