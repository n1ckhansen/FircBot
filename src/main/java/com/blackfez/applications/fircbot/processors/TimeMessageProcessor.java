package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public class TimeMessageProcessor extends MessageProcessor {
	
	public TimeMessageProcessor( FircBot bot, String channel, ConfigurationManager configManager ) {
		super( bot, channel, configManager );
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().equals( "time" ) ) {
			Bot.sendMessage( Channel, sender + ": The time is now " + new java.util.Date().toString() );
		}

	}

}
