package com.blackfez.apis.youtube;

import java.io.IOException;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;

public class YouTubeApiWrapperClient {

	public static void main(String[] args) {
		try {
			YouTube yt = YouTubeApiWrapper.getYouTubeService();
			YouTube.Channels.List channelListByUsernameRequest = yt.channels().list( "snippet,contentDetails,statistics" );
			channelListByUsernameRequest.setForUsername( "GoogleDevelopers" );
			ChannelListResponse response = channelListByUsernameRequest.execute();
			Channel channel = response.getItems().get( 0 );
			System.out.printf( "This channel's ID is %s.  Its title is '%s'.  It has %s views.\n",
				channel.getId(),
				channel.getSnippet().getTitle(),
				channel.getStatistics().getViewCount()
			);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
