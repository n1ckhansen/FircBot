package com.blackfez.applications.fircbot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.reflections.Reflections;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;
import com.blackfez.applications.fircbot.crontasks.CronTask;
import com.blackfez.applications.fircbot.processors.MessageProcessor;
import com.blackfez.models.user.ChannelUserManager;
import com.blackfez.models.user.interfaces.IChannelUser;
import com.blackfez.models.user.interfaces.IChannelUserManager;


public class FircBot extends PircBot {
	
	private transient IChannelUserManager userManager;
	private transient ConfigurationManager cm;
	private transient final String CONFIG_FILE = System.getProperty( "user.home" ) + File.separator + ".fezconfig" + File.separator + "fircbot" + File.separator + "FircBot.xml";
	private static String DEFAULT_BOTNAME = "fircbot";
	private transient final Map<String,List<MessageProcessor>> processors = new HashMap<String,List<MessageProcessor>>();
	private transient final Timer cron = new Timer();
	private transient ZipCodeApiWrapper zipWrapper;
	private transient DarkSkyApiWrapper dskWrapper;
	
	public FircBot() throws ClassNotFoundException, IOException, ConfigurationException {		
		this.cm = new ConfigurationManager( "fircbot", new File( CONFIG_FILE ) );
		this.setName( cm.getString( "botName", DEFAULT_BOTNAME ) );
		this.initCron();
		try {
			this.userManager = new ChannelUserManager( cm );
		} 
		catch (ClassNotFoundException e) {
			System.out.println( "Unable to convert serialized user map to Map<String,IChannelUser" );
			System.out.println( "This kind of breaks things so we're going to kind of just quit now" );
			e.printStackTrace();
			System.exit( 1 );
		} catch (IOException e) {
			System.out.println( "Unable to read serialized user map  file" );
			System.out.println( "This kind of breaks things so we're going to kind of just quit now" );
			e.printStackTrace();
			System.exit( 1 );
		}
		zipWrapper = new ZipCodeApiWrapper( cm );
		dskWrapper = new DarkSkyApiWrapper( cm, zipWrapper );
	}
	
	public Map<String,IChannelUser> getChanUsers() {
		return userManager.getUserMap();
	}
	
	private void initCron() {
		Reflections reflections = new Reflections( "com.blackfez.applications.fircbot.crontasks" );
		Set<Class<? extends CronTask>> cts = reflections.getSubTypesOf( CronTask.class );
		for( Class<? extends CronTask> ct : cts ) {
			System.out.println( ct.getSimpleName() );
			 Object task = null;
			try {
				Constructor<? extends CronTask> constructor = ct.getConstructor(ConfigurationManager.class);
				task = constructor.newInstance( cm );
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cron.scheduleAtFixedRate( (TimerTask) task, Math.round( (Math.random() * 300000 ) ), ((CronTask) task).getInterval() );
		}
	}
	public void onMessage( String channel, String sender, String login, String hostname, String message ) {
		
		for( MessageProcessor mp : processors.get( channel ) ) {
			mp.processMessage(sender, login, hostname, message);
		}
	}
	public void onUserList( String channel, User[] users ) {
		userManager.processOnUserList( channel, users );
		try {
			userManager.saveUserMap();
		} catch (IOException e) {
			System.out.println( "ChannelUserManager unable to serialze the user map in onUserList()");
			e.printStackTrace();
		}
	}
	
	public void onJoin( String channel, String sender, String login, String hostname ) {
		if( !processors.containsKey( channel ) ) {
			processors.put( channel, new ArrayList<MessageProcessor>() );
			Reflections reflections = new Reflections( "com.blackfez.applications.fircbot.processors" );
			Set<Class<? extends MessageProcessor>> p = reflections.getSubTypesOf( MessageProcessor.class );
			for( Class<? extends MessageProcessor> mp : p ) {
				Constructor<?> cons;
				try {
					cons = mp.getConstructor( FircBot.class, String.class, ConfigurationManager.class, ZipCodeApiWrapper.class );
					processors.get( channel ).add( (MessageProcessor) cons.newInstance( this, channel, cm, zipWrapper ) );
				} 
				catch (NoSuchMethodException e) {
					try {
						cons = mp.getConstructor( FircBot.class, String.class, ConfigurationManager.class );
						processors.get( channel ).add( (MessageProcessor) cons.newInstance( this, channel, cm ) );
					} 
					catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} 
				catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println( processors.get( channel ) );
		this.onUserList( channel, this.getUsers( channel ) );
		
		System.out.println( channel.replaceAll( "#", "" ).toLowerCase() );

		if( channel.replaceAll( "#","" ).toLowerCase().equals("fezchat" ) ) {
			if( sender.toLowerCase().equals( "fezboy" ) )
				sendMessage( channel, sender + ": Welcome back, sir." );
			else if( !sender.equalsIgnoreCase( "fircbot" ) ) {
				sendMessage( channel, sender + ": s'up yo?!" );
				boolean fezout = true;
				for( User u : getUsers( channel ) ) {
					if( u.getNick().toLowerCase().equals( "fezboy" ) ) {
						fezout = false;
						break;
					}
				}
				if( fezout ) {
					sendMessage( channel, sender + ": fezboy is out. I'm logging so he'll see you were here." );
				}
			}
			else {
				sendMessage( channel, "I'm baaaack!" );
			}
		}
	}
	public void serializeTheStuff() throws IOException {
		dskWrapper.serializeTheStuff();
		zipWrapper.serializeTheStuff();
	}
}
