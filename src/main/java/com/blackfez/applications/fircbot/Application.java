package com.blackfez.applications.fircbot;

import java.io.IOException;

import org.jibble.pircbot.*;

public class Application {

	public static void main(String[] args) throws Exception {
		FircBot bot = new FircBot();
		
		bot.setVerbose( true );
		
		try {
			bot.connect( "irc.freenode.net" );
		} 
		// TODO: break these out and handle more gracefully 
		catch (IOException | IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		bot.joinChannel( "#fezchat" );
		
	}

}
