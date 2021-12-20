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
	 */
	
	public CCAActivityLogging(String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType) {
		super(activityDescription, rootObjectId, subRootObjectId, rootObjectType, activityId, activityType);
	}

}
