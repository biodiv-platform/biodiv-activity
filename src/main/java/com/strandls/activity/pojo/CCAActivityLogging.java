/**
 * 
 */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class CCAActivityLogging extends CoreActivityLoggingData {
	private MailData mailData;
	/**
	 * 
	 */
	public CCAActivityLogging() {
		super();
	}

	/**
	 * @param activityDescription
	 * @param rootObjectId
	 * @param subRootObjectId
	 * @param rootObjectType
	 * @param activityId
	 * @param activityType
	 * @param mailData 
	 */
	
	public CCAActivityLogging(String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType, MailData mailData) {
		super(activityDescription, rootObjectId, subRootObjectId, rootObjectType, activityId, activityType);
		this.mailData = mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

	public MailData getMailData() {
		return mailData;
	}
}
