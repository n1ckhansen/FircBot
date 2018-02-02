package com.blackfez.applications.fircbot;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

public class FircBot extends PircBot {
	
	public FircBot() {
		this.setName( "FircBotWork" );
	}
	
	public void onMessage( String channel, String sender, String login, String hostname, String message ) {
		if( message.equalsIgnoreCase( "time") ) {
			String time = new java.util.Date().toString();
			sendMessage( channel, sender + ": The time is now " + time );
		}
		else if( message.equalsIgnoreCase( "wf" ) ) {
			sendMessage( channel, sender + ": Working on implementing the weather forecast command" );
		}
		else if( message.equalsIgnoreCase( "wx" ) ) {
			sendMessage( channel, sender + ": Working on implementing the current weather observation command" );
		}
		else if( message.toLowerCase().startsWith( "http://twitter.com") || message.toLowerCase().startsWith( "https://twitter.com") ) {
			sendMessage( channel, sender + ": Working on the twit summary command" );
		}
		else if( message.toLowerCase().startsWith( "http://youtube.com") || message.toLowerCase().startsWith( "https://youtube.com") ) {
			sendMessage( channel, sender + ": Working on the YouTube summary command" );
		}
		
		else if( message.toLowerCase().contains("fuck" ) ) {
			sendMessage( channel, sender + ": That's not nice.  Go eat soap." );
			sendMessage( channel, "Shit, man, stop it." );
		}
	}
	
	public void onJoin( String channel, String sender, String login, String hostname ) {
		if( !sender.equalsIgnoreCase( "fircbot" ) ) {
			sendMessage( channel, sender + ": s'up beyotch?!" );
		}
		else {
			sendMessage( channel, "I'm baaaack!" );
		}
	}

}
