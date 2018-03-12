package com.blackfez.applications.fircbot.crontasks;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.TwitterBank;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterLurkerCronTask extends CronTask {
	
	private TwitterBank twitbank = TwitterBank.getInstance();
	

	public TwitterLurkerCronTask(String channel, FircBot bot) {
		super(channel, bot);
		INTERVAL = 60000L;
	}

	@Override
	public Long getInterval() {
		return INTERVAL;
	}

	@Override
	public void run() {
		System.out.println( "Running TwitLurker" );
		Twitter twit = TwitterFactory.getSingleton();
		ResponseList<Status> twitlist = null;
		for( String user : twitbank.getLurksForChannel( Channel ) ) {
			try {
				twitlist = twit.getUserTimeline( user );
			} 
			catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			for( Status status : twitlist ) {
				if( twitbank.addStatus( status ) )
					Bot.sendMessage( Channel, "@" + status.getUser().getScreenName() + " tweets: " + status.getText() );
			}
		}
	}
}
