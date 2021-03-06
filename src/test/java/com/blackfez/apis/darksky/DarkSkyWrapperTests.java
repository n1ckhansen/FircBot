package com.blackfez.apis.darksky;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.applications.fircbot.utilities.ConfigurationManager;

public class DarkSkyWrapperTests {
	
	private DarkSkyApiWrapper dsw;

	@Before
	public void setUp() throws Exception {
		ConfigurationManager cm = new ConfigurationManager( "DSWTests" );
		ZipCodeApiWrapper zwrap = new ZipCodeApiWrapper( cm );
		dsw = new DarkSkyApiWrapper( cm, zwrap );
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetInstanceIsInstance() {
		assertEquals(DarkSkyApiWrapper.class, dsw.getClass() );
	}

	@Test
	public final void testGetForcastForZip() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetCache() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetCache() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetForcastForCoords() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRetrieveCurrentWeatherForZip() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testRetrieveWeatherForecastForZip() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSerializeTheStuff() {
//		fail("Not yet implemented"); // TODO
	}

}
