package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;

import twitter4j.Status;

public class TwitterBank implements Serializable {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient static final String TWIT_BANK_MAP_KEY = "dataFiles/twitterBank/twitBankMap";
	private transient static final String CHAN_FOLLOWS_MAP_KEY = "dataFiles/twitterBank/twitBankChanMap";
	private transient ConfigurationManager cm;
	private Map<String,Map<Long,Status>> twitBank = new HashMap<String,Map<Long,Status>>();
	private Map<String,Set<String>> channelFollows = new HashMap<String,Set<String>>();
	
	@SuppressWarnings("unchecked")
	public TwitterBank( ConfigurationManager configManager ) {
		cm = configManager;
		try {
			File f = new File( cm.getString( TWIT_BANK_MAP_KEY, "twitBankMap.xml" ) );
			if( !f.exists() )
				twitBank = new HashMap<String,Map<Long,Status>>();
			else
				twitBank = (Map<String,Map<Long,Status>>)ObjectSerializerIO.LoadObject( cm.getString( TWIT_BANK_MAP_KEY ) );
			f = new File( cm.getString( CHAN_FOLLOWS_MAP_KEY, "twitBankChannelFollowsMap.xml" ) );
			if( !f.exists() ) 
				channelFollows = new HashMap<String,Set<String>>();
			else
				channelFollows = (Map<String,Set<String>>)ObjectSerializerIO.LoadObject( cm.getString( CHAN_FOLLOWS_MAP_KEY ) );
		} 
		catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serializeStuff();
	}
	
	public boolean addStatus( Status status ) {
		boolean isNew = false;
		if( !twitBank.containsKey( status.getUser().getScreenName() ) )
			twitBank.put( status.getUser().getScreenName(), new HashMap<Long,Status>() );
		if( !twitBank.get( status.getUser().getScreenName() ).containsKey( status.getId() ) ) {
			twitBank.get( status.getUser().getScreenName() ).put( status.getId(), status );
			isNew = true;
			serializeStuff();
		}
		return isNew;
	}
	
	public void addChannel( String channel ) {
		if( !channelFollows.containsKey( channel ) ) {
			channelFollows.put( channel, new HashSet<String>() );
			serializeStuff();
		}
	}
	
	public void addChannelTwitterFollow( String channel, String username ) {
		if( !channelFollows.containsKey( channel ) ) {
			addChannel( channel );
		}
		channelFollows.get( channel ).add( username );
		serializeStuff();
	}
	
	public boolean channelFollowsUser( String channel, String user ) {
		boolean isFollows = false;
		for( String u : getLurksForChannel( channel ) ) {
			if( u.equals( user ) ) {
				isFollows = true;
				break;
			}
		}
		return isFollows;
	}
	
	public Set<String> getChannels() {
		return channelFollows.keySet();
	}
	
	public Set<String> getLurks() {
		Set<String> lurks = new HashSet<String>();
		for( String channel : getChannels() ) {
			lurks.addAll( getLurksForChannel( channel ) );
		}
		return lurks;
	}
	
	public Set<String> getLurksForChannel( String channel ) {
		if( channelFollows.containsKey( channel ) )
			return channelFollows.get( channel );
		else
			return new HashSet<String>();
	}
	
	public void removeLurkForChannel( String channel, String username ) {
		if( channelFollows.containsKey( channel ) && channelFollows.get( channel ).contains( username ) ) {
			channelFollows.get( channel ).remove( username );
			serializeStuff();
		}
	}
	
	public void serializeStuff() {
		try {
			ObjectSerializerIO.WriteObject( cm.getString( TWIT_BANK_MAP_KEY ), twitBank );
			ObjectSerializerIO.WriteObject( cm.getString( CHAN_FOLLOWS_MAP_KEY ), channelFollows );
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing TwitterBank object" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}
}
