/** */
package com.strandls.activity.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 */
public class ActivityResult {

	private List<ShowActivityIbp> activity;
	private Integer commentCount;

	/**
	 * @param activity
	 * @param commentCount
	 */
	public ActivityResult(List<ShowActivityIbp> activity, Integer commentCount) {
		super();
		this.activity = activity;
		this.commentCount = commentCount;
	}

	public List<ShowActivityIbp> getActivity() {
		return activity;
	}

	public void setActivity(List<ShowActivityIbp> activity) {
		this.activity = activity;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
}
