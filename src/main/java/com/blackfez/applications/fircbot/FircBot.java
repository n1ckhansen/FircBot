package com.blackfez.applications.fircbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.ChannelUser;


public class FircBot extends PircBot {
	
	private static final String CHANUSERSFILE = "chanUsers.ser";
	private Map<String,ChannelUser> ChanUsers;
	
	public FircBot() {
		this.setName( "FircBot" );
		try {
			this.initChanUsers();
		} catch (ClassNotFoundException | IOException e) {
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
			FileOutputStream fos = new FileOutputStream( CHANUSERSFILE );
			ObjectOutputStream oos = new ObjectOutputStream( fos );
			oos.writeObject( this.ChanUsers );
			oos.close();
			fos.close();
		}
		FileInputStream fis = new FileInputStream( f );
		ObjectInputStream ois = new ObjectInputStream( fis );
		this.ChanUsers = (Map<String,ChannelUser>) ois.readObject();
		ois.close();
		fis.close();
	}
	
	public void onMessage( String channel, String sender, String login, String hostname, String message ) {
		if( message.equalsIgnoreCase( "time") ) {
			String time = new java.util.Date().toString();
			sendMessage( channel, sender + ": The time is now " + time );
		}
		else if( message.equalsIgnoreCase( "wf" ) ) {
			String[] tokens =  message.split( " " );
			if( this.ChanUsers.containsKey( sender ) ) {
				ChannelUser cu = this.ChanUsers.get( sender );
				if( tokens.length >= 2 ) {
					Location loc = new Location();
					loc.setZip( tokens[ 1 ] );
					cu.setLocation( loc );
				}
				if( cu.getLocation() == null )
					cu.setLocation( new Location() );
				if( cu.getLocation().isZipSet() ) {
					DarkSkyApiWrapper dsw = DarkSkyApiWrapper.getInstance();
					sendMessage( channel, dsw.retrieveWeatherForecastForZip( cu.getLocation().getZip() ) );
				}
				else
					sendMessage( channel, sender + ": try it with a zip code--wf 00000" );
			}
		}
		else if( message.startsWith( "wx" ) ) {
			String[] tokens = message.split( " " );
			if( this.ChanUsers.containsKey( sender ) ) {
				ChannelUser cu = this.ChanUsers.get( sender );
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
			//TODO: handle this better when you're sober
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
		File f = new File( CHANUSERSFILE );
		FileOutputStream fos = new FileOutputStream( f );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		oos.writeObject( this.ChanUsers );
		oos.close();
		fos.close();
		DarkSkyApiWrapper.getInstance().serializeTheStuff();
		ZipCodeApiWrapper.getInstance().serializeTheStuff();
	}

}
