/**
 * 
 */
package com.strandls.activity.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.ActivityEnums;
import com.strandls.activity.Headers;
import com.strandls.activity.dao.ActivityDao;
import com.strandls.activity.dao.CcaPermissionRequestDao;
import com.strandls.activity.dao.CommentsDao;
import com.strandls.activity.pojo.Activity;
import com.strandls.activity.pojo.ActivityIbp;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.ActivityResult;
import com.strandls.activity.pojo.CCAActivityLogging;
import com.strandls.activity.pojo.CcaPermission;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.Comments;
import com.strandls.activity.pojo.CommentsIbp;
import com.strandls.activity.pojo.DatatableActivityLogging;
import com.strandls.activity.pojo.DocumentActivityLogging;
import com.strandls.activity.pojo.MailActivityData;
import com.strandls.activity.pojo.ODKMailData;
import com.strandls.activity.pojo.PageAcitvityLogging;
import com.strandls.activity.pojo.RecoVoteActivity;
import com.strandls.activity.pojo.ShowActivityIbp;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.TaggedUser;
import com.strandls.activity.pojo.TaxonomyActivityLogging;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.pojo.UserGroupActivityLogging;
import com.strandls.activity.service.ActivityService;
import com.strandls.activity.service.MailService;
import com.strandls.activity.service.NotificationService;
import com.strandls.activity.util.ActivityUtil;
import com.strandls.activity.util.CCAMailUtils;
import com.strandls.activity.util.CCARoles;
import com.strandls.activity.util.ODKMailUtils;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.OBJECT_TYPE;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;

/**
 * @author Abhishek Rudra
 *
 */
public class ActivityServiceImpl implements ActivityService {

	private final Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

	private String newComment = "Added a comment";

	private String deletedComment = "Deleted a comment";

	private String templateComment = "Template comment";

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private ActivityDao activityDao;

	@Inject
	private CommentsDao commentsDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private NotificationService notificationSevice;

	@Inject
	private MailService mailService;

	@Inject
	private Headers headers;

	@Inject
	private CCAMailUtils ccaMailutils;

	@Inject
	private ODKMailUtils odkMailutils;

	@Inject
	private CcaPermissionRequestDao ccaPermissionDao;

	private Long defaultLanguageId = Long
			.parseLong(PropertyFileUtil.fetchProperty("config.properties", "defaultLanguageId"));

	private String siteName = PropertyFileUtil.fetchProperty("config.properties", "siteName");

	List<String> userGroupActivities = new ArrayList<String>(
			Arrays.asList("Posted resource", "Removed resoruce", "Featured", "UnFeatured"));

	List<String> recoActivities = new ArrayList<String>(Arrays.asList("obv unlocked", "Suggested species name",
			"obv locked", "Agreed on species name", "Suggestion removed"));

	// OBSERVATION ACTIVITY LIST
	List<String> obvNullActivityList = new ArrayList<String>(
			Arrays.asList("Observation created", "Observation updated", "Rated media resource", "Observation Deleted"));

	List<String> obvRecommendationActivityList = new ArrayList<String>(
			Arrays.asList("obv unlocked", "Suggested species name", "obv locked", "Agreed on species name"));

	List<String> obvUserGroupActivityList = new ArrayList<String>(
			Arrays.asList("Posted resource", "Removed resoruce", "Featured", "UnFeatured"));

	List<String> obvTraitsActivityList = new ArrayList<String>(Arrays.asList("Updated fact", "Added a fact"));

	List<String> obvFlagActivityList = new ArrayList<String>(Arrays.asList("Flag removed", "Flagged"));

	List<String> commentActivityList = new ArrayList<>(Arrays.asList(newComment));

	List<String> observationActivityList = new ArrayList<String>(Arrays.asList("Featured", "Suggestion removed",
			"Observation tag updated", "Custom field edited", "UnFeatured", "Observation species group updated"));

	// USERGROUP ACTIVITY LIST

	List<String> ugNullActivityList = new ArrayList<String>(Arrays.asList("Group updated", "Group created"));

	List<String> userGroupActivityList = new ArrayList<String>(Arrays.asList("Removed resoruce", "Posted resource"));

	List<String> ugUserActivityList = new ArrayList<String>(Arrays.asList("Joined group", "Left Group", "Role updated",
			"Invitation Sent", "Removed user", "Requested Join"));

	List<String> ugCustomFieldActivityList = new ArrayList<String>(
			Arrays.asList("Added Custom Field", "Removed Custom Field"));

	List<String> ugFilterRuleActivityList = new ArrayList<String>(
			Arrays.asList("Added Filter Rule", "Removed Filter Rule", "Disabled Filter Rule", "Enabled Filter Rule"));

	// DOCUMENT ACTIVITY LIST

	List<String> docNullActivityList = new ArrayList<String>(
			Arrays.asList("Document created", "Document updated", "Document Deleted"));

	List<String> docFlagActivityList = new ArrayList<String>(Arrays.asList("Flag removed", "Flagged"));

	List<String> docUserGroupActivityList = new ArrayList<String>(
			Arrays.asList("Featured", "Posted resource", "Removed resoruce", "UnFeatured"));

	List<String> documentActivityList = new ArrayList<String>(
			Arrays.asList("Document tag updated", "Featured", "UnFeatured"));

	List<String> docCommentActivityList = new ArrayList<>(Arrays.asList(newComment));

	// SPECIES ACTIVITY LIST

	List<String> speciesNullActivityList = new ArrayList<String>(Arrays.asList("Created species", "Deleted species"));

	List<String> speciesSynonymActivityList = new ArrayList<String>(
			Arrays.asList("Added synonym", "Updated synonym", "Deleted synonym"));

	List<String> speciesCommonNameActivityList = new ArrayList<String>(
			Arrays.asList("Added common name", "Updated common name", "Deleted common name"));

	List<String> speciesActivityList = new ArrayList<String>(
			Arrays.asList("Featured", "UnFeatured", "Updated species gallery"));

	List<String> speciesFieldActivityList = new ArrayList<String>(
			Arrays.asList("Added species field", "Updated species field", "Deleted species field"));

	List<String> speciesTaxonomyActivityList = new ArrayList<String>(
			Arrays.asList("Added hierarchy", "Deleted hierarchy"));

	List<String> speciesTraitActivityList = new ArrayList<String>(Arrays.asList("Added a fact", "Updated fact"));

	List<String> speciesCommentActivityList = new ArrayList<>(Arrays.asList(newComment));

	List<String> speciesUserGroupActivityList = new ArrayList<String>(
			Arrays.asList("Featured", "UnFeatured", "Posted resource", "Removed resoruce"));

	// TAXONOMY ACTIVTY LIST

	List<String> taxonomyNullActivityList = new ArrayList<String>(
			Arrays.asList("Deleted synonym", "Deleted common name"));

	List<String> taxonomyCommonNameActivityList = new ArrayList<String>(
			Arrays.asList("Added common name", "Updated common name"));

	List<String> taxonomyTaxDefActivityList = new ArrayList<String>(
			Arrays.asList("Taxon name updated", "Taxon created", "Taxon position updated", "Taxon status updated"));

	List<String> taxonomySynonymActivityList = new ArrayList<String>(Arrays.asList("Added synonym", "Updated synonym"));

	List<String> taxonomyCommentActivityList = new ArrayList<>(Arrays.asList(newComment));

	// DATATABLE ACTIVITY LIST

	List<String> dataTableNullActivityList = new ArrayList<String>(
			Arrays.asList("Datatable created", "Datatable updated", "Datatable deleted"));

	List<String> dataTableUserGroupActivityList = new ArrayList<String>(
			Arrays.asList("Posted resource", "Removed resoruce", "Featured", "UnFeatured"));

	List<String> dataTableCommentActivityList = new ArrayList<>(Arrays.asList(newComment));

	List<String> pageNullActivityList = new ArrayList<String>(
			Arrays.asList("Page created", "Page updated", "Page Deleted"));

	List<String> pageCommentActivityList = new ArrayList<>(Arrays.asList(newComment));

	// CCA ACTIVITY LIST
	List<String> ccaTemplateActivityList = new ArrayList<>(Arrays.asList("Template created", "Template updated",
			"Field created", "Field updated", "Field deleted", "CCA Template Deleted"));
	List<String> ccaDataActivityList = new ArrayList<>(Arrays.asList("Data created", "Data updated", "Data deleted",
			"Permission added", "Permission removed", "Follower added", "Follower removed"));
	List<String> ccaCommentActivityList = new ArrayList<>(Arrays.asList(newComment, "Data comment", templateComment));

	List<String> ccaDataUserGroupActivityList = new ArrayList<>(
			Arrays.asList("Posted resource", "Removed resource", "Featured", "UnFeatured"));

	@Override
	public Integer activityCount(String objectType, Long objectId) {
		if (objectType.equalsIgnoreCase("observation"))
			objectType = ActivityEnums.OBSERVATION.getValue();
		Integer count = activityDao.findActivityCountByObjectId(objectType, objectId);
		return count;
	}

	@Override
	public ActivityResult fetchActivityIbp(String objectType, Long objectId, String offset, String limit) {

		if (objectType.equalsIgnoreCase("observation"))
			objectType = ActivityEnums.OBSERVATION.getValue();
		else if (objectType.equalsIgnoreCase("document"))
			objectType = ActivityEnums.DOCUMENT.getValue();
		else if (objectType.equalsIgnoreCase("usergroup"))
			objectType = ActivityEnums.USERGROUP.getValue();
		else if (objectType.equalsIgnoreCase("species"))
			objectType = ActivityEnums.SPECIES.getValue();
		else if (objectType.equalsIgnoreCase("taxonomy"))
			objectType = ActivityEnums.TAXONOMYDEFINITION.getValue();
		else if (objectType.equalsIgnoreCase("datatable"))
			objectType = ActivityEnums.DATATABLE.getValue();
		else if (objectType.equalsIgnoreCase("ccaData"))
			objectType = ActivityEnums.CCADATA.getValue();
		else if (objectType.equalsIgnoreCase("page"))
			objectType = ActivityEnums.PAGE.getValue();
		else if (objectType.equalsIgnoreCase("ccaTemplate"))
			objectType = ActivityEnums.CCATEMPLATE.getValue();

		List<ShowActivityIbp> ibpActivity = new ArrayList<ShowActivityIbp>();
		Integer commentCount = 0;
		ActivityResult activityResult = null;

		try {
			List<Activity> activites = activityDao.findByObjectId(objectType, objectId, offset, limit);
			commentCount = activityDao.findCommentCount(objectType, objectId);
			for (Activity activity : activites) {

				UserGroupActivity ugActivity = null;
				RecoVoteActivity recoVoteActivity = null;
				Comments comment = null;
				Comments reply = null;
				CommentsIbp commentIbp = null;
				CommentsIbp replyIbp = null;
				ActivityIbp activityIbp = null;

				if (commentActivityList.contains(activity.getActivityType())
						&& activity.getActivityHolderId() != null) {

					if (activity.getActivityHolderId().equals(activity.getSubRootHolderId())) {
						comment = commentsDao.findById(activity.getActivityHolderId());
						commentIbp = new CommentsIbp(comment.getId(), comment.getBody());

					} else {
						reply = commentsDao.findById(activity.getSubRootHolderId());
						comment = commentsDao.findById(activity.getActivityHolderId());
						replyIbp = new CommentsIbp(comment.getId(), comment.getBody());
						commentIbp = new CommentsIbp(reply.getId(), reply.getBody());
					}

				} else if (userGroupActivities.contains(activity.getActivityType())) {
					String description = activity.getActivityDescription();
					ugActivity = objectMapper.readValue(description, UserGroupActivity.class);
				} else if (recoActivities.contains(activity.getActivityType())) {
					String description = activity.getActivityDescription();
					recoVoteActivity = objectMapper.readValue(description, RecoVoteActivity.class);

				}
				activityIbp = new ActivityIbp(activity.getActivityDescription(), activity.getActivityType(),
						activity.getDateCreated(), activity.getLastUpdated());

				UserIbp user = userService.getUserIbp(activity.getAuthorId().toString());

				ibpActivity.add(
						new ShowActivityIbp(activityIbp, commentIbp, replyIbp, ugActivity, recoVoteActivity, user));

			}
			activityResult = new ActivityResult(ibpActivity, commentCount);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return activityResult;
	}

	// OBSERVATION ACTIVITY LOGGING

	@Override
	public Activity logActivities(HttpServletRequest request, Long userId, ActivityLoggingData loggingData) {
		Activity activity = null;
		MAIL_TYPE type = null;

		Boolean isUsergroupFeatured = false;
		isUsergroupFeatured = checkUserGroupFeatured(loggingData.getActivityType(),
				loggingData.getActivityDescription());

		if (obvNullActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, null, null, null, loggingData.getActivityType(), userId, new Date(),
					new Date(), loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), true);

		} else if (obvRecommendationActivityList.contains(loggingData.getActivityType())) {

			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.RECOMMENDATIONVOTE.getValue(), loggingData.getActivityType(), userId, new Date(),
					new Date(), loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), true);
		} else if (obvUserGroupActivityList.contains(loggingData.getActivityType()) && isUsergroupFeatured) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.USERGROUP.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), loggingData.getRootObjectId(),
					ActivityEnums.OBSERVATION.getValue(), true);
		} else if (obvTraitsActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.FACTS.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), loggingData.getRootObjectId(),
					ActivityEnums.OBSERVATION.getValue(), true);
		} else if (obvFlagActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.FLAG.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), loggingData.getRootObjectId(),
					ActivityEnums.OBSERVATION.getValue(), true);

		} else if (observationActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.OBSERVATION.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(), loggingData.getRootObjectId(),
					ActivityEnums.OBSERVATION.getValue(), true);
		} else if (commentActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(),
					loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
		} else if (deletedComment.equalsIgnoreCase(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.OBSERVATION.getValue(),
					loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
		}

		if (activity != null) {
			activity = activityDao.save(activity);
			try {
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				userService.updateFollow("observation", loggingData.getRootObjectId().toString());
				if (loggingData.getMailData() != null) {
					Map<String, Object> data = ActivityUtil.getMailType(activity.getActivityType(), loggingData);
					type = (MAIL_TYPE) data.get("type");
					if (type != null && type != MAIL_TYPE.COMMENT_POST) {
						MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
								loggingData.getActivityDescription(), loggingData.getMailData());
						mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId,
								null, mailActivityData, null, null);
						notificationSevice.sendNotification(mailActivityData, activity.getRootHolderType(),
								activity.getRootHolderId(), siteName, data.get("text").toString());
					}
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		return activity;

	}

	@Override
	public String sendObvCreateMail(Long userId, ActivityLoggingData loggingData) {
		try {
			MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
					loggingData.getActivityDescription(), loggingData.getMailData());
			mailService.sendMail(MAIL_TYPE.OBSERVATION_ADDED, ActivityEnums.OBSERVATION.getValue(),
					loggingData.getRootObjectId(), userId, null, mailActivityData, null, null);
			notificationSevice.sendNotification(mailActivityData, ActivityEnums.OBSERVATION.getValue(),
					loggingData.getRootObjectId(), siteName, "Observation created");
			return "Mail Sent";
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "Mail not sent";
	}

	@Override
	public Activity addComment(HttpServletRequest request, Long userId, String commentType,
			CommentLoggingData commentData) {

		if (commentData.getSubRootHolderId() == null) {
			commentData.setSubRootHolderId(commentData.getRootHolderId());
			commentData.setSubRootHolderType(commentData.getRootHolderType());
		}
		commentData.setSubRootHolderType(
				ActivityEnums.valueOf(commentData.getSubRootHolderType().toUpperCase()).getValue());
		commentData.setRootHolderType(ActivityEnums.valueOf(commentData.getRootHolderType().toUpperCase()).getValue());
		Comments comment = null;
		if (commentData.getRootHolderId().equals(commentData.getSubRootHolderId())) {
			comment = new Comments(null, userId, commentData.getBody(), commentData.getSubRootHolderId(),
					commentData.getSubRootHolderType(), new Date(), new Date(), commentData.getRootHolderId(),
					commentData.getRootHolderType(), null, null,
					commentData.getLanguageId() != null ? commentData.getLanguageId() : defaultLanguageId, false);

		} else {
			comment = new Comments(null, userId, commentData.getBody(), commentData.getSubRootHolderId(),
					commentData.getSubRootHolderType(), new Date(), new Date(), commentData.getRootHolderId(),
					commentData.getRootHolderType(), commentData.getSubRootHolderId(), commentData.getSubRootHolderId(),
					commentData.getLanguageId() != null ? commentData.getLanguageId() : defaultLanguageId, false);

		}

		Comments result = commentsDao.save(comment);
		OBJECT_TYPE objectType = null;
		Activity activityResult = null;
		if (commentType.equals("observation")) {
			ActivityLoggingData activity = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				activity = new ActivityLoggingData(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			} else {
				activity = new ActivityLoggingData(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			}
			activityResult = logActivities(request, userId, activity);

		} else if (commentType.equals("datatable")) {
			objectType = OBJECT_TYPE.DATATABLE;
			DatatableActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new DatatableActivityLogging(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());

			} else {
				loggingData = new DatatableActivityLogging(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			}

			activityResult = logDatatableActivities(request, userId, loggingData);
		} else if (commentType.equals("page")) {

			PageAcitvityLogging loggingData = null;

			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new PageAcitvityLogging(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());

			} else {
				loggingData = new PageAcitvityLogging(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			}

			activityResult = logPageActivities(request, userId, loggingData);

		}

		else if (commentType.equals("document")) {

			DocumentActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new DocumentActivityLogging(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());

			} else {
				loggingData = new DocumentActivityLogging(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			}

			activityResult = logDocActivities(request, userId, loggingData);
		} else if (commentType.equalsIgnoreCase("species")) {
			SpeciesActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new SpeciesActivityLogging(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			} else {
				loggingData = new SpeciesActivityLogging(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment, commentData.getMailData());
			}
			activityResult = logSpeciesActivities(request, userId, loggingData);
		} else if (commentType.equalsIgnoreCase("taxonomy")) {
			TaxonomyActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new TaxonomyActivityLogging(null, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), newComment);
			} else {
				loggingData = new TaxonomyActivityLogging(null, result.getRootHolderId(), result.getCommentHolderId(),
						result.getRootHolderType(), result.getId(), newComment);
			}
			activityResult = logTaxonomyActivities(request, userId, loggingData);
		} else if (commentType.equalsIgnoreCase("cca")) {
			objectType = OBJECT_TYPE.CCA;
			CCAActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new CCAActivityLogging(commentData.getBody(), result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(),
						result.getRootHolderType().equalsIgnoreCase("cca.Template") ? templateComment : newComment,
						commentData.getMailData());
			} else {
				loggingData = new CCAActivityLogging(commentData.getBody(), result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(),
						result.getRootHolderType().equalsIgnoreCase("cca.Template") ? templateComment : newComment,
						commentData.getMailData());
			}
			activityResult = logCCAActivities(request, userId, loggingData);
		}

		if (activityResult != null && commentData.getMailData() != null && !commentType.equals("cca")) {

			MailActivityData mailActivityData = new MailActivityData(newComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());

			if (commentType.equals("document")) {
				objectType = OBJECT_TYPE.DOCUMENT;
				mailService.sendMail(MAIL_TYPE.DOCUMENT_COMMENT_POST, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers,
						objectType);

			} else if (commentType.equals("page")) {
				mailService.sendMail(MAIL_TYPE.PAGE_COMMENT_POST, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers,
						objectType);
			} else if (commentType.equals("species")) {
				objectType = OBJECT_TYPE.SPECIES;
				mailService.sendMail(MAIL_TYPE.SPECIES_COMMENT_POST, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers,
						objectType);
			} else if (commentType.equals("datatable")) {
				mailService.sendMail(MAIL_TYPE.DATATABLE_COMMENT_POST, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, null);
			} else if (commentType.equals("observation")) {
				objectType = OBJECT_TYPE.OBSERVATION;
				mailService.sendMail(MAIL_TYPE.COMMENT_POST, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers,
						objectType);
				notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(),
						result.getRootHolderId(), siteName, mailActivityData.getActivityType());
			}

			if (!taggedUsers.isEmpty()) {
				mailService.sendMail(MAIL_TYPE.TAGGED_MAIL, activityResult.getRootHolderType(),
						activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers,
						objectType);
			}

		}

		return activityResult;
	}

	public Activity removeComment(HttpServletRequest request, Long userId, String commentType,
			CommentLoggingData commentData, String commentId) {

		Comments comment = commentsDao.findById(Long.parseLong(commentId));
		comment.setIsDeleted(true);
		Comments result = commentsDao.update(comment);

		Activity activityResult = null;

		if (commentType.equals("observation")) {
			ActivityLoggingData activity = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				activity = new ActivityLoggingData(deletedComment, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), deletedComment, commentData.getMailData());
			} else {
				activity = new ActivityLoggingData(deletedComment, result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(), deletedComment,
						commentData.getMailData());
			}
			activityResult = logActivities(request, userId, activity);

			OBJECT_TYPE objectType = null;

			MailActivityData mailActivityData = new MailActivityData(deletedComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());
			objectType = OBJECT_TYPE.OBSERVATION;
			mailService.sendMail(MAIL_TYPE.DELETED_COMMENT, activityResult.getRootHolderType(),
					activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, objectType);
			notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(), result.getRootHolderId(),
					siteName, mailActivityData.getActivityType());

		} else if (commentType.equals("document")) {

			DocumentActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new DocumentActivityLogging(deletedComment, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), deletedComment, commentData.getMailData());

			} else {
				loggingData = new DocumentActivityLogging(deletedComment, result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(), deletedComment,
						commentData.getMailData());
			}

			activityResult = logDocActivities(request, userId, loggingData);

			OBJECT_TYPE objectType = null;

			MailActivityData mailActivityData = new MailActivityData(deletedComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());
			objectType = OBJECT_TYPE.DOCUMENT;
			mailService.sendMail(MAIL_TYPE.DOCUMENT_COMMENT_DELETE, activityResult.getRootHolderType(),
					activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, objectType);
			notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(), result.getRootHolderId(),
					siteName, mailActivityData.getActivityType());

		} else if (commentType.equals("species")) {

			SpeciesActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new SpeciesActivityLogging(deletedComment, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), deletedComment, commentData.getMailData());
			} else {
				loggingData = new SpeciesActivityLogging(deletedComment, result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(), deletedComment,
						commentData.getMailData());
			}
			activityResult = logSpeciesActivities(request, userId, loggingData);

			OBJECT_TYPE objectType = null;

			MailActivityData mailActivityData = new MailActivityData(deletedComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());
			objectType = OBJECT_TYPE.SPECIES;

			mailService.sendMail(MAIL_TYPE.SPECIES_COMMENT_DELETE, activityResult.getRootHolderType(),
					activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, objectType);
			notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(), result.getRootHolderId(),
					siteName, mailActivityData.getActivityType());

		} else if (commentType.equals("page")) {

			PageAcitvityLogging loggingData = null;

			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new PageAcitvityLogging(deletedComment, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), deletedComment, commentData.getMailData());

			} else {
				loggingData = new PageAcitvityLogging(deletedComment, result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(), deletedComment,
						commentData.getMailData());
			}

			activityResult = logPageActivities(request, userId, loggingData);
			OBJECT_TYPE objectType = null;

			MailActivityData mailActivityData = new MailActivityData(deletedComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());

			mailService.sendMail(MAIL_TYPE.PAGE_COMMENT_DELETE, activityResult.getRootHolderType(),
					activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, objectType);
			notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(), result.getRootHolderId(),
					siteName, mailActivityData.getActivityType());

		} else if (commentType.equals("datatable")) {

			DatatableActivityLogging loggingData = null;
			if (result.getCommentHolderId().equals(result.getRootHolderId())) {
				loggingData = new DatatableActivityLogging(deletedComment, result.getRootHolderId(), result.getId(),
						result.getRootHolderType(), result.getId(), deletedComment, commentData.getMailData());
			} else {
				loggingData = new DatatableActivityLogging(deletedComment, result.getRootHolderId(),
						result.getCommentHolderId(), result.getRootHolderType(), result.getId(), deletedComment,
						commentData.getMailData());
			}
			activityResult = logDatatableActivities(request, userId, loggingData);

			OBJECT_TYPE objectType = null;

			MailActivityData mailActivityData = new MailActivityData(deletedComment, null, commentData.getMailData());
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(commentData.getBody());
			objectType = OBJECT_TYPE.DATATABLE;

			mailService.sendMail(MAIL_TYPE.DATATABLE_COMMENT_DELETE, activityResult.getRootHolderType(),
					activityResult.getRootHolderId(), userId, commentData, mailActivityData, taggedUsers, objectType);
			notificationSevice.sendNotification(mailActivityData, result.getRootHolderType(), result.getRootHolderId(),
					siteName, mailActivityData.getActivityType());

		}

		return activityResult;
	}

	// USERGROUP ACTIVITY LOGGING

	@Override
	public Activity logUGActivities(Long userId, UserGroupActivityLogging loggingData) {

		Activity activity = null;
		if (ugNullActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, null, null, null, loggingData.getActivityType(), userId, new Date(),
					new Date(), loggingData.getRootObjectId(), ActivityEnums.USERGROUP.getValue(),
					loggingData.getSubRootObjectId(), ActivityEnums.USERGROUP.getValue(), true);

		} else if (userGroupActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.USERGROUP.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.USERGROUP.getValue(), loggingData.getSubRootObjectId(),
					ActivityEnums.USERGROUP.getValue(), true);

		} else if (ugUserActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.USER.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.USERGROUP.getValue(), loggingData.getSubRootObjectId(),
					ActivityEnums.USERGROUP.getValue(), true);

		} else if (ugCustomFieldActivityList.contains(loggingData.getActivityType())) {
			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.CUSTOMFIELD.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.USERGROUP.getValue(), loggingData.getSubRootObjectId(),
					ActivityEnums.USERGROUP.getValue(), true);
		} else if (ugFilterRuleActivityList.contains(loggingData.getActivityType())) {

			activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
					ActivityEnums.FILTERRULE.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
					loggingData.getRootObjectId(), ActivityEnums.USERGROUP.getValue(), loggingData.getSubRootObjectId(),
					ActivityEnums.USERGROUP.getValue(), true);
		}

		if (activity != null)
			activity = activityDao.save(activity);

		return activity;
	}

	// DOCUMENT ACTIVITY LOGGING

	@Override
	public Activity logDocActivities(HttpServletRequest request, Long userId, DocumentActivityLogging loggingData) {
		try {
			Activity activity = null;
			Boolean isUsergroupFeatured = false;
			MAIL_TYPE type = null;
			isUsergroupFeatured = checkUserGroupFeatured(loggingData.getActivityType(),
					loggingData.getActivityDescription());

			if (docNullActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, null, null, null, loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DOCUMENT.getValue(), true);

			} else if (docFlagActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.FLAG.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
						loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DOCUMENT.getValue(), true);

			} else if (docCommentActivityList.contains(loggingData.getActivityType())) {

				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);

			} else if (docUserGroupActivityList.contains(loggingData.getActivityType()) && isUsergroupFeatured) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.USERGROUP.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DOCUMENT.getValue(), true);

			} else if (documentActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.DOCUMENT.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DOCUMENT.getValue(), true);

			} else if (deletedComment.equalsIgnoreCase(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DOCUMENT.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			}

			if (activity != null)
				activity = activityDao.save(activity);

			try {
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				userService.updateFollow("document", loggingData.getRootObjectId().toString());
				if (activity != null && loggingData.getMailData() != null) {

					String mailType = docUserGroupActivityList.contains(loggingData.getActivityType())
							|| docFlagActivityList.contains(loggingData.getActivityType())
									? activity.getActivityType() + " document"
									: activity.getActivityType();
					Map<String, Object> data = ActivityUtil.getMailType(mailType,
							new ActivityLoggingData(loggingData.getActivityDescription(), loggingData.getRootObjectId(),
									loggingData.getSubRootObjectId(), loggingData.getRootObjectType(),
									loggingData.getActivityId(), loggingData.getActivityType(),
									loggingData.getMailData()));
					type = (MAIL_TYPE) data.get("type");
					if (type != null && type != MAIL_TYPE.COMMENT_POST) {
						MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
								loggingData.getActivityDescription(), loggingData.getMailData());
						mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId,
								null, mailActivityData, null, null);
					}
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			return activity;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// SPECIES ACTIVITY LOGGING
	@Override
	public Activity logSpeciesActivities(HttpServletRequest request, Long userId, SpeciesActivityLogging loggingData) {
		try {

			Activity activity = null;
			Boolean isUsergroupFeatured = false;
			MAIL_TYPE type = null;
			isUsergroupFeatured = checkUserGroupFeatured(loggingData.getActivityType(),
					loggingData.getActivityDescription());

			if (speciesNullActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), null, null,
						loggingData.getActivityType(), userId, new Date(), new Date(), loggingData.getRootObjectId(),
						ActivityEnums.SPECIES.getValue(), loggingData.getSubRootObjectId(),
						ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesSynonymActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.TAXONOMYDEFINITION.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesCommonNameActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMONNAMES.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesFieldActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.SPECIESFIELD.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesTaxonomyActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.TAXONOMYREGISTRY.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesUserGroupActivityList.contains(loggingData.getActivityType()) && isUsergroupFeatured) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.USERGROUP.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesTraitActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.FACTS.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
						loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (speciesCommentActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			} else if (speciesActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.SPECIES.getValue(), loggingData.getActivityType(), userId, new Date(), new Date(),
						loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.SPECIES.getValue(), true);
			} else if (deletedComment.equalsIgnoreCase(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.SPECIES.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			}

			if (activity != null)
				activity = activityDao.save(activity);

			if (activity != null && loggingData.getMailData() != null) {

				String mailType = speciesUserGroupActivityList.contains(loggingData.getActivityType())
						|| speciesTraitActivityList.contains(loggingData.getActivityType())
								? activity.getActivityType() + " species"
								: activity.getActivityType();
				Map<String, Object> data = ActivityUtil.getMailType(mailType,
						new ActivityLoggingData(loggingData.getActivityDescription(), loggingData.getRootObjectId(),
								loggingData.getSubRootObjectId(), loggingData.getRootObjectType(),
								loggingData.getActivityId(), loggingData.getActivityType(), loggingData.getMailData()));
				type = (MAIL_TYPE) data.get("type");
				if (type != null && type != MAIL_TYPE.COMMENT_POST) {
					MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
							loggingData.getActivityDescription(), loggingData.getMailData());
					mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId, null,
							mailActivityData, null, null);
				}
			}

			return activity;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	// check if its a usergroup featuring or mother portal featuring and also if its
	// usergroup activity
	private Boolean checkUserGroupFeatured(String acitivityType, String description) {
		Boolean result = false;
		try {
			if (acitivityType.equals("Featured") || acitivityType.equals("UnFeatured")) {
				UserGroupActivity ugActivity = objectMapper.readValue(description, UserGroupActivity.class);
				if (ugActivity.getUserGroupId() != null)
					result = true;
			} else if (acitivityType.equals("Posted resource") || acitivityType.equals("Removed resoruce"))
				result = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;

	}

	// TAXONOMY ACTIVITY LOGGING

	@Override
	public Activity logTaxonomyActivities(HttpServletRequest request, Long userId,
			TaxonomyActivityLogging loggingData) {

		try {
			Activity activity = null;

			if (taxonomyNullActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), null, null,
						loggingData.getActivityType(), userId, new Date(), new Date(), loggingData.getRootObjectId(),
						ActivityEnums.TAXONOMYDEFINITION.getValue(), loggingData.getSubRootObjectId(),
						ActivityEnums.TAXONOMYDEFINITION.getValue(), true);

			} else if (taxonomyTaxDefActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.TAXONOMYDEFINITION.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(), true);

			} else if (taxonomyCommonNameActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMONNAMES.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(), true);

			} else if (taxonomySynonymActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.SYNONYMS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(), true);

			} else if (taxonomyCommentActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.TAXONOMYDEFINITION.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			}
			if (activity != null)
				activity = activityDao.save(activity);

			// TODO mailData integration

			return activity;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Activity logCCAActivities(HttpServletRequest request, Long userId, CCAActivityLogging ccaActivityLogging) {
		try {
			Activity activity = null;
			MAIL_TYPE type = null;
			List<TaggedUser> taggedUsers = ActivityUtil.getTaggedUsers(ccaActivityLogging.getActivityDescription());

			if (ccaTemplateActivityList.contains(ccaActivityLogging.getActivityType())) {
				activity = new Activity(null, ccaActivityLogging.getActivityDescription(),
						ccaActivityLogging.getActivityId(), ActivityEnums.CCATEMPLATE.getValue(),
						ccaActivityLogging.getActivityType(), userId, new Date(), new Date(),
						ccaActivityLogging.getRootObjectId(), ActivityEnums.CCATEMPLATE.getValue(),
						ccaActivityLogging.getSubRootObjectId(), ActivityEnums.CCATEMPLATE.getValue(), true);

			} else if (ccaDataActivityList.contains(ccaActivityLogging.getActivityType())) {
				String desc = ccaActivityLogging.getActivityDescription();
				if (ccaActivityLogging.getActivityType().contains("Follower")) {
					User u = userService.getUser(desc.substring(1, desc.length() - 1));
					desc = u.getName();
				}

				activity = new Activity(null, desc, ccaActivityLogging.getActivityId(),
						ActivityEnums.CCADATA.getValue(), ccaActivityLogging.getActivityType(), userId, new Date(),
						new Date(), ccaActivityLogging.getRootObjectId(), ActivityEnums.CCADATA.getValue(),
						ccaActivityLogging.getSubRootObjectId(), ActivityEnums.CCADATA.getValue(), true);

			} else if (ccaCommentActivityList.contains(ccaActivityLogging.getActivityType())) {
				activity = new Activity(null, ccaActivityLogging.getActivityDescription(),
						ccaActivityLogging.getActivityId(), ActivityEnums.COMMENTS.getValue(),
						ccaActivityLogging.getActivityType(), userId, new Date(), new Date(),
						ccaActivityLogging.getRootObjectId(), ccaActivityLogging.getRootObjectType(),
						ccaActivityLogging.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			} else if (ccaDataUserGroupActivityList.contains(ccaActivityLogging.getActivityType())) {
				activity = new Activity(null, ccaActivityLogging.getActivityDescription(),
						ccaActivityLogging.getActivityId(), ActivityEnums.USERGROUP.getValue(),
						ccaActivityLogging.getActivityType(), userId, new Date(), new Date(),
						ccaActivityLogging.getRootObjectId(), ActivityEnums.CCADATA.getValue(),
						ccaActivityLogging.getRootObjectId(), ActivityEnums.CCADATA.getValue(), true);
			}
			if (activity != null) {
				activity = activityDao.save(activity);
				try {
					userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
					userService.updateFollow("CCA", ccaActivityLogging.getRootObjectId().toString());
					Map<String, Object> data = ActivityUtil.getCCAMailType(activity.getActivityType(),
							ccaActivityLogging);
					type = (MAIL_TYPE) data.get("type");
					if (ccaActivityLogging.getMailData() != null && type != null && type != MAIL_TYPE.COMMENT_POST) {
						MailActivityData mailActivityData = new MailActivityData(ccaActivityLogging.getActivityType(),
								ccaActivityLogging.getActivityDescription(), ccaActivityLogging.getMailData());
						mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId,
								null, mailActivityData, taggedUsers, OBJECT_TYPE.CCA);
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}

			return activity;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Activity logDatatableActivities(HttpServletRequest request, Long userId,
			DatatableActivityLogging loggingData) {
		Activity activity = null;
		MAIL_TYPE type = null;
		try {

			if (dataTableNullActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), null, null,
						loggingData.getActivityType(), loggingData.getMailData().getDataTableMailData().getAuthorId(),
						new Date(), new Date(), loggingData.getRootObjectId(), ActivityEnums.DATATABLE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DATATABLE.getValue(), true);
			} else if (dataTableUserGroupActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.USERGROUP.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DATATABLE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.DATATABLE.getValue(), true);
			} else if (dataTableCommentActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DATATABLE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			} else if (deletedComment.equalsIgnoreCase(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.DATATABLE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			}

			if (activity != null)
				activity = activityDao.save(activity);

			if (activity != null && loggingData.getMailData() != null) {

				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				userService.updateFollow("content.eml.Datatable", loggingData.getRootObjectId().toString());

				String mailType = dataTableUserGroupActivityList.contains(loggingData.getActivityType())
						? activity.getActivityType() + " Datatable"
						: activity.getActivityType();

				Map<String, Object> data = ActivityUtil.getMailType(mailType,
						new ActivityLoggingData(loggingData.getActivityDescription(), loggingData.getRootObjectId(),
								loggingData.getSubRootObjectId(), loggingData.getRootObjectType(),
								loggingData.getActivityId(), loggingData.getActivityType(), loggingData.getMailData()));
				type = (MAIL_TYPE) data.get("type");
				if (type != null && type != MAIL_TYPE.COMMENT_POST) {
					MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
							loggingData.getActivityDescription(), loggingData.getMailData());
					mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId, null,
							mailActivityData, null, null);
				}
			}

			return activity;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Activity logPageActivities(HttpServletRequest request, Long userId, PageAcitvityLogging loggingData) {

		Activity activity = null;
		MAIL_TYPE type = null;
		try {
			if (pageNullActivityList.contains(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), null, null,
						loggingData.getActivityType(), userId, new Date(), new Date(), loggingData.getRootObjectId(),
						ActivityEnums.PAGE.getValue(), loggingData.getSubRootObjectId(), ActivityEnums.PAGE.getValue(),
						true);
			} else if (pageCommentActivityList.contains(loggingData.getActivityType())) {

				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.PAGE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			} else if (deletedComment.equalsIgnoreCase(loggingData.getActivityType())) {
				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityId(),
						ActivityEnums.COMMENTS.getValue(), loggingData.getActivityType(), userId, new Date(),
						new Date(), loggingData.getRootObjectId(), ActivityEnums.PAGE.getValue(),
						loggingData.getSubRootObjectId(), ActivityEnums.COMMENTS.getValue(), true);
			}

			if (activity != null)
				activity = activityDao.save(activity);

			if (activity != null && loggingData.getMailData() != null) {

				Map<String, Object> data = ActivityUtil.getMailType(activity.getActivityType(),
						new ActivityLoggingData(loggingData.getActivityDescription(), loggingData.getRootObjectId(),
								loggingData.getSubRootObjectId(), loggingData.getRootObjectType(),
								loggingData.getActivityId(), loggingData.getActivityType(), loggingData.getMailData()));
				type = (MAIL_TYPE) data.get("type");
				if (type != null && type != MAIL_TYPE.COMMENT_POST) {
					MailActivityData mailActivityData = new MailActivityData(loggingData.getActivityType(),
							loggingData.getActivityDescription(), loggingData.getMailData());
					mailService.sendMail(type, activity.getRootHolderType(), activity.getRootHolderId(), userId, null,
							mailActivityData, null, null);
				}
			}

			return activity;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean checkCCARequest(CcaPermission permissionReq) {
		CCARoles role = CCARoles.valueOf(permissionReq.getRole().replace(" ", ""));
		CcaPermission alreadyExist = ccaPermissionDao.requestPermissionExist(permissionReq.getRequestorId(),
				permissionReq.getCcaid(), role);

		// check for already existing request
		if (alreadyExist == null) {
			CcaPermission permission = new CcaPermission(null, permissionReq.getRequestorId(),
					permissionReq.getOwnerId(), permissionReq.getCcaid(), permissionReq.getRole(), new Date(),
					permissionReq.getShortName(), null, null);
			ccaPermissionDao.save(permission);
			sendCCAPermisionMail(permissionReq);
			return true;
		}
		// Get the time difference between permissions
		else {
			long daylimit = 3;
			long timeDifference = new Date().getTime() - permissionReq.getRequestedOn().getTime();
			long days = TimeUnit.DAYS.convert(timeDifference, TimeUnit.MILLISECONDS);

			if (days >= daylimit) {
				CcaPermission permission = ccaPermissionDao.findById(alreadyExist.getId());
				permission.setRequestedOn(new Date());
				ccaPermissionDao.update(permission);
				sendCCAPermisionMail(permissionReq);
				return true;
			}

		}
		return false;

	}

	public Boolean sendCCAPermisionMail(CcaPermission permissionReq) {

		try {
			String encryptedKey = permissionReq.getEncryptKey();
			User requestor = userService.getUser(permissionReq.getRequestorId().toString());
			User owner = userService.getUser(permissionReq.getOwnerId().toString());
			List<User> requestee = Arrays.asList(owner);

			Long ccaId = permissionReq.getCcaid();
			String ccaName = permissionReq.getShortName();
			String role = permissionReq.getRole();
			Map<String, Object> summaryData = permissionReq.getData();
			String requestorMessage = permissionReq.getRequestorMessage();

			ccaMailutils.sendPermissionRequest(requestee, ccaName, ccaId, role, requestor, encryptedKey, summaryData,
					requestorMessage);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public Boolean sendDownloadLink(String authorId, String fileName, String type) {
		ccaMailutils.sendMail(authorId, fileName, type);
		return true;
	}

	@Override
	public Boolean odkUserMail(ODKMailData odkMail) {

		try {
			String userId = odkMail.getsUserId();
			String passWord = odkMail.getPassword();
			String role = odkMail.getRole();
			String projectId = odkMail.getProjectId();
			String projectName = odkMail.getProjectName();

			odkMailutils.sendOdkUserMail(userId, passWord, role, projectId, projectName);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public Activity logCropcertActivities(HttpServletRequest request, Activity loggingData) {
		Activity activity = null;
		try {
			if (isValidCropcertActivityType(loggingData.getActivityType())) {

				Date dateCreated = loggingData.getDateCreated() != null ? loggingData.getDateCreated() : new Date();
				Date lastUpdated = loggingData.getLastUpdated() != null ? loggingData.getLastUpdated() : new Date();

				activity = new Activity(null, loggingData.getActivityDescription(), loggingData.getActivityHolderId(),
						loggingData.getActivityHolderType(), loggingData.getActivityType(), loggingData.getAuthorId(),
						dateCreated, lastUpdated, loggingData.getRootHolderId(), loggingData.getRootHolderType(),
						loggingData.getSubRootHolderId(), loggingData.getSubRootHolderType(), true);
			}
			if (activity != null) {
				activity = activityDao.save(activity);
				return activity;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private boolean isValidCropcertActivityType(String activityType) {
		Set<String> allowedActivityTypes = new HashSet<>(
				Arrays.asList("BATCH_CREATION", "BATCH_UPDATE", "FARMER_CREATION", "FARMER_UPDATE", "FARMER_DELETION",
						"LOT_CREATION", "LOT_UPDATE", "FARMER_PRODUCE_CREATED", "FARMER_PRODUCE", "CONTAINER_CREATION",
						"CONTAINER_UPDATE", "CONTAINER_DELETION"));
		return allowedActivityTypes.contains(activityType);
	}

}
