package com.strandls.activity.pojo;

import java.util.Date;

public class PageMailData {

	private Long pageId;
	private Date createdOn;
	private Long authorId;
	private String title;
	
	
	public PageMailData() {
		super();
	}


	public Long getPageId() {
		return pageId;
	}


	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}


	public Date getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public Long getAuthorId() {
		return authorId;
	}


	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
}
