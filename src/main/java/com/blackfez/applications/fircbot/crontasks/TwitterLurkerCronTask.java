package com.blackfez.applications.fircbot.crontasks;

import com.blackfez.applications.fircbot.utilities.TwitterBank;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterLurkerCronTask extends CronTask {
	
	private static final TwitterBank twitbank = TwitterBank.getInstance();
	private static TwitterLurkerCronTask INSTANCE;
	

	private TwitterLurkerCronTask() {
		super();
		INTERVAL = 60000L;
	}
	
	public static final TwitterLurkerCronTask getInstance() {
		if( null != INSTANCE ) 
			return INSTANCE;
		synchronized( TwitterLurkerCronTask.class ) {
			if( null == INSTANCE ) {
				INSTANCE = new TwitterLurkerCronTask();
			}
		}
		return INSTANCE;
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
		for( String user : twitbank.getLurks() ) {
			try {
				twitlist = twit.getUserTimeline( user );
			} 
			catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			for( Status status : twitlist ) {
				if( twitbank.addStatus( status ) ) 
					for( String ch : twitbank.getChannels() ) {
						if( twitbank.channelFollowsUser( ch, status.getUser().getScreenName() ) ) 
							Bot.sendMessage( ch, "@" + status.getUser().getScreenName() + " tweets: " + status.getText() );
					}
			}
		}
	}
}
