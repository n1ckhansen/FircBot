package com.blackfez.models.rss;

import java.util.Date;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;

public class Entry {
	
	private String author;
	private String contents;
	private String link;
	private Date publishedDate;
	private String title;
	private String uri;

	public Entry() {
		// TODO Auto-generated constructor stub
	}
	
	public Entry( SyndEntry syndEntry ) {
		setAuthor( null == syndEntry.getAuthor() ? "" : syndEntry.getAuthor() );
		setTitle( null == syndEntry.getTitle() ? "" : syndEntry.getTitle() );
		setPublishedDate( null == syndEntry.getPublishedDate() ? new Date() : syndEntry.getPublishedDate() );
		setLink( null == syndEntry.getLink() ? "" : syndEntry.getLink() );
		setUri( null == syndEntry.getUri() ? "" : syndEntry.getUri() );
		StringBuffer sb = new StringBuffer();
		for( SyndContent con : syndEntry.getContents() ) {
			if( syndEntry.getContents().indexOf( con ) != 0 ) {
				sb.append( System.lineSeparator() );
				sb.append( System.lineSeparator() );
			}
			sb.append( con.getValue() );
		}
		setContents( sb.toString() );
	}

	public String getAuthor() {
		return author;
	}

	public String getContents() {
		return contents;
	}
	
	public String getLink() {
		return link;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public String getTitle() {
		return title;
	}
	
	public String getUri() {
		return uri;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setLink( String lnk ) {
		link = lnk;
	}
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setUri( String u ) {
		this.uri = u;
	}

}
