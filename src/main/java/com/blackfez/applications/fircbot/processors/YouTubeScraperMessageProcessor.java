package com.blackfez.applications.fircbot.processors;

import java.io.IOException;

import com.blackfez.apis.youtube.YouTubeApiWrapper;
import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public class YouTubeScraperMessageProcessor extends MessageProcessor {

	public YouTubeScraperMessageProcessor(FircBot bot, String channel, ConfigurationManager configManager) {
		super(bot, channel, configManager);
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( 
				message.toLowerCase().startsWith( "http://youtube.com") || 
				message.toLowerCase().startsWith( "http://www.youtube.com" ) ||
				message.toLowerCase().startsWith( "https://youtube.com") || 
				message.toLowerCase().startsWith( "https://www.youtube.com" )
				) {
			Integer wart = message.indexOf( "watch?v=" );
			String videoId = message.substring( wart + 8 , wart + 19 );
			try {
				Bot.sendMessage( Channel, sender + ": " + YouTubeApiWrapper.getYouTubeInfoForId( videoId ) );
			} 
			catch (IOException e) {
				System.out.println( "Issue encountered trying to scrape info for YouTube video: " + message + ".  Thought videoId was " + videoId );
				e.printStackTrace();
			}
		}
	}
}
