/**
 * 
 */
package com.strandls.activity.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */
@Entity
@Table(name = "activity_feed")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7491402738483974055L;

	private Long id;
	private String activityDescription;
	private Long activityHolderId;
	private String activityHolderType;
	private String activityType;
	private Long authorId;
	private Date dateCreated;
	private Date lastUpdated;
	private Long rootHolderId;
	private String rootHolderType;
	private Long subRootHolderId;
	private String subRootHolderType;
	private Boolean isShowable;

	/**
	 * 
	 */
	public Activity() {
		super();
	}

	/**
	 * @param id
	 * @param activityDescription
	 * @param activityHolderId
	 * @param activityHolderType
	 * @param activityType
	 * @param authorId
	 * @param dateCreated
	 * @param lastUpdated
	 * @param rootHolderId
	 * @param rootHolderType
	 * @param subRootHolderId
	 * @param subRootHolderType
	 * @param isShowable
	 */
	public Activity(Long id, String activityDescription, Long activityHolderId, String activityHolderType,
			String activityType, Long authorId, Date dateCreated, Date lastUpdated, Long rootHolderId,
			String rootHolderType, Long subRootHolderId, String subRootHolderType, Boolean isShowable) {
		super();
		this.id = id;
		this.activityDescription = activityDescription;
		this.activityHolderId = activityHolderId;
		this.activityHolderType = activityHolderType;
		this.activityType = activityType;
		this.authorId = authorId;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
		this.rootHolderId = rootHolderId;
		this.rootHolderType = rootHolderType;
		this.subRootHolderId = subRootHolderId;
		this.subRootHolderType = subRootHolderType;
		this.isShowable = isShowable;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "activity_descrption")
	public String getActivityDescription() {
		return activityDescription;
	}

	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}

	@Column(name = "activity_holder_id")
	public Long getActivityHolderId() {
		return activityHolderId;
	}

	public void setActivityHolderId(Long activityHolderId) {
		this.activityHolderId = activityHolderId;
	}

	@Column(name = "activity_holder_type")
	public String getActivityHolderType() {
		return activityHolderType;
	}

	public void setActivityHolderType(String activityHolderType) {
		this.activityHolderType = activityHolderType;
	}

	@Column(name = "activity_type")
	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "date_created")
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "last_updated")
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "root_holder_id")
	public Long getRootHolderId() {
		return rootHolderId;
	}

	public void setRootHolderId(Long rootHolderId) {
		this.rootHolderId = rootHolderId;
	}

	@Column(name = "root_holder_type")
	public String getRootHolderType() {
		return rootHolderType;
	}

	public void setRootHolderType(String rootHolderType) {
		this.rootHolderType = rootHolderType;
	}

	@Column(name = "sub_root_holder_id")
	public Long getSubRootHolderId() {
		return subRootHolderId;
	}

	public void setSubRootHolderId(Long subRootHolderId) {
		this.subRootHolderId = subRootHolderId;
	}

	@Column(name = "sub_root_holder_type")
	public String getSubRootHolderType() {
		return subRootHolderType;
	}

	public void setSubRootHolderType(String subRootHolderType) {
		this.subRootHolderType = subRootHolderType;
	}

	@Column(name = "is_showable")
	public Boolean getIsShowable() {
		return isShowable;
	}

	public void setIsShowable(Boolean isShowable) {
		this.isShowable = isShowable;
	}
}
