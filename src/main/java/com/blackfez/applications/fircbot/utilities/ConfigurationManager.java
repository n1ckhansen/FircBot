package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

public class ConfigurationManager {
	
	private XMLConfiguration config;

	public ConfigurationManager( String configFile ) {
		this( new File( configFile ) );
	}
	
	public ConfigurationManager( File configFile ) {
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
