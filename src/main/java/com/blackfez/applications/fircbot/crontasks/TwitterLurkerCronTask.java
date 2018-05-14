package com.blackfez.applications.fircbot.crontasks;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterLurkerCronTask extends CronTask {
	
	private transient Twitter twit;
	private transient final String TWIT_DEBUG_KEY = "twitter4j/debug";
	private transient final String TWIT_OAUTH_CKEY_KEY = "twitter4j/oauth/consumerKey";
	private transient final String TWIT_OAUTH_CSEC_KEY = "twitter4j/oauth/consumerSecret";
	private transient final String TWIT_OAUTH_ASSTOKEN_KEY = "twitter4j/oauth/accessToken";
	private transient final String TWIT_OAUTH_ASSTOKEN_SEC_KEY = "twitter4j/oauth/accessTokenSecret";
	
	private transient final Boolean TWIT_DEBUG_DEFAULT = true;
	private transient final String TWIT_OACK_DEFAULT = "";
	private transient final String TWIT_OACS_DEFAULT = "";
	private transient final String TWIT_OAAT_DEFAULT = "";
	private transient final String TWIT_OAAS_DEFAULT = "";
	

	public TwitterLurkerCronTask( ConfigurationManager configManager ) {
		super( configManager );
		INTERVAL = 300000L;
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled( cm.getBooleanValue( TWIT_DEBUG_KEY, TWIT_DEBUG_DEFAULT ) )
		  .setOAuthConsumerKey( cm.getStringValue( TWIT_OAUTH_CKEY_KEY, TWIT_OACK_DEFAULT ) )
		  .setOAuthConsumerSecret( cm.getStringValue( TWIT_OAUTH_CSEC_KEY, TWIT_OACS_DEFAULT ) )
		  .setOAuthAccessToken( cm.getStringValue( TWIT_OAUTH_ASSTOKEN_KEY, TWIT_OAAT_DEFAULT ) )
		  .setOAuthAccessTokenSecret( cm.getStringValue( TWIT_OAUTH_ASSTOKEN_SEC_KEY, TWIT_OAAS_DEFAULT ) );
		TwitterFactory tf = new TwitterFactory( cb.build() );
		twit = tf.getInstance();
	}
	
	@Override
	public Long getInterval() {
		return INTERVAL;
	}

	@Override
	public void run() {
		System.out.println( "Running TwitLurker" );
		ResponseList<Status> twitlist = null;
		for( String user : cm.getTwitterBank().getLurks() ) {
			try {
				twitlist = twit.getUserTimeline( user );
			} 
			catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			for( Status status : twitlist ) {
				if( cm.getTwitterBank().addStatus( status ) ) 
					for( String ch : cm.getTwitterBank().getChannels() ) {
						if( cm.getTwitterBank().channelFollowsUser( ch, status.getUser().getScreenName() ) ) 
							Bot.sendMessage( ch, "@" + status.getUser().getScreenName() + " tweets: " + status.getText() );
				}
			}
		}
	}
}
