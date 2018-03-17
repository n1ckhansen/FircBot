package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.TwitterBank;

public class TwitterLinkMessageProcessor extends MessageProcessor {
	
	public TwitterLinkMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
	}
	
	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "http://twitter.com") || message.toLowerCase().startsWith( "https://twitter.com") ) {
			// TODO Bored with this presently.  I've decided I like twitlurking better.  We'll cut a new feature branch and circle back.
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
