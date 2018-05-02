package com.blackfez.applications.fircbot.utilities;

import java.lang.reflect.Constructor;

public class ReflectionUtilities {
	public static Constructor<?> GetConstructorByParameters( Class<?> clazz, Class[] parms ) {
		Constructor<?>[] constructors = clazz.getConstructors();
		for( Constructor<?> cons : constructors ) {
			Class<?>[] types = cons.getParameterTypes();
			if( types.length == parms.length ) {
				boolean isMatch = true;
				for( int i = 0; i < parms.length; i++ ) {
					if( !types[i].isAssignableFrom( parms[ i ] ) ) {
						isMatch = false;
						break;
					}
				}
				if( isMatch ) 
					return cons;
			}
		}
		return null;
	}
}
