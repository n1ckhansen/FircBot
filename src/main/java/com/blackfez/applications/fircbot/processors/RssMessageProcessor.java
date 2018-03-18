package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;

public class RssMessageProcessor extends MessageProcessor {

	public RssMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "rssfollowing" ) ) {
		}

	}

}
