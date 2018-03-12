package com.blackfez.applications.fircbot;

import java.io.IOException;

import org.jibble.pircbot.*;

public class Application {

	public static void main(String[] args) throws Exception {
		FircBot bot = new FircBot();
		Runtime.getRuntime().addShutdownHook(
				new Thread() {
					public void run() {
						System.out.println( "Shudown Hook is running" );
						try {
							bot.serializeTheStuff();
						} catch (IOException e) {
							System.out.println( "Unable to serialize all of the things on exit.  Boo." );
							e.printStackTrace();
						}
						finally  {
							// go quietly into the night
						}
					}
				}
		);
		bot.setVerbose( true );
		try {
			bot.connect( "irc.freenode.net" );
		} 
		// TODO: break these out and handle more gracefully 
		catch (IOException | IrcException e) {
			e.printStackTrace();
			throw e;
		}
		bot.joinChannel( "#fezchat" );
		bot.joinChannel( "#pircbot" );
	}
}
