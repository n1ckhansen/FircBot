package com.blackfez.models.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ChannelUserClient {

	public static void main(String[] args) {
		ChannelUser u = null;
		File f = new File( "user.ser" );
		if( f.exists() ) {
			System.out.println( "file exists, loading user");
			try {
				FileInputStream fis = new FileInputStream( f );
				ObjectInputStream ois = new ObjectInputStream( fis );
				u = (ChannelUser) ois.readObject();
				ois.close();
				fis.close();
				
			} 
			catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			System.out.println( "No file, creating user 'skippy'" );
			u = new ChannelUser( "skippy" );
			try {
				FileOutputStream fos = new FileOutputStream( f );
				ObjectOutputStream oos = new ObjectOutputStream( fos );
				oos.writeObject( u );
				oos.close();
				fos.close();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println( u.getNic() );

	}

}
