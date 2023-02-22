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
	private String sUserId;

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
	 * @param sUserId
	 */

	public ODKMailData(String email, String username, String password, String projectId, String projectName,
			String role, String sUserId) {
		super();
		this.password = password;
		this.projectId = projectId;
		this.projectName = projectName;
		this.role = role;
		this.sUserId = sUserId;
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

	public String getsUserId() {
		return sUserId;
	}

	public void setsUserId(String sUserId) {
		this.sUserId = sUserId;
	}

}
