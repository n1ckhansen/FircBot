package com.blackfez.applications.fircbot.processors;

import java.rmi.NoSuchObjectException;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.models.user.UserUtils;
import com.blackfez.models.user.interfaces.IChannelUser;

public class WeatherForecastMessageProcessor extends MessageProcessor {
	
	public WeatherForecastMessageProcessor(FircBot bot, String channel, ConfigurationManager configManager ) {
		super(bot, channel, configManager);
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.startsWith( "wf" ) ) {
			IChannelUser cu;
			try {
				cu = UserUtils.UpdateUserLocation(sender, message, Bot.getChanUsers() );
				if( cu.getLocation().isZipSet() ) {
					Bot.sendMessage( Channel, cm.getDskWrapper().retrieveWeatherForecastForZip( cu.getLocation().getZip() ) );
				}
				else
					Bot.sendMessage( Channel, sender + ": try it with a zip code--wf 00000" );
			} 
			catch (NoSuchObjectException e) {
				e.printStackTrace();
				System.out.println( "Aborting 'wf' command. " );
			}
		}
	}

}
