package com.blackfez.applications.fircbot.processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.models.geolocation.Location;
import com.blackfez.models.user.interfaces.IChannelUser;

public class CurrentWeatherMessageProcessor extends MessageProcessor {
	
	private DarkSkyApiWrapper dsw;

	public CurrentWeatherMessageProcessor(FircBot bot, String channel, ConfigurationManager configManager, DarkSkyApiWrapper dskWrapper ) {
		super(bot, channel, configManager);
		dsw = dskWrapper;
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.startsWith( "wx" ) ) {
			System.out.println( "processing wx message");
			IChannelUser cu = cm.getUserManager().getUserMap().get( sender );
			Pattern zipPattern = Pattern.compile( "(\\d{5})" );
			Matcher matcher = zipPattern.matcher( message );
			if( matcher.find() ) {
				Location loc = new Location();
				loc.setZip( matcher.group( 1 ) );	
				System.out.println( loc.getZip() + " is loc's zip" );
				cm.getUserManager().setChannelUserLocation( sender, loc );
				Bot.sendMessage( Channel, dsw.retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
			}
			else if( null != cu.getLocation() )
				Bot.sendMessage( Channel, dsw.retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
			else
				Bot.sendMessage( Channel, sender + ": try it with a zip code--wx 00000" );
		}
	}
}
