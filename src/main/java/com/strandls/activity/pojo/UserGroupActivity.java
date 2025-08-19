/** */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 */
public class UserGroupActivity {

	private Long userGroupId;
	private String userGroupName;
	private String webAddress;
	private String featured;
	private String reason;

	/** */
	public UserGroupActivity() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param userGroupName
	 * @param webAddress
	 * @param featured
	 * @param reason
	 */
	public UserGroupActivity(Long userGroupId, String userGroupName, String webAddress, String featured,
			String reason) {
		super();
		this.userGroupId = userGroupId;
		this.userGroupName = userGroupName;
		this.webAddress = webAddress;
		this.featured = featured;
		this.reason = reason;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public String getFeatured() {
		return featured;
	}

	public void setFeatured(String featured) {
		this.featured = featured;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
