package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

import com.blackfez.apis.darksky.DarkSkyApiWrapper;
import com.blackfez.apis.zipcodeapi.ZipCodeApiWrapper;
import com.blackfez.models.user.interfaces.IChannelUserManager;

public class ConfigurationManager {
	
	private XMLConfiguration config;
	private DarkSkyApiWrapper dskWrapper;
	private TwitterBank twitBank;
	private IChannelUserManager userManager;
	private ZipCodeApiWrapper zipWrapper;

	public ConfigurationManager( String configFile ) throws ClassNotFoundException, IOException {
		this( new File( configFile ) );
	}
	
	public ConfigurationManager( File configFile ) throws ClassNotFoundException, IOException {
		//Bootstrap config object
		validateFilePathExists( configFile );
		FileBasedConfigurationBuilder<XMLConfiguration> builder = initBuilder( configFile );
		try {
			config = builder.getConfiguration();
		} 
		catch (ConfigurationException e) {
			System.out.println( "Unable to create configuration object.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
		//Create utils and other pass-arounds
		zipWrapper = new ZipCodeApiWrapper( this );
		dskWrapper = new DarkSkyApiWrapper( this, zipWrapper );
		twitBank = new TwitterBank( this );
	}
	
	public Boolean getBooleanValue( String xpath, Boolean defaultValue ) {
		if( !config.containsKey( xpath ) )
			config.addProperty(xpath, String.valueOf( defaultValue ) );
		return config.getBoolean( xpath );
	}
	
	public Boolean getBooleanValue( String xpath ) {
		return config.getBoolean( xpath );
	}
	
	public DarkSkyApiWrapper getDskWrapper() {
		return dskWrapper;
	}
	
	public String getStringValue( String xpath, String defaultValue ) {
		if( !config.containsKey( xpath ) ) {
			config.addProperty( xpath, defaultValue );
		}
		return getStringValue( xpath );
	}
	
	public String getStringValue( String xpath ) {
		return config.getString( xpath );
	}
	
	public TwitterBank getTwitterBank() {
		return twitBank;
	}
	
	public IChannelUserManager getUserManager() {
		return this.userManager;
	}
	
	public ZipCodeApiWrapper getZipWrapper() {
		return zipWrapper;
	}
	
	private FileBasedConfigurationBuilder<XMLConfiguration> initBuilder( File f ) {
		Parameters parms = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder = 
				new FileBasedConfigurationBuilder<XMLConfiguration>( XMLConfiguration.class )
				.configure( parms.xml()
						.setFile( f )
						.setExpressionEngine( new XPathExpressionEngine() )
						
				);
		builder.setAutoSave( true );
		return builder;
	}
	
	public void setStringValue( String xpath, String value ) {
		config.setProperty( xpath, value );
	}
	
	public void setUserManager( IChannelUserManager um ) {
		this.userManager = um;
	}
	
	private void validateFilePathExists( File f ) {
		if( f.exists() )
			return;
		if( f.getParentFile() != null )
			f.getParentFile().mkdirs();
		try {
			FileWriter fw = new FileWriter( f );
			fw.write( "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" );
			fw.write( System.getProperty( "line.separator" ) );
			fw.write( "<configuration />" );
			fw.write( System.getProperty( "line.separator" ) );
			fw.flush();
			fw.close();
		}
		catch( IOException e ) {
			System.out.println( "Unable to write default config file.  Exiting application" );
			e.printStackTrace();
			System.exit( 1 );
		}
	}

}
