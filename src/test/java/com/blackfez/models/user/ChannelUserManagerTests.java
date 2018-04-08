package com.blackfez.models.user;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.blackfez.models.user.interfaces.IChannelUser;
import com.blackfez.models.user.interfaces.IChannelUserManager;

public class ChannelUserManagerTests {
	
	private IChannelUserManager cum = null; 
	private IChannelUser channelUser1 = null;
	private IChannelUser channelUser2 = null;
	private String user1Nic = "skippy"; 
	private String user2Nic = "sloppy";

	@Before
	public void setUp() throws Exception {
		cum = new ChannelUserManager();
		channelUser1 = Mockito.mock( IChannelUser.class );
		channelUser2 = Mockito.mock( IChannelUser.class );
	}

	@After
	public void tearDown() throws Exception {
		cum = null;
	}

	@Test
	public final void testGetUserMap() {
		Map<String,IChannelUser> testMap = new HashMap<String,IChannelUser>();
		assertEquals( testMap, cum.getUserMap() );
	}

	@Test
	public final void testGetUser() {
		cum.addChannelUser( user1Nic, channelUser1 );
		assertEquals( cum.getUser( user1Nic), channelUser1 );
	}

	@Test
	public final void testGetUserWithMultipleUsers() {
		cum.addChannelUser( user1Nic, channelUser1 );
		cum.addChannelUser( user2Nic, channelUser2 );
		assertEquals( cum.getUser( user1Nic), channelUser1 );
		assertEquals( cum.getUser( user2Nic), channelUser2 );
	}

	@Test
	public final void testRemoveChannelUser() {
		cum.addChannelUser( user1Nic, channelUser1 );
		cum.addChannelUser( user2Nic, channelUser2 );
		cum.removeChannelUser( user1Nic );
		assertNull( cum.getUser( user1Nic ) );
	}

}
