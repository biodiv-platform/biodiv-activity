package com.strandls.activity.util;

public enum CCARoles {
	ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER"), ROLE_DATACURATOR("ROLE_DATACURATOR"),
	ROLE_TEMPLATECURATOR("ROLE_TEMPLATECURATOR"), ROLE_EXTDATACONTRIBUTOR("ROLE_EXTDATACONTRIBUTOR");

	String value;

	private CCARoles(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
