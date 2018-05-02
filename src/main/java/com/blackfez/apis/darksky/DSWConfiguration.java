package com.blackfez.apis.darksky;

import java.util.HashMap;
import java.util.Map;

import com.blackfez.fezcore.configurationmanager.interfaces.IConfigurationBlock;

public class DSWConfiguration implements IConfigurationBlock {

	private Map<Keys,String> keyPairs;
	public enum Keys { dataPath };
	
	public DSWConfiguration() {
		keyPairs = new HashMap<Keys,String>();
		for( Keys key : Keys.values() ) {
			if( !keyPairs.containsKey( key ) )
				keyPairs.put( key, "" );
		}
	}
	
	@Override
	public Class<?> getBlockClass() {
		return this.getClass();
	}
	
	public String getKeyValue( Keys key ) {
		return getKeyPairs().get( key );
	}
	
	public Map<Keys,String> getKeyPairs() {
		return keyPairs;
	}
	
	public void setKeyPairs( Map<Keys,String> map ) {
		keyPairs = map;
	}

}
