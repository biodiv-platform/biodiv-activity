/**
 * 
 */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class CCAActivityLogging {

	private String activityDescription;
	private String rootObjectId;
	private String subRootObjectId;
	private String rootObjectType;
	private String activityId;
	private String activityType;

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
	public CCAActivityLogging(String activityDescription, String rootObjectId, String subRootObjectId,
			String rootObjectType, String activityId, String activityType) {
		this.activityDescription = activityDescription;
		this.rootObjectId = rootObjectId;
		this.subRootObjectId = subRootObjectId;
		this.rootObjectType = rootObjectType;
		this.activityId = activityId;
		this.activityType = activityType;
	}

	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	public String getRootObjectId() {
		return rootObjectId;
	}

	public void setRootObjectId(String rootObjectId) {
		this.rootObjectId = rootObjectId;
	}

	public String getSubRootObjectId() {
		return subRootObjectId;
	}

	public void setSubRootObjectId(String subRootObjectId) {
		this.subRootObjectId = subRootObjectId;
	}

	public String getRootObjectType() {
		return rootObjectType;
	}

	public void setRootObjectType(String rootObjectType) {
		this.rootObjectType = rootObjectType;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

}
