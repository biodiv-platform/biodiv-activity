/**
 * 
 */
package com.strandls.activity.pojo;

import java.util.Date;

/**
 * @author Abhishek Rudra
 *
 */
public class DocumentMailData {

	private Long documentId;
	private Date createdOn;
	private Long authorId;
	private String type;
	private String title;

	/**
	 * 
	 */
	public DocumentMailData() {
		super();
	}

	/**
	 * @param documentId
	 * @param createdOn
	 * @param authorId
	 */
	public DocumentMailData(Long documentId, Date createdOn, Long authorId, String type, String title) {
		super();
		this.documentId = documentId;
		this.createdOn = createdOn;
		this.authorId = authorId;
		this.type = type;
		this.title = title;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
