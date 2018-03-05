package com.blackfez.applications.fircbot.processors;

import com.blackfez.applications.fircbot.FircBot;

public class TwitterLinkMessageProcessor extends MessageProcessor {

	public TwitterLinkMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.toLowerCase().startsWith( "http://twitter.com") || message.toLowerCase().startsWith( "https://twitter.com") ) {
			Bot.sendMessage( Channel, sender + ": Working on the twit summary command" );
		}

	}

}
