package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.blackfez.fezcore.utilities.IO.ObjectSerializerIO;
import com.blackfez.models.rss.Entry;
import com.blackfez.models.rss.Feed;
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
	private transient static final String ENTRIES_KEY = "dataFiles/RSSBank/entries";
	private transient static final String FEED_MAP_KEY = "dataFiles/RSSBank/urlFeedMap";
	private Map<Feed,Set<Entry>> entries;
	private Map<String,Feed> urlFeedMap;
	private transient ConfigurationManager cm;
	
	@SuppressWarnings("unchecked")
	public RssBank( ConfigurationManager configManager ) {
		cm = configManager;
		try {
			File f = new File( cm.getStringValue( ENTRIES_KEY, "rssBankEntries.xml" ) );
			if( !f.exists() ) 
				entries = new HashMap<Feed,Set<Entry>>();
			else 
				entries = (Map<Feed,Set<Entry>>)ObjectSerializerIO.LoadObject( cm.getStringValue( ENTRIES_KEY ) );
			f = new File( cm.getStringValue( FEED_MAP_KEY, "rssBankFeedMap.xml" ) );
			if( !f.exists() )
				urlFeedMap = new HashMap<String,Feed>();
			else
				urlFeedMap = (Map<String,Feed>)ObjectSerializerIO.LoadObject( cm.getStringValue( FEED_MAP_KEY ) );
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serializeStuff();
	}
	
	public void addChannelWatcherForFeed( String channel, String feedUrl ) {
		getSyndFeedFromUrl( feedUrl );
		urlFeedMap.get( feedUrl ).addChannelFollowing( channel );
		serializeUrlFeedMap();
	}
	
	public Set<String> getChannelsForFeed( String url ) {
		return urlFeedMap.get( url ).getChannelsFollowing();
	}
	
	public Map<Feed,Set<Entry>> getEntries() {
		return this.entries;
	}
	
	public Feed getFeedForUrl( String url ) {
		return urlFeedMap.get( url );
	}
	
	public Set<String> getFeedUrls() {
		System.out.println( "returning a set of " + urlFeedMap.keySet().size() );
		return urlFeedMap.keySet();
	}

	private SyndFeed getSyndFeedFromUrl( String url ) {
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build( new XmlReader( new URL( url ) ) );
		} 
		catch (IllegalArgumentException | FeedException | IOException e) {
			System.out.println( "Unable to load feed at " + url );
			e.printStackTrace();
			System.out.println( "Continuing...." );
		}
		if( !urlFeedMap.containsKey( url ) ) {
			urlFeedMap.put(url, new Feed( feed ) );
			serializeUrlFeedMap();
		}
		return feed;
	}
	
	public Map<String,Feed> getUrlFeedMap() {
		return this.urlFeedMap;
	}
	
	public Set<Entry> parseForNewEntries( String url ) {
		Set<Entry> newEntries = new HashSet<Entry>();
		Feed ourFeed = getFeedForUrl( url );
		SyndFeed sf = getSyndFeedFromUrl( url );
		if( null == entries.get( ourFeed ) )
			entries.put( ourFeed, new HashSet<Entry>() );
		for( SyndEntry sEntry : sf.getEntries() ) {
			Boolean isNewEntry = true;
			for( Entry entry : entries.get( ourFeed ) ) {
				if( sEntry.getUri().equals( entry.getUri() ) ) {
					isNewEntry = false;
					break;
				}
			}
			if( isNewEntry ) {
				Entry entry = new Entry( sEntry );
				entries.get( ourFeed ).add( entry );
				newEntries.add( entry );
				serializeEntries();
			}
		}
		return newEntries;
	}
	
	public void refreshFeed( String url ) {
		getSyndFeedFromUrl( url );
	}
	
	public void removeChannelWatcherForFeed( String channel, String feedUrl ) {
		urlFeedMap.get( feedUrl ).removeChannelFollowing( channel );
		serializeUrlFeedMap();
	}
	
	public void serializeEntries() {
		try {
			ObjectSerializerIO.WriteObject( cm.getStringValue(ENTRIES_KEY), entries );
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serializeStuff() {
		System.out.println( "Serializing feed entries" );
		serializeEntries();
		System.out.println( "Serializing url to feed map" );
		serializeUrlFeedMap();
	}

	public void serializeUrlFeedMap() {
		try {
			ObjectSerializerIO.WriteObject( cm.getStringValue(FEED_MAP_KEY), urlFeedMap );
		}
		catch( IOException e ) {
			System.out.println( "Error encountered when serializing RssBank object[s]" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}
	
	public void setEntries( Map<Feed,Set<Entry>> map ) {
		this.entries = map;
	}
	
	public void setUrlFeedMap( Map<String,Feed> map ) {
		this.urlFeedMap = map;
	}

}
