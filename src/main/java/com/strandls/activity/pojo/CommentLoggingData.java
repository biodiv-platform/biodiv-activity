/**
 * 
 */
package com.strandls.activity.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentLoggingData {

	private String body;
	private Long rootHolderId;
	private String rootHolderType;
	private Long subRootHolderId;
	private String subRootHolderType;
	private MailData mailData;
	private Long languageId;

	/**
	 * 
	 */
	public CommentLoggingData() {
		super();
	}

	/**
	 * @param body
	 * @param rootHolderId
	 * @param rootHolderType
	 * @param subRootHolderId
	 * @param subRootHolderType
	 * @param mailData
	 * @param languageId
	 */
	public CommentLoggingData(String body, Long rootHolderId, String rootHolderType, Long subRootHolderId,
			String subRootHolderType, MailData mailData, Long languageId) {
		super();
		this.body = body;
		this.rootHolderId = rootHolderId;
		this.rootHolderType = rootHolderType;
		this.subRootHolderId = subRootHolderId;
		this.subRootHolderType = subRootHolderType;
		this.mailData = mailData;
		this.languageId = languageId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Long getRootHolderId() {
		return rootHolderId;
	}

	public void setRootHolderId(Long rootHolderId) {
		this.rootHolderId = rootHolderId;
	}

	public String getRootHolderType() {
		return rootHolderType;
	}

	public void setRootHolderType(String rootHolderType) {
		this.rootHolderType = rootHolderType;
	}

	public Long getSubRootHolderId() {
		return subRootHolderId;
	}

	public void setSubRootHolderId(Long subRootHolderId) {
		this.subRootHolderId = subRootHolderId;
	}

	public String getSubRootHolderType() {
		return subRootHolderType;
	}

	public void setSubRootHolderType(String subRootHolderType) {
		this.subRootHolderType = subRootHolderType;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

}
