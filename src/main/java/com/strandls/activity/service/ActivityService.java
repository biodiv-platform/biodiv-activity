/**
 * 
 */
package com.strandls.activity.service;

import javax.servlet.http.HttpServletRequest;

import com.strandls.activity.pojo.Activity;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.ActivityResult;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.DatatableActivityLogging;
import com.strandls.activity.pojo.DocumentActivityLogging;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.UserGroupActivityLogging;

/**
 * @author Abhishek Rudra
 *
 */
public interface ActivityService {

	public ActivityResult fetchActivityIbp(String objectType, Long objectId, String offset, String limit);

	public Activity logActivities(HttpServletRequest request, Long userId, ActivityLoggingData loggingData);

	public Activity addComment(HttpServletRequest request, Long userId, String commentType,
			CommentLoggingData commentData);

	public String sendObvCreateMail(Long userid, ActivityLoggingData loggingData);

	public Activity logUGActivities(Long userId, UserGroupActivityLogging loggingData);

	public Activity logDocActivities(HttpServletRequest request, Long userId, DocumentActivityLogging loggingData);

	public Activity logSpeciesActivities(HttpServletRequest request, Long userId, SpeciesActivityLogging loggingData);

	public Activity logDatatableActivities(HttpServletRequest request, Long userId, DatatableActivityLogging loggingData);

	public Integer activityCount(String objectType, Long objectId);
	
}
