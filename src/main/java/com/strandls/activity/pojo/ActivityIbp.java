/** */
package com.strandls.activity.pojo;

import java.util.Date;

/**
 * @author Abhishek Rudra
 */
public class ActivityIbp {

	private String activityDescription;
	private String activityType;
	private Date dateCreated;
	private Date lastUpdated;

	/**
	 * @param activityDescription
	 * @param activityType
	 * @param dateCreated
	 * @param lastUpdated
	 */
	public ActivityIbp(String activityDescription, String activityType, Date dateCreated, Date lastUpdated) {
		super();
		this.activityDescription = activityDescription;
		this.activityType = activityType;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
