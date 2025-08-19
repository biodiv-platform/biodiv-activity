/** */
package com.strandls.activity.pojo;

import com.strandls.user.pojo.UserIbp;

/**
 * @author Abhishek Rudra
 */
public class ShowActivityIbp {

	private ActivityIbp activityIbp;
	private CommentsIbp commentsIbp;
	private CommentsIbp reply;
	private UserGroupActivity userGroup;
	private RecoVoteActivity recoVote;
	private UserIbp userIbp;

	/** */
	public ShowActivityIbp() {
		super();
	}

	/**
	 * @param activityIbp
	 * @param commentsIbp
	 * @param reply
	 * @param userGroup
	 * @param recoVote
	 * @param userIbp
	 */
	public ShowActivityIbp(ActivityIbp activityIbp, CommentsIbp commentsIbp, CommentsIbp reply,
			UserGroupActivity userGroup, RecoVoteActivity recoVote, UserIbp userIbp) {
		super();
		this.activityIbp = activityIbp;
		this.commentsIbp = commentsIbp;
		this.reply = reply;
		this.userGroup = userGroup;
		this.recoVote = recoVote;
		this.userIbp = userIbp;
	}

	public ActivityIbp getActivityIbp() {
		return activityIbp;
	}

	public void setActivityIbp(ActivityIbp activityIbp) {
		this.activityIbp = activityIbp;
	}

	public CommentsIbp getCommentsIbp() {
		return commentsIbp;
	}

	public void setCommentsIbp(CommentsIbp commentsIbp) {
		this.commentsIbp = commentsIbp;
	}

	public CommentsIbp getReply() {
		return reply;
	}

	public void setReply(CommentsIbp reply) {
		this.reply = reply;
	}

	public UserGroupActivity getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupActivity userGroup) {
		this.userGroup = userGroup;
	}

	public RecoVoteActivity getRecoVote() {
		return recoVote;
	}

	public void setRecoVote(RecoVoteActivity recoVote) {
		this.recoVote = recoVote;
	}

	public UserIbp getUserIbp() {
		return userIbp;
	}

	public void setUserIbp(UserIbp userIbp) {
		this.userIbp = userIbp;
	}
}
