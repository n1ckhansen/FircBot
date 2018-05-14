package com.blackfez.models.user;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blackfez.models.geolocation.Location;

public class ChannelUserTests {
	
	private ChannelUser testUser;
	private String TEST_NIC = "Skippy";
	private String TEST_CHANNEL_1 = "testChannel1";
	private String TEST_CHANNEL_2 = "testChannel2";
	private String TEST_CHANNEL_3 = "testChannel3";

	@Before
	public void setUp() throws Exception {
		testUser = new ChannelUser( TEST_NIC );
	}

	@After
	public void tearDown() throws Exception {
	}
	/*

	@Test
	public final void testChannelUserConstructorSetsNic() {
		assertEquals( TEST_NIC, testUser.getNic() );
	}
	
	@Test
	public final void testConstructorCreatesEmptyChannel() {
		assertEquals( 0, testUser.getChannels().size() );
	}
	
	@Test
	public final void testAddChannel() {
		Set<String> channelsValues = new HashSet<String>( Arrays.asList( TEST_CHANNEL_1 ) );
		assertEquals( 0, testUser.getChannels().size() );
		testUser.addChannel( TEST_CHANNEL_1 );
		assertEquals( 1, testUser.getChannels().size() );
		assertEquals( channelsValues, testUser.getChannels() );
	}
	
	public final void testAddMultipleChannels() {
		Set<String> channelsValues = new HashSet<String>( Arrays.asList( TEST_CHANNEL_1, TEST_CHANNEL_2, TEST_CHANNEL_3 ) );
		assertEquals( 0, testUser.getChannels().size() );
		testUser.addChannel( TEST_CHANNEL_1 );
		testUser.addChannel( TEST_CHANNEL_2 );
		testUser.addChannel( TEST_CHANNEL_3 );
		assertEquals( 3, testUser.getChannels().size() );
		assertEquals( channelsValues, testUser.getChannels() );
	}

	@Test
	public final void testGetLocationAfterChange() {
		Location loc1 = new Location();
		Location loc2 = new Location();
		loc1.setZip( "12345" );
		loc2.setZip( "54321" );
		testUser.setLocation( loc1 );
		assertSame( loc1, testUser.getLocation() );
		testUser.setLocation( loc2 );
		assertSame( loc2, testUser.getLocation() );
	}

	@Test
	public final void testGetNic() {
		assertEquals( TEST_NIC, testUser.getNic() );
	}

	@Test
	public final void testRemoveChannel() {
		Set<String> channelsValues = new HashSet<String>( Arrays.asList( TEST_CHANNEL_1, TEST_CHANNEL_3 ) );
		testAddMultipleChannels();
		testUser.removeChannel( TEST_CHANNEL_2 );
		assertEquals( 2, testUser.getChannels().size() );
		assertEquals( channelsValues, testUser.getChannels() );
	}
	*/

}
