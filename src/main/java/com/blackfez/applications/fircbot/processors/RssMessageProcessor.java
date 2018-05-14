package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.applications.fircbot.utilities.RssBank;

public class RssMessageProcessor extends MessageProcessor {
	
	private RssBank rssbank;

	public RssMessageProcessor(FircBot bot, String channel, ConfigurationManager configManager ) {
		super(bot, channel, configManager);
		rssbank = new RssBank( cm );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "rssfollowing" ) ) {
			Bot.sendMessage( Channel, "In " + Channel + " I'm following: " );
			boolean follows = false;
			for( String feed : rssbank.getFeedUrls() ) {
				if( rssbank.getChannelsForFeed( feed ).contains( Channel ) ) {
					Bot.sendMessage( Channel, "\t* " + rssbank.getFeedForUrl( feed ).getTitle() + " (" + feed + ")" );
					follows = true;
				}
			}
			if( !follows ) {
				Bot.sendMessage( Channel, "No RSS feeds being followed" );			
			}
		}

		else if( message.toLowerCase().startsWith( "rssfollow" ) ) {
			System.out.println( "processing rssfollow command " + message );
			String[] tokens = message.split (" " );
			System.out.println( "message tokens are " + tokens );
			if( tokens.length != 2 ) {
				Bot.sendMessage( Channel, sender + ": try something like 'rssfollow URL'" );
				return;
			}
			String u = tokens[ 1 ];
			System.out.println( "adding " + tokens[ 1 ] + " to channel " + Channel );
			rssbank.addChannelWatcherForFeed( Channel, tokens[ 1 ] );
			System.out.println( "There are now " + rssbank.getUrlFeedMap().keySet().size() + " keys in the url feed map." );
			Bot.sendMessage( Channel, "Now following " + rssbank.getFeedForUrl( u ).getTitle() + " at " + tokens[ 1 ] );
		}
		else if( message.toLowerCase().startsWith( "rssunfollow" ) ) {
			String[] tokens = message.split (" " );
			if( tokens.length != 2 ) {
				Bot.sendMessage( Channel, sender + ": try something like 'rssunfollow URL'" );
				return;
			}
			String u = tokens[ 1 ];
			rssbank.removeChannelWatcherForFeed( Channel,  tokens[ 1 ] );
			Bot.sendMessage( Channel, "Unfollowed " + rssbank.getFeedForUrl( u ).getTitle() + " at " + tokens[ 1 ] );
		}
	}
}
