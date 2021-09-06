package com.strandls.activity.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.TaggedUser;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.service.impl.PropertyFileUtil;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;

public class ActivityUtil {

	/**
	 * 
	 */
	private ActivityUtil() {
		super();
	}

	private static final Logger logger = LoggerFactory.getLogger(ActivityUtil.class);

	private static final String TAGGED_USER_REGEX = "@\\[(.*?)\\]\\(\\d+\\)";

	private static final Map<String, String> flaggedMessages = new HashMap<String, String>();

	static {
		flaggedMessages.put("DETAILS_INAPPROPRIATE", "Details Inapppropriate");
		flaggedMessages.put("LOCATION_INAPPROPRIATE", "Location Inapppropriate");
		flaggedMessages.put("DATE_INAPPROPRIATE", "Date Inapppropriate");
	}

	public static List<TaggedUser> getTaggedUsers(String comment) {
		List<TaggedUser> users = new ArrayList<TaggedUser>();
		Pattern pattern = Pattern.compile(TAGGED_USER_REGEX);
		try {
			Matcher matcher = pattern.matcher(comment);
			while (matcher.find()) {
				String match = matcher.group();
				TaggedUser user = new TaggedUser();
				user.setName(match.substring(match.indexOf("[") + 1, match.lastIndexOf("]")));
				user.setId(Long.parseLong(match.substring(match.indexOf("(") + 1, match.lastIndexOf(")"))));
				users.add(user);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return users;
	}

	public static String linkTaggedUsersProfile(List<TaggedUser> users, String commentBody, boolean withURL) {
		String comment = commentBody;
		try {
			for (TaggedUser user : users) {
				String taggedUserLink = null;
				if (withURL) {
					taggedUserLink = "<a href=\"*$URL$*\" target=\"_blank\">*$NAME$*</a>";
					String url = PropertyFileUtil.fetchProperty("config.properties", "portalAddress") + "/user/show/"
							+ user.getId();
					taggedUserLink = taggedUserLink.replace("*$URL$*", url);
				} else {
					taggedUserLink = "*$NAME$*";
				}
				taggedUserLink = taggedUserLink.replace("*$NAME$*", user.getName());
				comment = comment.replaceFirst(TAGGED_USER_REGEX, taggedUserLink);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return comment;
	}

	public static String replaceFlaggedMessage(String message) {
		for (Map.Entry<String, String> m : flaggedMessages.entrySet()) {
			if (message.contains(m.getKey())) {
				message = message.replaceAll(m.getKey(), m.getValue());
			}
		}
		return message;
	}

	public static Map<String, Object> getMailType(String activity, ActivityLoggingData loggingData) {
		boolean featuredToIBP = false;
		System.out.println("\n\n ***** " + activity + " " + loggingData.getActivityDescription() + " *****\n\n");
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<String> ugActivity = Arrays.asList("Featured", "UnFeatured");
			if (ugActivity.contains(loggingData.getActivityType())) {
				UserGroupActivity data = mapper.readValue(loggingData.getActivityDescription(),
						UserGroupActivity.class);
				featuredToIBP = (data.getUserGroupId() == null);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		Map<String, Object> data = new HashMap<String, Object>();
		switch (activity) {
		case "Observation created":
			data.put("type", MAIL_TYPE.OBSERVATION_ADDED);
			data.put("text", "Observation created");
			break;
		case "Observation updated":
			data.put("type", MAIL_TYPE.OBSERVATION_UPDATED);
			data.put("text", "Observation updated");
			break;
		case "obv unlocked":
			data.put("type", MAIL_TYPE.OBSERVATION_UNLOCKED);
			data.put("text", "Observation unlocked");
			break;
		case "Suggested species name":
			data.put("type", MAIL_TYPE.SUGGEST_MAIL);
			data.put("text", "Suggested species name");
			break;
		case "obv locked":
			data.put("type", MAIL_TYPE.OBSERVATION_LOCKED);
			data.put("text", "Observation locked");
			break;
		case "Agreed on species name":
			data.put("type", MAIL_TYPE.AGREED_SPECIES);
			data.put("text", "Agreed on species name");
			break;
		case "Posted resource":
			data.put("type", MAIL_TYPE.POST_TO_GROUP);
			data.put("text", "Posted resource");
			break;
		case "Removed resoruce":
			data.put("type", MAIL_TYPE.POST_TO_GROUP);
			data.put("text", "Removed resource");
			break;
		case "Featured":
			data.put("type", !featuredToIBP ? MAIL_TYPE.FEATURED_POST : MAIL_TYPE.FEATURED_POST_IBP);
			data.put("text", !featuredToIBP ? "Observation featured" : "Observation featured in IBP");
			break;
		case "Added a fact":
			data.put("type", MAIL_TYPE.FACT_ADDED);
			data.put("text", "Added a fact");
			break;
		case "Updated fact":
			data.put("type", MAIL_TYPE.FACT_UPDATED);
			data.put("text", "Updated fact");
			break;
		case "Flagged":
			data.put("type", MAIL_TYPE.OBSERVATION_FLAGGED);
			data.put("text", "Observation flagged");
			break;
		case "Suggestion removed":
			data.put("type", MAIL_TYPE.REMOVED_SPECIES);
			data.put("text", "Suggestion removed");
			break;
		case "Observation tag updated":
			data.put("type", MAIL_TYPE.TAG_UPDATED);
			data.put("text", "Observation tag updated");
			break;
		case "Custom field edited":
			data.put("type", MAIL_TYPE.CUSTOM_FIELD_UPDATED);
			data.put("text", "Custom field edited");
			break;
		case "Observation species group updated":
			data.put("type", null);
			break;
		case "Added a comment":
			data.put("type", MAIL_TYPE.COMMENT_POST);
			data.put("text", "Added a comment");
			break;
		case "Observation Deleted":
			data.put("type", MAIL_TYPE.OBSERVATION_DELETED);
			data.put("text", "Observation Deleted");
			break;
		case "Rated media resource":
			data.put("type", MAIL_TYPE.RATED_MEDIA_RESOURCE);
			data.put("text", "Rated media resource");
			break;

		default:
			data.put("type", null);
			break;
		}
		return data;
	}

	public static String getFormattedDate(String date) {
		String[] d = date.split(" ");
		return String.join(" ", Integer.parseInt(d[0]) + getDateSuffix(d[0]), d[1], d[2]);
	}

	private static String getDateSuffix(String date) {
		try {
			int d = Integer.parseInt(date);
			switch (d % 10) {
			case 1:
				return "st";
			case 2:
				return "nd";
			case 3:
				return "rd";
			default:
				return "th";
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return "";
	}

}
