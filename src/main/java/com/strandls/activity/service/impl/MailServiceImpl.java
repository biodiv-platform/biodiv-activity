package com.strandls.activity.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.RabbitMqConnection;
import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.DocumentMailData;
import com.strandls.activity.pojo.MailActivityData;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.RecoVoteActivity;
import com.strandls.activity.pojo.SpeciesMailData;
import com.strandls.activity.pojo.TaggedUser;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.pojo.UserGroupMailData;
import com.strandls.activity.pojo.ObservationMailData;
import com.strandls.activity.service.MailService;
import com.strandls.activity.util.ActivityUtil;
import com.strandls.mail_utility.model.EnumModel.COMMENT_POST;
import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INFO_FIELDS;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.POST_TO_GROUP;
import com.strandls.mail_utility.model.EnumModel.SUGGEST_MAIL;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.Recipients;
import com.strandls.user.pojo.User;

public class MailServiceImpl implements MailService {

	private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	private String siteName = "";
	private String serverUrl = "";

	@Inject
	private RabbitMQProducer producer;

	@Inject
	private UserServiceApi userService;

	@Inject
	private ObjectMapper mapper;

	List<String> recommendationActivityList = new ArrayList<String>(
			Arrays.asList("obv unlocked", "Suggested species name", "obv locked", "Agreed on species name"));

	List<String> userGroupActivityList = new ArrayList<String>(
			Arrays.asList("Posted resource", "Removed resoruce", "Featured", "UnFeatured"));

	public MailServiceImpl() {
		Properties props = PropertyFileUtil.fetchProperty("config.properties");
		siteName = props.getProperty("siteName");
		serverUrl = props.getProperty("serverUrl");
		serverUrl = props.getProperty("serverUrl");
	}

	@Override
	public void sendMail(MAIL_TYPE type, String objectType, Long objectId, Long userId, CommentLoggingData comment,
			MailActivityData activity, List<TaggedUser> taggedUsers) {
		try {
			List<Recipients> recipientsList = userService.getRecipients(objectType, objectId);
			System.out.println("*******The object type and object id *********" + objectType + "   " + objectId);
			System.out.println("*******Recipient Data ********" + recipientsList.size());
			ObservationMailData observation = activity.getMailData().getObservationData();
			DocumentMailData document = activity.getMailData().getDocumentMailData();
			List<UserGroupMailData> groups = activity.getMailData().getUserGroupData();
			User who = userService.getUser(String.valueOf(userId));
			RecoVoteActivity reco = null;
			UserGroupActivity userGroup = null;
			String name = "";

			if (recipientsList.isEmpty()) {
				Recipients recipient = new Recipients();
				recipient.setEmail(who.getEmail());
				recipient.setIsSubscribed(who.getSendNotification());
				recipient.setId(who.getId());
				recipient.setName(who.getName());
				recipient.setTokens(null);
				recipientsList.add(recipient);
			}
			if (recommendationActivityList.contains(activity.getActivityType())
					|| activity.getActivityType().equalsIgnoreCase("suggestion removed")) {
				reco = mapper.readValue(activity.getActivityDescription(), RecoVoteActivity.class);

				name = (reco.getScientificName() != null && !reco.getScientificName().isEmpty())
						? reco.getScientificName()
						: (reco.getCommonName() != null && !reco.getCommonName().isEmpty()) ? reco.getCommonName()
								: (reco.getGivenName() != null && !reco.getGivenName().isEmpty()) ? reco.getGivenName()
										: "";
			}
			if (userGroupActivityList.contains(activity.getActivityType())) {
				userGroup = mapper.readValue(activity.getActivityDescription(), UserGroupActivity.class);
				System.out.println("***** UserGroup ***** " + userGroup.getUserGroupName());
			}

			System.out.println("INSIDE MAIL SERVICE IMPL");

			if (groups != null && !groups.isEmpty()) {
				for (UserGroupMailData mailData : groups) {
					System.out.println("***** GroupFromAPI *****" + mailData.toString());
				}
			} else {
				System.out.println("***** Groups Empty *****");
			}

			Map<String, Object> data = null;
			String linkTaggedUsers = "";
			if (type == MAIL_TYPE.COMMENT_POST && taggedUsers != null) {
				linkTaggedUsers = ActivityUtil.linkTaggedUsersProfile(taggedUsers, comment.getBody(), true);
			}
			List<Map<String, Object>> mailDataList = new ArrayList<>();
			if (type == MAIL_TYPE.TAGGED_MAIL && taggedUsers != null && !taggedUsers.isEmpty()) {
				for (TaggedUser user : taggedUsers) {
					User follower = userService.getUser(String.valueOf(user.getId()));
					Recipients recipient = new Recipients();
					recipient.setId(follower.getId());
					recipient.setIsSubscribed(follower.getSendNotification());
					data = prepareMailData(type, recipient, follower, who, reco, userGroup, activity, comment, name,
							observation, groups, linkTaggedUsers!=null && !linkTaggedUsers.isEmpty() ?linkTaggedUsers:
								comment!= null && !comment.getBody().isEmpty()?comment.getBody():"",
							document);
					if (follower.getEmail() != null && !follower.getEmail().isEmpty()) {
						mailDataList.add(data);
					}
				}
			} else {
				for (Recipients recipient : recipientsList) {
					User follower = userService.getUser(String.valueOf(recipient.getId()));
					data = prepareMailData(type, recipient, follower, who, reco, userGroup, activity, comment, name,
							observation, groups, linkTaggedUsers!=null && !linkTaggedUsers.isEmpty() ?linkTaggedUsers:
								comment!= null && !comment.getBody().isEmpty()?comment.getBody():"",
							document);
					if (recipient.getEmail() != null && !recipient.getEmail().isEmpty()) {
						mailDataList.add(data);
					}
				}
			}
			Map<String, Object> mailData = new HashMap<String, Object>();
			mailData.put(INFO_FIELDS.TYPE.getAction(), type.getAction());
			mailData.put(INFO_FIELDS.RECIPIENTS.getAction(), mailDataList);
			System.out.println("\n\n ***** \n\n" + mailData + "\n\n ***** \n\n");
			producer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
					JsonUtil.mapToJSON(mailData));
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private Map<String, Object> prepareMailData(MAIL_TYPE type, Recipients recipient, User follower, User who,
			RecoVoteActivity reco, UserGroupActivity userGroup, MailActivityData activity, CommentLoggingData comment,
			String name, ObservationMailData observation, List<UserGroupMailData> groups, String modifiedComment,
			DocumentMailData document) {
		Map<String, Object> data = new HashMap<String, Object>();
		ObservationMailData observation = mailData.getObservationData();
		DocumentMailData document = mailData.getDocumentMailData();
		SpeciesMailData species = mailData.getSpeciesData();
		data.put(FIELDS.TYPE.getAction(), type.getAction());
		data.put(FIELDS.TO.getAction(), new String[] { recipient.getEmail() });
		data.put(FIELDS.SUBSCRIPTION.getAction(), recipient.getIsSubscribed());
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(COMMENT_POST.TYPE.getAction(), type.getAction());
		model.put(COMMENT_POST.SITENAME.getAction(), siteName);
		model.put(COMMENT_POST.SERVER_URL.getAction(), serverUrl);
		model.put(SUGGEST_MAIL.RECO_VOTE.getAction(), name);

		if (comment != null) {
			model.put(COMMENT_POST.COMMENT_BODY.getAction(), modifiedComment);
		}

		if (type == MAIL_TYPE.FACT_ADDED || type == MAIL_TYPE.FACT_UPDATED || type == MAIL_TYPE.TAG_UPDATED
				|| type == MAIL_TYPE.SPECIES_FIELD_DELETED || type == MAIL_TYPE.SPECIES_FIELD_ADDED
				|| type == MAIL_TYPE.CUSTOM_FIELD_UPDATED ||
				type == MAIL_TYPE.OBSERVATION_FLAGGED || type == MAIL_TYPE.SPECIES_FACT
				|| type == MAIL_TYPE.SPECIES_SYNONYMS || type == MAIL_TYPE.SPECIES_COMMON_NAME
				|| type == MAIL_TYPE.SPECIES_FIELD_UPDATED || type == MAIL_TYPE.SPECIES_RESOURCE) {
			model.put(COMMENT_POST.COMMENT_BODY.getAction(),
					ActivityUtil.replaceFlaggedMessage(activity.getActivityDescription()));
		} else if (type == MAIL_TYPE.FEATURED_POST || type == MAIL_TYPE.FEATURED_POST_IBP) {
			model.put(COMMENT_POST.COMMENT_BODY.getAction(), userGroup.getFeatured());
		}

		model.put(COMMENT_POST.FOLLOWER_ID.getAction(), follower.getId());
		model.put(COMMENT_POST.FOLLOWER_NAME.getAction(), follower.getName());
		model.put(COMMENT_POST.WHO_POSTED_ID.getAction(), who.getId());
		String icon = who.getIcon() != null ? who.getIcon() : "";
		if (!icon.isEmpty()) {
			int dot = icon.lastIndexOf(".");
			String fileName = icon.substring(0, dot);
			icon = String.join("_gall_th", fileName, ".jpg");
		}

		model.put(COMMENT_POST.WHO_POSTED_ICON.getAction(), icon.isEmpty() ? "/user_large.png" : icon);
		model.put(COMMENT_POST.WHO_POSTED_NAME.getAction(),
				recipient.getId().equals(who.getId()) ? "You" : who.getName());

		if (reco != null) {
			model.put(SUGGEST_MAIL.GIVEN_NAME_ID.getAction(), reco.getSpeciesId() == null ? 0 : reco.getSpeciesId());
			model.put(SUGGEST_MAIL.GIVEN_NAME_NAME.getAction(), name);
			model.put(SUGGEST_MAIL.GIVEN_NAME_IS_SCIENTIFIC_NAME.getAction(),
					reco.getScientificName() != null && !reco.getScientificName().isEmpty());
		}

		if (species != null) {
			model.put(COMMENT_POST.WHAT_POSTED_ID.getAction(),
					(species != null && species.getSpeciesId() != null) ? species.getSpeciesId() : null);

			model.put(COMMENT_POST.WHAT_POSTED_NAME.getAction(),
					(species != null && species.getSpeciesName() != null) ? species.getSpeciesName() : "Help Identify");

			model.put(COMMENT_POST.WHAT_POSTED_ICON.getAction(),
					(species.getIconUrl() != null && !species.getIconUrl().isEmpty()) ? species.getIconUrl() : null);

			model.put(COMMENT_POST.WHAT_POSTED_SPECIES.getAction(),
					species.getGroup() != null && !species.getGroup().isEmpty() ? species.getGroup() : null);

		}

		if (document != null) {
			model.put(COMMENT_POST.WHAT_POSTED_ID.getAction(),
					(document != null && document.getDocumentId() != null) ? document.getDocumentId() : null);

			model.put(COMMENT_POST.WHAT_POSTED_NAME.getAction(),
					(document != null && document.getTitle() != null) ? document.getTitle() : "Help Identify");
		}

		if (observation != null) {
			model.put(COMMENT_POST.WHAT_POSTED_ID.getAction(),
					(observation != null && observation.getObservationId() != null) ? observation.getObservationId()
							: null);

			model.put(COMMENT_POST.WHAT_POSTED_NAME.getAction(),
					(observation != null && observation.getScientificName() != null
							&& !observation.getScientificName().isEmpty())
									? observation.getScientificName()
									: (observation != null && observation.getCommonName() != null
											&& !observation.getCommonName().isEmpty()) ? observation.getCommonName()
													: "Help Identify");
			model.put(COMMENT_POST.WHAT_POSTED_LOCATION.getAction(),
					observation.getLocation() == null ? "" : observation.getLocation());
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
			String date = ActivityUtil.getFormattedDate(sdf.format(observation.getObservedOn()));
			model.put(COMMENT_POST.WHAT_POSTED_OBSERVED_ON.getAction(), date);
			String image = observation.getIconURl() == null ? "" : observation.getIconURl();
			if (!image.isEmpty()) {
				int dot = image.lastIndexOf(".");
				String fileName = image.substring(0, dot);
				String extension = image.substring(dot);
				image = String.join("_th1", fileName, extension);
			}
			model.put(COMMENT_POST.WHAT_POSTED_ICON.getAction(), image);
		}

		model.put(COMMENT_POST.WHAT_POSTED_USERGROUPS.getAction(), groups);

		if (userGroup != null) {
			model.put(POST_TO_GROUP.WHERE_WEB_ADDRESS.getAction(), userGroup.getWebAddress());
			model.put(POST_TO_GROUP.WHERE_USER_GROUPNAME.getAction(), userGroup.getUserGroupName());
		}

		model.put(POST_TO_GROUP.SUBMIT_TYPE.getAction(),
				activity.getActivityType().toLowerCase().contains("post")
						|| activity.getActivityType().toLowerCase().contains("Added synonym")
						|| activity.getActivityType().toLowerCase().contains("Updated synonym")
						|| activity.getActivityType().toLowerCase().contains("Added common name")
						|| activity.getActivityType().toLowerCase().contains("Updated common name") ? "post" : "");
		data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));
		return data;
	}
}
