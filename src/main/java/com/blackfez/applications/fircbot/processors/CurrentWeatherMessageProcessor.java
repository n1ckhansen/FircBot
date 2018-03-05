package com.blackfez.applications.fircbot.processors;

import java.rmi.NoSuchObjectException;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.models.user.ChannelUser;
import com.blackfez.models.user.UserUtils;

public class CurrentWeatherMessageProcessor extends MessageProcessor {

	public CurrentWeatherMessageProcessor(FircBot bot, String channel) {
		super(bot, channel);
	}

	@Override
	public void processMessage(String sender, String login, String hostname, String message) {
		if( message.startsWith( "wx" ) ) {
			ChannelUser cu;
			try {
				cu = UserUtils.UpdateUserLocation(sender, message, Bot.getChanUsers() );
				if( cu.getLocation().isZipSet() ) {
					DarkSkyApiWrapper dsw = DarkSkyApiWrapper.getInstance();
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
