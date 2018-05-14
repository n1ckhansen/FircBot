package com.blackfez.applications.fircbot.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;

public class CurrentWeatherMessageProcessor extends MessageProcessor {
	
	public CurrentWeatherMessageProcessor(FircBot bot, String channel, ConfigurationManager configManager ) {
		super(bot, channel, configManager);
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.startsWith( "wx" ) ) {
			IChannelUser cu = cm.getUserManager().getUserMap().get( sender );
			Pattern zipPattern = Pattern.compile( "(\\d{5})" );
			Matcher matcher = zipPattern.matcher( message );
			if( matcher.find() ) {
				Location loc = new Location();
				loc.setZip( matcher.group( 1 ) );	
				cm.getUserManager().setChannelUserLocation( sender, loc );
				Bot.sendMessage( Channel, cm.getDskWrapper().retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
			}
			else if( null != cu.getLocation() )
				Bot.sendMessage( Channel, cm.getDskWrapper().retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
			else
				Bot.sendMessage( Channel, sender + ": try it with a zip code--wx 00000" );
		}
	}
}
