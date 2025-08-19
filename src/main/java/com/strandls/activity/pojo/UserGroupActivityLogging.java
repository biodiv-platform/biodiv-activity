/** */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 */
public class UserGroupActivityLogging extends CoreActivityLoggingData {

	/** */
	public UserGroupActivityLogging() {
	}

	/**
	 * @param activityDescription
	 * @param rootObjectId
	 * @param subRootObjectId
	 * @param rootObjectType
	 * @param activityId
	 * @param activityType
	 */
	public UserGroupActivityLogging(String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType) {
		super(activityDescription, rootObjectId, subRootObjectId, rootObjectType, activityId, activityType);
	}
}
