/**
 * 
 */
package com.strandls.activity.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.strandls.activity.pojo.Activity;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.ActivityResult;
import com.strandls.activity.pojo.CCAActivityLogging;
import com.strandls.activity.pojo.CcaPermission;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.DatatableActivityLogging;
import com.strandls.activity.pojo.DocumentActivityLogging;
import com.strandls.activity.pojo.ODKMailData;
import com.strandls.activity.pojo.PageAcitvityLogging;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.TaxonomyActivityLogging;
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

	public Activity logTaxonomyActivities(HttpServletRequest request, Long userId, TaxonomyActivityLogging loggingData);

	public Activity logPageActivities(HttpServletRequest request, Long userId, PageAcitvityLogging loggingData);

	public Activity logDatatableActivities(HttpServletRequest request, Long userId,
			DatatableActivityLogging loggingData);

	public Integer activityCount(String objectType, Long objectId);

	public Activity logCCAActivities(HttpServletRequest request, Long userId, CCAActivityLogging ccaActivityLogging);

	public Boolean checkCCARequest(CcaPermission permissionReq);

	public Boolean sendDownloadLink(String authorId, String fileName, String type);

	public Activity removeComment(HttpServletRequest request, Long userId, String commentType,
			CommentLoggingData commentData, String commentId);

	public Boolean odkUserMail(ODKMailData odkMail);

	public Activity logCropcertActivities(String jsonString);
}
