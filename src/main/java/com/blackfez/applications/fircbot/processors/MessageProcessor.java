package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;

public abstract class MessageProcessor {
	
	protected String Channel;
	protected FircBot Bot;
	
	public MessageProcessor( FircBot bot, String channel ) {
		this.Bot = bot;
		this.Channel = channel;
	}
	
	public abstract void processMessage( String sender, String login, String hostname, String message ); 

}
