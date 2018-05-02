package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public abstract class MessageProcessor {
	
	protected String Channel;
	protected FircBot Bot;
	protected ConfigurationManager cm;
	
	public MessageProcessor( FircBot bot, String channel, ConfigurationManager configManager ) {
		this.Bot = bot;
		this.Channel = channel;
		this.cm = configManager;
	}
	
	public abstract void processMessage( String sender, String login, String hostname, String message ); 

}
