/**
 * 
 */
package com.strandls.activity.pojo;

import java.util.Date;

/**
 * @author Arun Bala
 *
 */
public class DataTableMailData {

	private Long dataTableId;
	private Date createdOn;
	private Long authorId;
	private String title;

	/**
	 * 
	 */
	public DataTableMailData() {
		super();
	}

	/**
	 * @param datatableId
	 * @param createdOn
	 * @param authorId
	 */
	public DataTableMailData(Long dataTableId, Date createdOn, Long authorId, String title) {
		super();
		this.dataTableId = dataTableId;
		this.createdOn = createdOn;
		this.authorId = authorId;
		this.title = title;
	}

	public Long getDataTableId() {
		return dataTableId;
	}

	public void setDataTableId(Long dataTableId) {
		this.dataTableId = dataTableId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
