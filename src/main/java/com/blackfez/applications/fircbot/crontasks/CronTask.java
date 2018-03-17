package com.blackfez.applications.fircbot.crontasks;

import java.util.TimerTask;

import com.blackfez.applications.fircbot.FircBot;

public abstract class CronTask extends TimerTask {

	protected FircBot Bot;
	protected Long INTERVAL;

	protected CronTask() {
		super();
	}
	
	public void setBot( FircBot bot ) {
		this.Bot = bot;
	}
	
	public abstract Long getInterval();
	
}
