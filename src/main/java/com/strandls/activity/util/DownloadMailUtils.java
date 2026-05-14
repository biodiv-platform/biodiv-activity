/**
 * 
 */
package com.strandls.activity.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.mail_utility.model.EnumModel.DOWNLOAD_MAIL;
import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INFO_FIELDS;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.activity.RabbitMqConnection;
import com.strandls.activity.service.impl.PropertyFileUtil;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.User;

/**
 * @author Mekala Rishitha Ravi
 *
 * 
 */
public class DownloadMailUtils {

	private final Logger logger = LoggerFactory.getLogger(CCAMailUtils.class);

	@Inject
	private RabbitMQProducer mailProducer;

	@Inject
	private UserServiceApi userServiceApi;

	public void sendMail(String authorId, String fileName, String type) {
		try {

			User user = userServiceApi.getUser(authorId);

			Properties properties = PropertyFileUtil.fetchProperty("config.properties");

			Map<String, Object> data = new HashMap<>();
			data.put(FIELDS.TO.getAction(), new String[] { user.getEmail() });
			data.put(FIELDS.SUBSCRIPTION.getAction(), user.getSendNotification());
			Map<String, Object> model = new HashMap<>();
			model.put(DOWNLOAD_MAIL.SERVER_URL.getAction(), properties.getProperty("serverUrl"));
			model.put(DOWNLOAD_MAIL.SITENAME.getAction(), properties.getProperty("siteName"));
			model.put(DOWNLOAD_MAIL.USER_DATA.getAction(), user);
			model.put(DOWNLOAD_MAIL.DOWNLOAD_TYPE.getAction(), type);
			model.put(DOWNLOAD_MAIL.DOWNLOAD_FILE.getAction(), fileName);
			model.put(DOWNLOAD_MAIL.TYPE.getAction(), MAIL_TYPE.DOWNLOAD_MAIL.getAction());
			data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(model));

			Map<String, Object> mData = new HashMap<>();
			mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.DOWNLOAD_MAIL.getAction());
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
