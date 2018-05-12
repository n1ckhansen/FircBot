package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
import com.blackfez.models.twitter.Tweet;

import twitter4j.Status;

public class TwitterBank implements Serializable {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient static final String TWIT_BANK_MAP_KEY = "dataFiles/twitterBank/twitBankMap";
	private transient static final String CHAN_FOLLOWS_MAP_KEY = "dataFiles/twitterBank/twitBankChanMap";
	private transient ConfigurationManager cm;
	private Map<String,Set<Tweet>> twitBank = new HashMap<String,Set<Tweet>>();
	private Map<String,Set<String>> channelFollows = new HashMap<String,Set<String>>();
	
	@SuppressWarnings("unchecked")
	public TwitterBank( ConfigurationManager configManager ) {
		cm = configManager;
		try {
			File f = new File( cm.getStringValue( TWIT_BANK_MAP_KEY, "twitBankMap.xml" ) );
			if( !f.exists() )
				twitBank = new HashMap<String,Set<Tweet>>();
			else
				twitBank = (Map<String,Set<Tweet>>)ObjectSerializerIO.LoadObject( cm.getStringValue( TWIT_BANK_MAP_KEY ) );
			f = new File( cm.getStringValue( CHAN_FOLLOWS_MAP_KEY, "twitBankChannelFollowsMap.xml" ) );
			if( !f.exists() ) 
				channelFollows = new HashMap<String,Set<String>>();
			else
				channelFollows = (Map<String,Set<String>>)ObjectSerializerIO.LoadObject( cm.getStringValue( CHAN_FOLLOWS_MAP_KEY ) );
		} 
		catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serializeStuff();
	}
	
	public boolean addStatus( Status status ) {
		Boolean addTweet = true;
		Tweet tweet = new Tweet( status );
		if( !twitBank.containsKey( tweet.getUserScreenName() ) ) {
			System.out.println( "No key for " + tweet.getUserScreenName() );
			twitBank.put( tweet.getUserScreenName(), new HashSet<Tweet>() );
			System.out.println( "Key added for " + tweet.getUserScreenName() );
		}
		else {
			for ( Tweet stored : twitBank.get( tweet.getUserScreenName() ) ) {
				if( String.valueOf( stored.getId() ).equals( String.valueOf( tweet.getId() ) ) ) {
					System.out.println( "found id " + tweet.getId());
					addTweet = false;
					break;
				}
			}
		}
		if( addTweet ) {
			twitBank.get( tweet.getUserScreenName() ).add( tweet );
			serializeStuff();
		}
		return addTweet;
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
			ObjectSerializerIO.WriteObject( cm.getStringValue( TWIT_BANK_MAP_KEY ), twitBank );
			System.out.println( "Serializer serialized twitbank");
			ObjectSerializerIO.WriteObject( cm.getStringValue( CHAN_FOLLOWS_MAP_KEY ), channelFollows );
			System.out.println( "Serializer serialized chanfollows");
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing TwitterBank object" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}
}
