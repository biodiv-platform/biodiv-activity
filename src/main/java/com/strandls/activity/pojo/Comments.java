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
@Table(name = "comment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1560748571733072408L;

	private Long id;
	private Long authorId;
	private String body;
	private Long commentHolderId;
	private String commentHolderType;
	private Date dateCreated;
	private Date lastUpdated;
	private Long rootHolderId;
	private String rootHolderType;
	private Long mainParentId;
	private Long parentId;
	private Long languageId;
	private Boolean isDeleted;

	/**
	 * 
	 */
	public Comments() {
		super();
	}

	/**
	 * @param id
	 * @param authorId
	 * @param body
	 * @param commentHolderId
	 * @param commentHolderType
	 * @param dateCreated
	 * @param lastUpdated
	 * @param rootHolderId
	 * @param rootHolderType
	 * @param mainParentId
	 * @param parentId
	 * @param languageId
	 */
	public Comments(Long id, Long authorId, String body, Long commentHolderId, String commentHolderType,
			Date dateCreated, Date lastUpdated, Long rootHolderId, String rootHolderType, Long mainParentId,
			Long parentId, Long languageId) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.body = body;
		this.commentHolderId = commentHolderId;
		this.commentHolderType = commentHolderType;
		this.dateCreated = dateCreated;
		this.lastUpdated = lastUpdated;
		this.rootHolderId = rootHolderId;
		this.rootHolderType = rootHolderType;
		this.mainParentId = mainParentId;
		this.parentId = parentId;
		this.languageId = languageId;
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

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "body")
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Column(name = "comment_holder_id")
	public Long getCommentHolderId() {
		return commentHolderId;
	}

	public void setCommentHolderId(Long commentHolderId) {
		this.commentHolderId = commentHolderId;
	}

	@Column(name = "comment_holder_type")
	public String getCommentHolderType() {
		return commentHolderType;
	}

	public void setCommentHolderType(String commentHolderType) {
		this.commentHolderType = commentHolderType;
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

	@Column(name = "main_parent_id")
	public Long getMainParentId() {
		return mainParentId;
	}

	public void setMainParentId(Long mainParentId) {
		this.mainParentId = mainParentId;
	}

	@Column(name = "parent_id")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "language_id")
	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	@Column(name = "is_deleted")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
