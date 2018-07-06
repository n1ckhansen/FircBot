package com.blackfez.applications.fircbot.crontasks;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.applications.fircbot.utilities.RssBank;
import com.blackfez.models.rss.Entry;

public class RssCronTask extends CronTask {
	
	private final RssBank rssbank;
	
	public RssCronTask( ConfigurationManager configManager ) {
		super( configManager );
		rssbank = new RssBank( cm );
		INTERVAL = 900000L;
	}

	@Override
	public Long getInterval() {
		return INTERVAL;
	}

	@Override
	public void run() {
		System.out.println( "Running RssCron" );
		Set<Entry> newEntries;
		for( String url : rssbank.getFeedUrls() ) {
			System.out.println( "Processing " + url );
			rssbank.refreshFeed( url );
			newEntries = rssbank.parseForNewEntries( url );
			System.out.println( "We have " + newEntries.size() + " new entries" );
			for( String channel : rssbank.getChannelsForFeed( url ) ) {
				for( Entry entry : newEntries ) {
					StringBuffer sb = new StringBuffer();
					sb.append( "New entry by " );
					sb.append( entry.getAuthor() );
					sb.append( " at " );
					sb.append( entry.getLink() );
					sb.append( ". " );
					sb.append( entry.getTitle() );
					Bot.sendMessage( channel, sb.toString() );
					try {
						TimeUnit.SECONDS.sleep( 15L );				

					} 
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
