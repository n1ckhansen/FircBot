package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;

public class ConfigurationManager {
	
	private FileBasedConfigurationBuilder<XMLConfiguration> builder;
	private Configuration config;
	
	public ConfigurationManager( String appName, File path ) throws ConfigurationException {
		File f = path ;
		if( !f.exists() ) {
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
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Parameters params = new Parameters();
		builder = new FileBasedConfigurationBuilder<XMLConfiguration>( XMLConfiguration.class )
				.configure( 
						params.xml()
						.setFileName( 
								f.getPath()
						)
						.setExpressionEngine( 
								new XPathExpressionEngine() 
						) 
				);
		builder.setAutoSave( true );
		config = builder.getConfiguration();
		System.out.println( f.getPath() );
		
	}
	
	public ConfigurationManager( String appName ) throws ConfigurationException {
		this( appName, new File(
				new StringBuffer()
				.append( System.getProperty( "user.home" ) )
				.append( File.separator )
				.append( ".fezconfigs" )
				.append( File.separator )
				.append( appName )
				.append( File.separator )
				.append( appName )
				.append(".xml" ).toString()
			)
		);
	}
	
	public String getString( String xpath, String defaultValue ) {
		Iterator<String> itr = config.getKeys();
		while( itr.hasNext() ) {
			System.out.println( itr.next() );
		}
		if( !config.containsKey( xpath ) ) {
			System.out.println( "doesn't have key " + xpath );
			config.addProperty( xpath, defaultValue );
		}
		return config.getString( xpath );
	}
	
	public String getString( String xpath ) {
		return getString( xpath, "" );
	}
	
	public void setString( String xpath, String value ) {
		System.out.println( "Seeting " + xpath + " to " + value );
		config.setProperty( xpath, value );
		System.out.println( "done" );
	}

}
