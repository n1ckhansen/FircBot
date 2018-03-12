package com.blackfez.applications.fircbot.crontasks;

import java.util.TimerTask;

import com.blackfez.applications.fircbot.FircBot;

public abstract class CronTask extends TimerTask {
	
	protected String Channel;
	protected FircBot Bot;
	protected Long INTERVAL;

	protected CronTask( String channel, FircBot bot ) {
		super();
		this.Channel = channel;
		this.Bot = bot;
	}
	
	public abstract Long getInterval();
	
}
