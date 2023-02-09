package com.strandls.activity.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.RabbitMqConnection;
import com.strandls.activity.service.impl.PropertyFileUtil;
import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INFO_FIELDS;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.ODK_USER_EMAIL;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.User;

public class ODKMailUtils {

	private final Logger logger = LoggerFactory.getLogger(CCAMailUtils.class);

	@Inject
	private RabbitMQProducer mailProducer;

	@Inject
	private UserServiceApi userServiceApi;

	public void sendOdkUserMail(String userId, String passWord, String role, String projectId, String projectName) {
		try {

			User user = userServiceApi.getUser(userId);

			Properties properties = PropertyFileUtil.fetchProperty("config.properties");

			Map<String, Object> data = new HashMap<>();
			data.put(FIELDS.TO.getAction(), new String[] { user.getEmail() });
			data.put(FIELDS.SUBSCRIPTION.getAction(), user.getSendNotification());
			Map<String, Object> model = new HashMap<>();
			model.put(ODK_USER_EMAIL.SERVER_URL.getAction(), properties.getProperty("serverUrl"));
			model.put(ODK_USER_EMAIL.SITENAME.getAction(), properties.getProperty("siteName"));
			model.put(ODK_USER_EMAIL.ODK_EMAIL.getAction(), user.getEmail());
			model.put(ODK_USER_EMAIL.USER_NAME.getAction(), user.getName());

			model.put(ODK_USER_EMAIL.PASSWORD.getAction(), passWord);
			model.put(ODK_USER_EMAIL.ROLE.getAction(), role);
			model.put(ODK_USER_EMAIL.PROJECT_ID.getAction(), projectId);
			model.put(ODK_USER_EMAIL.PROJECT_NAME.getAction(), projectName);

			data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));

			Map<String, Object> mData = new HashMap<>();
			mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.ODK_USER_EMAIL.getAction());
			mData.put(INFO_FIELDS.RECIPIENTS.getAction(), Arrays.asList(data));
			if (user.getEmail() != null && !user.getEmail().isEmpty()) {
				mailProducer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
						JsonUtil.mapToJSON(mData));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
