/**
 * 
 */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class SpeciesMailData {
	private Long speciesId;
	private String speciesName;
	private String group;
	private String iconUrl;
	private Long authorId;

	/**
	 * 
	 */
	public SpeciesMailData() {
		super();
	}

	/**
	 * @param speciesId
	 * @param speciesName
	 * @param iconUrl
	 * @param authorId
	 */
	public SpeciesMailData(Long speciesId, String speciesName, String iconUrl, Long authorId,String group) {
		super();
		this.speciesId = speciesId;
		this.speciesName = speciesName;
		this.iconUrl = iconUrl;
		this.authorId = authorId;
		this.group = group;
	}

	public Long getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Long speciesId) {
		this.speciesId = speciesId;
	}

	public String getSpeciesName() {
		return speciesName;
	}

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
