package com.blackfez.applications.fircbot.crontasks;

import java.util.TimerTask;

import com.blackfez.applications.fircbot.FircBot;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public abstract class CronTask extends TimerTask {

	protected FircBot Bot;
	protected Long INTERVAL;
	protected ConfigurationManager cm;

	protected CronTask( ConfigurationManager configManager ) {
		super();
		cm = configManager;
	}
	
	public void setBot( FircBot bot ) {
		this.Bot = bot;
	}
	
	public abstract Long getInterval();
	
}
