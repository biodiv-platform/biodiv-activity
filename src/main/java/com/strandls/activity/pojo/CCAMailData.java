package com.strandls.activity.pojo;

import java.util.Date;

/**
 * @author Guddu Sharma
 *
 */
public class CCAMailData {

	private Long id;
	private String location;
	private Date date;
	private Long authorId;
	public Activity activity;

	/**
	 * 
	 */
	public CCAMailData() {
		super();
	}

	/**
	 * @param id
	 * @param location
	 * @param date
	 * @param iconURl
	 * @param scientificName
	 * @param commonName
	 * @param authorId
	 */
	public CCAMailData(Long id, String location, Date date, Long authorId, Activity activity) {
		super();
		this.id = id;
		this.location = location;
		this.date = date;
		this.authorId = authorId;
		this.activity = activity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public Activity getActivity() {
		return activity;
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
