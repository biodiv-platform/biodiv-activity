package com.strandls.activity.pojo;

public class PageAcitvityLogging extends CoreActivityLoggingData {

	private MailData mailData;

	/**
	 * 
	 */
	public PageAcitvityLogging() {
		super();
	}

	/**
	 * @param activityDescription
	 * @param rootObjectId
	 * @param subRootObjectId
	 * @param rootObjectType
	 * @param activityId
	 * @param activityType
	 */

	public PageAcitvityLogging(String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType, MailData mailData) {
		super(activityDescription, rootObjectId, subRootObjectId, rootObjectType, activityId, activityType);
		this.mailData = mailData;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}
}
