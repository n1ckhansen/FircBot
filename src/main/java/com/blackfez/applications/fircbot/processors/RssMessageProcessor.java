package com.blackfez.applications.fircbot.processors;

import java.net.MalformedURLException;
import java.net.URL;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.RssBank;

public class RssMessageProcessor extends MessageProcessor {
	
	private transient static final String nl = System.getProperty( "line.separator" );
	private static final RssBank rssbank = RssBank.getInstance();

	public RssMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "rssfollowing" ) ) {
			StringBuffer msg = new StringBuffer();
			Bot.sendMessage( Channel, "In " + Channel + " I'm following: " );
			boolean follows = false;
			for( URL feed : rssbank.getFeedUrls() ) {
				if( rssbank.getChannelSubsForUrl( feed ).contains( Channel ) ) {
					Bot.sendMessage( Channel, "\t* " + rssbank.getFeedForUrl( feed ).getTitle() + " (" + feed + ")" );
					follows = true;
				}
			}
			if( !follows ) {
				Bot.sendMessage( Channel, "No RSS feeds being followed" );			}
		}
		else if( message.toLowerCase().startsWith( "rssfollow" ) ) {
			String[] tokens = message.split (" " );
			if( tokens.length != 2 ) {
				Bot.sendMessage( Channel, sender + ": try something like 'rssfollow URL'" );
				return;
			}
			URL u = null;
			try {
				u = new URL( tokens[ 1 ] );
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
				Bot.sendMessage( Channel, sender + ": Could not parse '" + tokens[ 1 ] + "' as a URL" );
				return;
			}
			rssbank.addChannelWatcherForFeed( Channel, tokens[ 1 ] );
			Bot.sendMessage( Channel, "Now following " + rssbank.getFeedForUrl( u ).getTitle() + " at " + tokens[ 1 ] );
		}
		else if( message.toLowerCase().startsWith( "rssunfollow" ) ) {
			String[] tokens = message.split (" " );
			if( tokens.length != 2 ) {
				Bot.sendMessage( Channel, sender + ": try something like 'rssunfollow URL'" );
				return;
			}
			URL u = null;
			try {
				u = new URL( tokens[ 1 ] );
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
				Bot.sendMessage( Channel, sender + ": Could not parse '" + tokens[ 1 ] + "' as a URL" );
				return;
			}
			rssbank.removeChannelWatcherForFeed( Channel,  tokens[ 1 ] );
			Bot.sendMessage( Channel, "Unfollowed " + rssbank.getFeedForUrl( u ).getTitle() + " at " + tokens[ 1 ] );
		}
	}
}
