/**
 * 
 */
package com.strandls.activity.pojo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Arun
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CCAPermssionData {

	private Long requestorId;
	private Long ownerId;
	private Long ccaId;
	private String role;
	private Date createdOn;
	private String shortName;
	
	/**
	 * 
	 */
	public CCAPermssionData() {
		super();
	}

	/**
	 * @param requestorId
	 * @param ccaId
	 * @param ownerId
	 * @param createdOn
	 * @param role
	 * @param shortName
	 */
	public CCAPermssionData(Long requestorId,Long ownerId, Long ccaId, Date createdOn,
			String role , String shortName) {
		super();
		this.requestorId = requestorId;
		this.ownerId=ownerId;
		this.ccaId = ccaId;
		this.createdOn = createdOn;
		this.role = role;
		this.shortName=shortName;
	}

	public Long getCCAId() {
		return ccaId;
	}

	public void setCCAId(Long ccaId) {
		this.ccaId = ccaId;
	}
	

	public Long getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(Long requestorId) {
		this.requestorId = requestorId;
	}
	

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
