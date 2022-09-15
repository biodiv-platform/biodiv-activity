package com.strandls.activity.pojo;

import java.util.List;

public class MailData {

	private ObservationMailData observationData;
	private PageMailData pageMailData;
	private DocumentMailData documentMailData;
	private List<UserGroupMailData> userGroupData;
	private SpeciesMailData speciesData;
	private CCAMailData ccaMailData;

	/**
	 * 
	 */
	public MailData() {
		super();
	}

	/**
	 * @param observationData
	 * @param documentMailData
	 * @param userGroupData
	 * @param speciesData
	 */
	public MailData(ObservationMailData observationData, DocumentMailData documentMailData,
			List<UserGroupMailData> userGroupData, SpeciesMailData speciesData, PageMailData pageMailData,
			CCAMailData ccaMailData) {
		super();
		this.observationData = observationData;
		this.documentMailData = documentMailData;
		this.userGroupData = userGroupData;
		this.speciesData = speciesData;
		this.ccaMailData = ccaMailData;
		this.pageMailData = pageMailData;
	}

	public ObservationMailData getObservationData() {
		return observationData;
	}

	public void setObservationData(ObservationMailData observationData) {
		this.observationData = observationData;
	}

	public DocumentMailData getDocumentMailData() {
		return documentMailData;
	}

	public void setDocumentMailData(DocumentMailData documentMailData) {
		this.documentMailData = documentMailData;
	}

	public List<UserGroupMailData> getUserGroupData() {
		return userGroupData;
	}

	public void setUserGroupData(List<UserGroupMailData> userGroupData) {
		this.userGroupData = userGroupData;
	}

	public SpeciesMailData getSpeciesData() {
		return speciesData;
	}

	public void setSpeciesData(SpeciesMailData speciesData) {
		this.speciesData = speciesData;
	}

	public CCAMailData getCcaMailData() {
		return ccaMailData;
	}

	public void setCcaMailData(CCAMailData ccaMailData) {
		this.ccaMailData = ccaMailData;
	}

	public PageMailData getPageMailData() {
		return pageMailData;
	}

	public void setPageMailData(PageMailData pageMailData) {
		this.pageMailData = pageMailData;
	}
}
