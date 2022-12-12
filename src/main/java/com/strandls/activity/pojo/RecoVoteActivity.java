/**
 * 
 */
package com.strandls.activity.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class RecoVoteActivity {

	private String scientificName;
	private String commonName;
	private String givenName;
	private Long speciesId;
	private String source;

	/**
	 * 
	 */
	public RecoVoteActivity() {
		super();
	}

	/**
	 * @param scientificName
	 * @param commonName
	 * @param givenName
	 * @param speciesId
	 */
	public RecoVoteActivity(String scientificName, String commonName, String givenName, Long speciesId, String source) {
		super();
		this.scientificName = scientificName;
		this.commonName = commonName;
		this.givenName = givenName;
		this.speciesId = speciesId;
		this.source = source;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public Long getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Long speciesId) {
		this.speciesId = speciesId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
