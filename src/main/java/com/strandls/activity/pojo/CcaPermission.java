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
import com.strandls.activity.util.CCARoles;

/**
 * @author Arun
 *
 */

@Entity
@Table(name = "cca_permission_request")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CcaPermission {

	private Long id;
	private Long requestorId;
	private Long ownerId;
	private Long ccaid;
	private String role;
	private Date createdOn;
	private String shortName;

	public CcaPermission() {
		super();
	}

	/**
	 * @param id
	 * @param requestorId
	 * @param ownerId
	 * @param ccaid
	 * @param role
	 * @param createdOn
	 * @param shortName
	 */

	public CcaPermission(Long id, Long requestorId, Long ownerId, Long ccaid, String role, Date createdOn,
			String shortName) {
		super();
		this.id = id;
		this.requestorId = requestorId;
		this.ownerId = ownerId;
		this.ccaid = ccaid;
		this.role = role;
		this.createdOn = createdOn;
		this.shortName = shortName;
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

	@Column(name = "requestor_id")
	public Long getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(Long requestorId) {
		this.requestorId = requestorId;
	}

	@Column(name = "owner_id")
	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "cca_id")
	public Long getCcaid() {
		return ccaid;
	}

	public void setCcaid(Long ccaid) {
		this.ccaid = ccaid;
	}

	@Column(name = "role")
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
