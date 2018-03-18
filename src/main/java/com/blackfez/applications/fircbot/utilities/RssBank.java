package com.blackfez.applications.fircbot.utilities;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class RssBank implements Serializable {

	/**
	 * 
	 */
	private transient static final long serialVersionUID = 1L;
	private transient static final String RSSBANK_FILE = "rssbank.ser";
	private transient static RssBank INSTANCE = null;
	
	private RssBank() {
		this.serializeStuff();
	}
	
	public static RssBank getInstance() {
		if( null != INSTANCE )
			return INSTANCE;
		synchronized( RssBank.class ) {
			File f = new File( RSSBANK_FILE );
			if( !f.exists() ) {
				INSTANCE = new RssBank();
			}
			else {
				try {
					INSTANCE = (RssBank) IOUtils.LoadObject( RSSBANK_FILE );
				} 
				catch (ClassNotFoundException | IOException e) {
					INSTANCE = new RssBank();
				}
			}
		}
		return INSTANCE;
	}
	
	public void serializeStuff() {
		try {
			IOUtils.WriteObject( RSSBANK_FILE, this );
		} 
		catch (IOException e) {
			System.out.println( "Error encountered when serializing RssBank object" );
			e.printStackTrace();
			System.out.println( "Continuing.... " );
		}
	}

}
