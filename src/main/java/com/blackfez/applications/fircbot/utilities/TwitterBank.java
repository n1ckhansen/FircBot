package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import twitter4j.Status;
import twitter4j.User;

public class TwitterBank implements Serializable {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	public transient static final String TWITBANK_FILENAME = "twitbank.ser";
	private transient static TwitterBank INSTANCE = null;
	
	private Map<User,Map<Long,Status>> twitBank = new HashMap<User,Map<Long,Status>>();
	private Map<String,Set<String>> channelFollows = new HashMap<String,Set<String>>();
	
	private TwitterBank() {}
	
	public static TwitterBank getInstance() {
		if( null != INSTANCE )
			return INSTANCE;
		synchronized( TwitterBank.class ) {
			File f = new File( TWITBANK_FILENAME );
			if( !f.exists() )
				INSTANCE = new TwitterBank();
			else {
				try {
					INSTANCE = (TwitterBank) IOUtils.LoadObject( TWITBANK_FILENAME );
				} 
				catch (ClassNotFoundException | IOException e) {
					INSTANCE = new TwitterBank();
				}
			}
		}
		return INSTANCE;
	}
	
	public boolean addStatus( Status status ) {
		boolean isNew = false;
		if( !twitBank.containsKey( status.getUser() ) )
			twitBank.put( status.getUser(), new HashMap<Long,Status>() );
		if( !twitBank.get( status.getUser() ).containsKey( status.getId() ) ) {
			twitBank.get( status.getUser() ).put( status.getId(), status );
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
			IOUtils.WriteObject( TWITBANK_FILENAME, this );
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing TwitterBank object" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}
}
