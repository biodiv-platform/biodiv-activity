package com.strandls.activity.pojo;

public class TaggedUser {

	private Long id;
	private String name;

	public TaggedUser() {
		super();
	}

	public TaggedUser(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TaggedUser [id=" + id + ", name=" + name + "]";
	}
}
