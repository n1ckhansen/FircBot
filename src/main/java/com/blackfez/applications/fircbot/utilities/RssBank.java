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

import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
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
	private transient static final String CHANNEL_SUBS_KEY = "dataFiles/RSSBank/channelSubs";
	private transient static final String ENTRIES_KEY = "dataFiles/RSSBank/entries";
	private transient static final String FEED_MAP_KEY = "dataFiles/RSSBank/urlFeedMap";
	private Map<URL,Set<String>> channelSubs;
	private Map<URL,Set<SyndEntry>> entries;
	private Map<URL,SyndFeed> urlFeedMap;
	private transient ConfigurationManager cm;
	
	@SuppressWarnings("unchecked")
	public RssBank( ConfigurationManager configManager ) {
		cm = configManager;
		try {
			File f = new File( cm.getString( CHANNEL_SUBS_KEY, "rssBankChannelSubs.xml" ) );
			if( !f.exists() ) 
				channelSubs = new HashMap<URL,Set<String>>();
			else 
				channelSubs = (Map<URL,Set<String>>)ObjectSerializerIO.LoadObject( cm.getString( CHANNEL_SUBS_KEY ) );
			f = new File( cm.getString( ENTRIES_KEY, "rssBankEntries.xml" ) );
			if( !f.exists() ) 
				entries = new HashMap<URL,Set<SyndEntry>>();
			else 
				entries = (Map<URL,Set<SyndEntry>>)ObjectSerializerIO.LoadObject( cm.getString( ENTRIES_KEY ) );
			f = new File( cm.getString( FEED_MAP_KEY, "rssBankFeedMap.xml" ) );
			if( !f.exists() )
				urlFeedMap = new HashMap<URL,SyndFeed>();
			else
				urlFeedMap = (Map<URL,SyndFeed>)ObjectSerializerIO.LoadObject( cm.getString( FEED_MAP_KEY ) );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serializeStuff();
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
	
	public Map<URL,Set<String>> getChannelSubs() {
		return channelSubs;
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
			ObjectSerializerIO.WriteObject( cm.getString(CHANNEL_SUBS_KEY), channelSubs );
			ObjectSerializerIO.WriteObject( cm.getString(ENTRIES_KEY), entries );
			ObjectSerializerIO.WriteObject( cm.getString(FEED_MAP_KEY), urlFeedMap );
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing RssBank object[s]" );
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
	
	public void setChannelSubs( Map<URL,Set<String>> subs ) {
		this.channelSubs = subs;
	}
	
	public Map<URL,Set<SyndEntry>> getEntries() {
		return this.entries;
	}
	
	public void setEntries( Map<URL,Set<SyndEntry>> map ) {
		this.entries = map;
	}
	
	public Map<URL,SyndFeed> getUrlFeedMap() {
		return this.urlFeedMap;
	}
	
	public void setUrlFeedMap( Map<URL,SyndFeed> map ) {
		this.urlFeedMap = map;
	}

}
