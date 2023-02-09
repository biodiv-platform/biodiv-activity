package com.strandls.activity.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Arun
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ODKMailData {
	private String password;
	private String projectId;
	private String projectName;
	private String role;
	private String userId;

	/**
	 * 
	 */

	public ODKMailData() {
		super();
	}

	/**
	 * @param password
	 * @param projectId
	 * @param projectName
	 * @param role
	 * @param userId
	 */

	public ODKMailData(String email, String username, String password, String projectId, String projectName,
			String role, String userId) {
		super();
		this.password = password;
		this.projectId = projectId;
		this.projectName = projectName;
		this.role = role;
		this.userId = userId;

	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
