package com.blackfez.models.rss;

import java.util.HashSet;
import java.util.Set;

import com.rometools.rome.feed.synd.SyndFeed;

public class Feed {
	
	private String description;
	private String title;
	private Set<String> channelsFollowing;


	public Feed() {
		// TODO Auto-generated constructor stub
	}
	
	public Feed( SyndFeed syndFeed ) {
		setDescription( null == syndFeed.getDescription() ? "" : syndFeed.getDescription() );
		setTitle( null == syndFeed.getTitle() ? "" : syndFeed.getTitle() );
		setChannelsFollowing( new HashSet<String>() );
	}
	
	public void addChannelFollowing( String channel ) {
		channelsFollowing.add( channel );
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Set<String> getChannelsFollowing() {
		return channelsFollowing;
	}

	public void removeChannelFollowing( String channel ) {
		channelsFollowing.remove( channel );
	}

	public void setChannelsFollowing(Set<String> channelsFollowing) {
		this.channelsFollowing = channelsFollowing;
	}


}
