/**
 * 
 */
package com.strandls.activity;

/**
 * @author Abhishek Rudra
 *
 */
public enum ActivityEnums {

	OBSERVATION("species.participation.Observation"), RECOMMENDATIONVOTE("species.participation.RecommendationVote"),
	DATATABLE("content.eml.Datatable"),PAGE("content.eml.Page"), USERGROUP("species.groups.UserGroup"), FACTS("species.trait.Fact"),
	COMMENTS("species.participation.Comment"), FLAG("species.participation.Flag"), USER("species.auth.SUser"),
	CUSTOMFIELD("CustomField"), FILTERRULE("FilterRule"), DOCUMENT("content.eml.Document"), SPECIES("species.Species"),
	SPECIESFIELD("species.SpeciesField"), TAXONOMYREGISTRY("species.TaxonomyRegistry"),
	TAXONOMYDEFINITION("species.TaxonomyDefinition"), COMMONNAMES("species.CommonNames"),
	SYNONYMS("species.SynonymsMerged"), CCADATA("cca.Data"), CCATEMPLATE("cca.Template");

	String value;

	private ActivityEnums(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
