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
public class CcaPermission extends CCAMailData{

	private Long id;
	private Long requestorId;
	private Long ownerId;
	private Long ccaid;
	private String role;
	private Date requestedOn;
	private String shortName;
	private String encryptKey;
	private String requestorMessage;

	public CcaPermission() {
		super();
	}

	/**
	 * @param id
	 * @param requestorId
	 * @param ownerId
	 * @param ccaid
	 * @param role
	 * @param requestedOn
	 * @param shortName
	 * @param requestorMessage
	 */

	public CcaPermission(Long id, Long requestorId, Long ownerId, Long ccaid, String role, Date requestedOn,
			String shortName, String encryptKey,String requestorMessage) {
		super();
		this.id = id;
		this.requestorId = requestorId;
		this.ownerId = ownerId;
		this.ccaid = ccaid;
		this.role = role;
		this.requestedOn = requestedOn;
		this.shortName = shortName;
		this.encryptKey = encryptKey;
		this.requestorMessage = requestorMessage;
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

	@Column(name = "requested_on")
	public Date getRequestedOn() {
		return requestedOn;
	}

	public void setRequestedOn(Date requestedOn) {
		this.requestedOn = requestedOn;
	}

	@Column(name = "short_name")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}
	public String getRequestorMessage() {
		return requestorMessage;
	}

	public void setRequestorMessage(String requestorMessage) {
		this.requestorMessage = requestorMessage;
	}

}
