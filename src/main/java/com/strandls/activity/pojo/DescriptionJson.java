/** */
package com.strandls.activity.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescriptionJson {

	private String name;
	private Long ro_id;

	/** */
	public DescriptionJson() {
		super();
	}

	/**
	 * @param name
	 * @param ro_id
	 */
	public DescriptionJson(String name, Long ro_id) {
		super();
		this.name = name;
		this.ro_id = ro_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRo_id() {
		return ro_id;
	}

	public void setRo_id(Long ro_id) {
		this.ro_id = ro_id;
	}
}
