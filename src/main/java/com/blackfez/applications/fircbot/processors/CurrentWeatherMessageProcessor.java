package com.blackfez.applications.fircbot.processors;

import java.rmi.NoSuchObjectException;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.models.user.ChannelUser;
import com.blackfez.models.user.UserUtils;
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
			IChannelUser cu;
			try {
				cu = UserUtils.UpdateUserLocation(sender, message, Bot.getChanUsers() );
				if( cu.getLocation().isZipSet() ) {
					Bot.sendMessage( Channel, dsw.retrieveCurrentWeatherForZip( cu.getLocation().getZip() ) );
				}
				else
					Bot.sendMessage( Channel, sender + ": try it with a zip code--wx 00000" );
			}
			catch( NoSuchObjectException e ) {
				e.printStackTrace();
				System.out.println( "Aborting 'wx' command" );
			}
		}
	}

}
