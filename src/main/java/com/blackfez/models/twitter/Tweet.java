package com.blackfez.models.twitter;

import java.io.Serializable;
import java.util.Date;

import twitter4j.Status;

public class Tweet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date createdAt;
	private Long id;
	private String text;
	private Boolean truncated;
	private String userName;
	private String userScreenName;
	private String userUrl;
	

	public Tweet() {
		// Makes XML [de]serialization happy
	}
	
	public Tweet( Status status ) {
		setCreatedAt( status.getCreatedAt() );
		System.out.println( "status id is '" + status.getId() + "'" );
		setId( status.getId() );
		System.out.println( "tweet id is '" + getId() + "'" );
		setText( status.getText() );
		setTruncated( status.isTruncated() );
		setUserName( status.getUser().getName() );
		setUserScreenName( status.getUser().getScreenName() );
		setUserUrl( status.getUser().getURL() );
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean isTruncated() {
		return truncated;
	}

	public void setTruncated(Boolean truncated) {
		this.truncated = truncated;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserScreenName() {
		return userScreenName;
	}

	public void setUserScreenName(String userScreenName) {
		this.userScreenName = userScreenName;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}


	
}
