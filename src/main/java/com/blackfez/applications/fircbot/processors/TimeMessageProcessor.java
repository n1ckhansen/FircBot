package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;

public class TimeMessageProcessor extends MessageProcessor {
	
	public TimeMessageProcessor( FircBot bot, String channel  ) {
		super( bot, channel );
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().equals( "time" ) ) {
			Bot.sendMessage( Channel, sender + ": The time is now " + new java.util.Date().toString() );
		}

	}

}
