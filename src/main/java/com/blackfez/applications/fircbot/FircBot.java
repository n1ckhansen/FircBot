package com.blackfez.applications.fircbot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import com.blackfez.applications.fircbot.utilities.ReflectionUtilities;
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
	public static String BOTNET_NAME_KEY = "botName";
	private transient final Map<String,List<MessageProcessor>> processors = new HashMap<String,List<MessageProcessor>>();
	private transient final Timer cron = new Timer();
	private transient ZipCodeApiWrapper zipWrapper;
	private transient DarkSkyApiWrapper dskWrapper;
	
	public FircBot() throws ClassNotFoundException, IOException, ConfigurationException {		
		this.cm = new ConfigurationManager( "fircbot", new File( CONFIG_FILE ) );
		this.setName( cm.getString( BOTNET_NAME_KEY, DEFAULT_BOTNAME ) );
		this.userManager = new ChannelUserManager( cm );
		this.initCron();
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
//		try {
//			userManager.saveUserMap();
//		} catch (IOException e) {
//			System.out.println( "ChannelUserManager unable to serialze the user map in onUserList()");
//			e.printStackTrace();
//		}
	}
	
	public void onJoin( String channel, String sender, String login, String hostname ) {
		if( !processors.containsKey( channel ) ) {
			processors.put( channel, new ArrayList<MessageProcessor>() );
			Reflections reflections = new Reflections( "com.blackfez.applications.fircbot.processors" );
			Set<Class<? extends MessageProcessor>> p = reflections.getSubTypesOf( MessageProcessor.class );
			for( Class<? extends MessageProcessor> mp : p ) {
				Constructor<?> cons;
				//Generic processor constructor params
				Class<?>[] genericProcessor = { FircBot.class,String.class,ConfigurationManager.class };
				//Weather processors constructor parms
				Class<?>[] weatherProcessor = { FircBot.class,String.class,ConfigurationManager.class, DarkSkyApiWrapper.class };

				//Lets try the various constructor types we know of.  Perhaps this can be more dynamic in the future?
				try {
					if( null != ReflectionUtilities.GetConstructorByParameters( mp.getClass(), genericProcessor ) ) {
						cons = ReflectionUtilities.GetConstructorByParameters( mp.getClass(), genericProcessor );
						processors.get( channel ).add( (MessageProcessor)cons.newInstance( this, channel, cm ) );
					}
					else if( null != ReflectionUtilities.GetConstructorByParameters( mp.getClass(), weatherProcessor ) ) {
						cons = ReflectionUtilities.GetConstructorByParameters( mp.getClass(), weatherProcessor );
						processors.get( channel ).add( (MessageProcessor)cons.newInstance( this, channel, cm, dskWrapper ) );
					}
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						System.out.println( "Unable to find a constructor matching known patterns for class " + mp.getName() );
						e.printStackTrace();
						System.exit( 1 );
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
