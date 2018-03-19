package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RssBank implements Serializable {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient static final String RSSBANK_FILE = "rssbank.ser";
	private transient static RssBank INSTANCE = null;
	private Map<URL,Set<String>> channelSubs;
	private Map<URL,Set<SyndEntry>> entries;
	private Map<URL,SyndFeed> urlFeedMap;
	
	private RssBank() {
		if( null == urlFeedMap )
			urlFeedMap = new HashMap<URL,SyndFeed>();
		if( null == entries )
			entries = new HashMap<URL,Set<SyndEntry>>();
		if( null == channelSubs )
			channelSubs = new HashMap<URL,Set<String>>();
		this.serializeStuff();
	}
	
	public static RssBank getInstance() {
		if( null != INSTANCE )
			return INSTANCE;
		synchronized( RssBank.class ) {
			File f = new File( RSSBANK_FILE );
			if( !f.exists() ) {
				INSTANCE = new RssBank();
			}
			else {
				try {
					INSTANCE = (RssBank) IOUtils.LoadObject( RSSBANK_FILE );
				} 
				catch (ClassNotFoundException | IOException e) {
					INSTANCE = new RssBank();
				}
			}
		}
		return INSTANCE;
	}
	
	private SyndFeed getFeedFromUrl( URL url ) {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build( new XmlReader( url ) );
		} 
		catch (IllegalArgumentException | FeedException | IOException e) {
			System.out.println( "Unable to load feed at " + url.toString() );
			e.printStackTrace();
			System.out.println( "Continuing...." );
		}
		urlFeedMap.put(url, feed);
		return feed;
	}
	
	public Set<String> getChannelSubsForUrl( URL url ) {
		return channelSubs.get( url );
	}
	
	public SyndFeed getFeedForUrl( URL url ) {
		return urlFeedMap.get( url );
	}
	
	public void addChannelWatcherForFeed( String channel, String feedUrl ) {
		URL url = null;
		try {
			url = new URL( feedUrl );
		} 
		catch (MalformedURLException e) {
			System.out.println( "Unable to convert '" + feedUrl + "' to URL object" );
			e.printStackTrace();
			return;
		}
		if( !channelSubs.containsKey( url ) )
			channelSubs.put( url, new HashSet<String>() );
		channelSubs.get( url ).add( channel );
		urlFeedMap.put( url, getFeedFromUrl( url ) );
		serializeStuff();
	}
	
	public void refreshFeed( URL url ) {
		getFeedFromUrl( url );
	}
	
	public void removeChannelWatcherForFeed( String channel, String feedUrl ) {
		URL url = null;
		try {
			url = new URL( feedUrl );
		}
		catch( MalformedURLException e ) {
			System.out.println( "Unable to convert '" + feedUrl + "' to URL object" );
			e.printStackTrace();
			return;
		}
		if( !channelSubs.containsKey( url ) )
			return;
		if( channelSubs.get( url ).contains( channel ) )
			channelSubs.get ( url ).remove( channel );
		serializeStuff();
	}
	
	
	public void serializeStuff() {
		try {
			IOUtils.WriteObject( RSSBANK_FILE, this );
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing RssBank object" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}

	public Set<URL> getFeedUrls() {
		return urlFeedMap.keySet();
	}

	public Set<SyndEntry> parseForNewEntries(URL url) {
		Set<SyndEntry> newEntries = new HashSet<SyndEntry>();
		if( null == entries.get( url ) )
			entries.put( url, new HashSet<SyndEntry>() );
		for( SyndEntry entry : urlFeedMap.get( url ).getEntries() ) {
			if( entries.get( url ).contains( entry ) )
				continue;
			newEntries.add( entry );
			entries.get( url ).add( entry );
		}
		serializeStuff();
		return newEntries;
	}

}
