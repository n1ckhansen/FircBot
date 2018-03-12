package com.blackfez.applications.fircbot.processors;

import java.util.ArrayList;
import java.util.List;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.TwitterBank;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TimelinesResources;

public class TwitterLinkMessageProcessor extends MessageProcessor {
	
	public TwitterLinkMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
	}
	
	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "http://twitter.com") || message.toLowerCase().startsWith( "https://twitter.com") ) {
			//Bot.sendMessage( Channel, sender + ": Working on the twit summary command" );
		}
		else if( message.toLowerCase().startsWith( "twitlurking" ) ) {
			TwitterBank twitBank = TwitterBank.getInstance();
			StringBuffer sb = new StringBuffer();
			sb.append( "In " );
			sb.append( Channel );
			sb.append( " I'm Twitter lurking" );
			if( !(twitBank.getLurksForChannel( Channel ).size()  > 0 ) ) 
				sb.append( " no one" );
			else {
				for( String u : twitBank.getLurksForChannel( Channel ) ) {
					sb.append( " @" + u );
				}
			}
			Bot.sendMessage( Channel, sb.toString() );
		}
		else if( message.toLowerCase().startsWith( "twitlurk" ) ) {
			if( message.split( " " ).length !=2  )
				Bot.sendMessage( Channel, sender + ": try the command like this 'twitlurk USERNAME' and no extraneous spaces or characters" );
			else {
				TwitterBank twitbank = TwitterBank.getInstance();
				// TODO should probably do some better validation of input here
				twitbank.addChannelTwitterFollow( Channel, message.split( " " )[ 1 ] );
			}
		}
		else if( message.toLowerCase().startsWith( "twitunlurk" ) ) {
			if( message.split( " " ).length !=2  )
				Bot.sendMessage( Channel, sender + ": try the command like this 'twitunlurk USERNAME' and no extraneous spaces or characters" );
			else {
				TwitterBank twitbank = TwitterBank.getInstance();
				// TODO should probably do some better validation of input here
				twitbank.removeLurkForChannel( Channel, message.split( " " )[ 1 ] );
			}
		}
	}
}
