/**
 * 
 */
package com.strandls.activity.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Arun
 *
 */

@Entity
@Table(name = "cca_permission_request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CCAPermission {

	private Long id;
	private Long requestorId;
	private Long ownerId;
	private Long ccaId;
	private String role;
	private Date createdOn;
	private String shortName;
	
	/**
	 * 
	 */
	public CCAPermission() {
		super();
	}

	/**
	 * @param id	
	 * @param requestorId
	 * @param ccaId
	 * @param ownerId
	 * @param createdOn
	 * @param role
	 * @param shortName
	 */
	public CCAPermission(Long id, Long requestorId,Long ownerId, Long ccaId, Date createdOn,
			String role , String shortName) {
		super();
		this.id = id;
		this.requestorId = requestorId;
		this.ownerId=ownerId;
		this.ccaId = ccaId;
		this.createdOn = createdOn;
		this.role = role;
		this.shortName=shortName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "cca_id")
	public Long getCCAId() {
		return ccaId;
	}

	public void setCCAId(Long ccaId) {
		this.ccaId = ccaId;
	}
	
	@Column(name = "requestor_id")
	public Long getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(Long requestorId) {
		this.requestorId = requestorId;
	}
	
	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "role")
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
