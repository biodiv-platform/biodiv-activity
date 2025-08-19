/** */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 */
public class MailActivityData {

	private String activityType;
	private String activityDescription;
	private MailData mailData;

	/** */
	public MailActivityData() {
		super();
	}

	/**
	 * @param activityType
	 * @param activityDescription
	 * @param mailData
	 */
	public MailActivityData(String activityType, String activityDescription, MailData mailData) {
		super();
		this.activityType = activityType;
		this.activityDescription = activityDescription;
		this.mailData = mailData;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}
}
