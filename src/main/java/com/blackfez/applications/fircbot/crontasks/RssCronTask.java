package com.blackfez.applications.fircbot.crontasks;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.blackfez.applications.fircbot.utilities.RssBank;
import com.rometools.rome.feed.synd.SyndEntry;

public class RssCronTask extends CronTask {
	
	private static RssCronTask INSTANCE;
	private static final RssBank rssbank = RssBank.getInstance();
	
	private RssCronTask() {
		super();
		INTERVAL = 900000L;
	}

	public static final RssCronTask getInstance() {
		if( null != INSTANCE )
			return INSTANCE;
		synchronized( RssCronTask.class ) {
			if( null == INSTANCE )
				INSTANCE = new RssCronTask();
		}
		return INSTANCE;
	}

	@Override
	public Long getInterval() {
		return INTERVAL;
	}

	@Override
	public void run() {
		System.out.println( "Running RssCron" );
		Set<SyndEntry> newEntries;
		for( URL url : rssbank.getFeedUrls() ) {
			rssbank.refreshFeed( url );
			newEntries = rssbank.parseForNewEntries( url );
			for( String channel : rssbank.getChannelSubsForUrl( url ) ) {
				for( SyndEntry entry : newEntries ) {
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
