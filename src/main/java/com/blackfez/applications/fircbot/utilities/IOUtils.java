package com.blackfez.applications.fircbot.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

public class IOUtils {
	
	public static String GetUrlResult( URL url ) throws IOException {
		InputStream is = url.openStream();
		InputStreamReader isr = new InputStreamReader( is );
		BufferedReader br = new BufferedReader( isr );
		StringBuilder sb = new StringBuilder();
		String line;
		while( ( line = br.readLine() ) != null ) {
			sb.append( line );
		}
		return sb.toString();
	}

	public static Object LoadObject( String filename ) throws IOException, ClassNotFoundException {
		File f = new File( filename );
		FileInputStream fis = new FileInputStream( f );
		ObjectInputStream ois = new ObjectInputStream( fis );
		Object o = ois.readObject();
		ois.close();
		fis.close();
		return o;
	}
	
	public static void WriteObject( String filename, Object obj ) throws IOException {
		File f = new File( filename );
		FileOutputStream fos = new FileOutputStream( f );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		oos.writeObject( obj );
		oos.close();
		fos.close();
	}
	

}
