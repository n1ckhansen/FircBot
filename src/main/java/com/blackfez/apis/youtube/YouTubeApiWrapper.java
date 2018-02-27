package com.blackfez.apis.youtube;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YouTubeApiWrapper {
	private static final String API_KEY = "AIzaSyCpJXZcnuq52lmqTEqEb75_q0rv6hc8Uhw";
	private static final String APPLICATION_NAME = "youtubescraper";
	private static final File DATA_STORE_DIR = new File( System.getProperty( "user.home" ), ".credentials/youtubescraper" );
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static HttpTransport HTTP_TRANSPORT;
	private static final List<String> SCOPES = Arrays.asList( YouTubeScopes.YOUTUBE_READONLY );
	
	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory( DATA_STORE_DIR );
		}
		catch( Throwable t ) {
			t.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public static Credential authorize() throws IOException {
		String f = new File( DATA_STORE_DIR + "/client_secret.json" ).getAbsolutePath();
		System.out.println( f );
		InputStream is = new FileInputStream( f );
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load( JSON_FACTORY, new InputStreamReader( is ) );
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder( HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES )
				.setDataStoreFactory( DATA_STORE_FACTORY )
				.setAccessType( "offline" )
				.build();
		Credential credential = new AuthorizationCodeInstalledApp( flow, new LocalServerReceiver() ).authorize( "user" );
		return credential;
	}
	
	public static YouTube getYouTubeService() throws IOException {
		Credential credential = authorize();
		return new YouTube.Builder( HTTP_TRANSPORT, JSON_FACTORY, credential )
				.setApplicationName( APPLICATION_NAME )
				.build();
	}
	
	public static String getYouTubeInfoForId( String id ) throws IOException {
		YouTube yt = getYouTubeService();
		YouTube.Videos.List videoListByIdRequest = yt.videos().list( "snippet,contentDetails" );
		videoListByIdRequest.setId( id );
		VideoListResponse response = videoListByIdRequest.execute();
		Video video = response.getItems().get( 0 );
		StringBuffer sb = new StringBuffer();
		sb.append( "YouTube: " );
		sb.append( video.getSnippet().getTitle() );
		sb.append( " (" );
		sb.append( video.getContentDetails().getDuration() );
		sb.append( ") " );
		return sb.toString(); 
		
	}

}
