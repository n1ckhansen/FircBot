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

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.reflections.Reflections;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.crontasks.CronTask;
import com.blackfez.applications.fircbot.processors.MessageProcessor;
import com.blackfez.applications.fircbot.utilities.IOUtils;
import com.blackfez.models.user.ChannelUser;


public class FircBot extends PircBot {
	
	private static final String CHANUSERSFILE = "chanUsers.ser";
	private Map<String,ChannelUser> ChanUsers;
	private static final String BOTNAME = "citadelOfJerrys";
	private transient final Map<String,List<MessageProcessor>> processors = new HashMap<String,List<MessageProcessor>>();
	private transient final Timer cron = new Timer();
	
	public FircBot() {		

		this.setName( BOTNAME );
		try {
			this.initChanUsers();
			this.initCron();
		} 
		catch (ClassNotFoundException | IOException e) {
			System.out.println( "Fatal error encountered when deserializing ChannelUser cache" );
			e.printStackTrace();
			System.exit( 1 );
		}
		ZipCodeApiWrapper.getInstance();
		DarkSkyApiWrapper.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	private void initChanUsers() throws IOException, ClassNotFoundException {
		File f = new File( CHANUSERSFILE );
		if( ! f.exists() ) {
			this.setChanUsers(new HashMap<String,ChannelUser>());
			IOUtils.WriteObject( CHANUSERSFILE, this.getChanUsers() );
		}
		this.setChanUsers((Map<String,ChannelUser>) IOUtils.LoadObject( CHANUSERSFILE ));
	}
	
	private void initCron() {
		Reflections reflections = new Reflections( "com.blackfez.applications.fircbot.crontasks" );
		Set<Class<? extends CronTask>> cts = reflections.getSubTypesOf( CronTask.class );
		for( Class<? extends CronTask> ct : cts ) {
			Method getter = null;
			Object task = null;
			try {
				getter = ct.getDeclaredMethod( "getInstance" );
				task = (Object) getter.invoke( null );
				((CronTask) task).setBot( this );
			} 
			catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
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
		for( User u : users ) {
			if( this.getChanUsers().containsKey( u.getNick() ) ) {
				this.getChanUsers().get( u.getNick() ).addChannel( channel );
				this.getChanUsers().get( u.getNick() ).setUser( u );
			}
			else {
				this.getChanUsers().put( u.getNick(), new ChannelUser( u.getNick() ) );
				this.getChanUsers().get( u.getNick() ).addChannel( channel );
				this.getChanUsers().get( u.getNick() ).setUser( u );
			}
		}
		try {
			this.serializeTheStuff();
		} catch (IOException e) {
			System.out.println( "unable to serialze the stuff in onUserList()");
			e.printStackTrace();
		}
	}
	
	public void onJoin( String channel, String sender, String login, String hostname ) {
		// TODO: use singleton pattern for processors (like cron) and track channels there?
		if( !processors.containsKey( channel ) ) {
			processors.put( channel, new ArrayList<MessageProcessor>() );
			Reflections reflections = new Reflections( "com.blackfez.applications.fircbot.processors" );
			Set<Class<? extends MessageProcessor>> p = reflections.getSubTypesOf( MessageProcessor.class );
			for( Class<? extends MessageProcessor> mp : p ) {
				try {
					Constructor<?> cons = mp.getConstructor( FircBot.class, String.class );
					processors.get( channel ).add( (MessageProcessor) cons.newInstance( this, channel ) );
				} 
				catch ( NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println( processors.get( channel ) );

		if( channel.replaceAll( "#","" ).toLowerCase() == "fezchat" ) {
			if( sender.toLowerCase().equals( "fezboy" ) )
				sendMessage( channel, sender + ": Welcome back, sir." );
			else if( !sender.equalsIgnoreCase( "fircbot" ) ) {
				sendMessage( channel, sender + ": s'up beyotch?!" );
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
		IOUtils.WriteObject(CHANUSERSFILE, this.getChanUsers() );
		DarkSkyApiWrapper.getInstance().serializeTheStuff();
		ZipCodeApiWrapper.getInstance().serializeTheStuff();
	}

	public Map<String,ChannelUser> getChanUsers() {
		return ChanUsers;
	}

	public void setChanUsers(Map<String,ChannelUser> chanUsers) {
		ChanUsers = chanUsers;
	}

}
