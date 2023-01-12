/**
 * 
 */
package com.strandls.activity.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.mail_utility.model.EnumModel.CCA_DATA_PERMISSION_REQUEST;
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
 * @author Arun
 *
 * 
 */
public class CCAMailUtils {

	private final Logger logger = LoggerFactory.getLogger(CCAMailUtils.class);

	@Inject
	private RabbitMQProducer mailProducer;

	@Inject
	private UserServiceApi userServiceApi;

	public void sendPermissionRequest(List<User> requestors, String ccaName, Long ccaId, String role, User requestee,
			String encryptedKey, Map<String, Object> summaryData, String requestorMessage) {
		List<String> emailList = new ArrayList<String>();
		for (User requestor : requestors) {
			if (requestor.getEmail() != null)
				emailList.add(requestor.getEmail());
		}
		try {
			Map<String, Object> data = new HashMap<>();
			data.put(FIELDS.TO.getAction(), emailList.toArray());
			data.put(FIELDS.SUBSCRIPTION.getAction(), true);
			Map<String, Object> permissionRequest = new HashMap<>();

			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.ENCRYPTED_KEY.getAction(), encryptedKey);
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.REQUESTEE_ID.getAction(), requestee.getId());
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.REQUESTEE_NAME.getAction(), requestee.getName());
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.ROLE.getAction(), role);
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.CCA_ID.getAction(), ccaId);
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.CCA_NAME.getAction(), ccaName);
			permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.REQUESTOR_MESSAGE.getAction(), requestorMessage);
			if (summaryData != null)
				permissionRequest.put(CCA_DATA_PERMISSION_REQUEST.SUMMARY.getAction(), summaryData);

			data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(permissionRequest));

			Map<String, Object> mData = new HashMap<>();
			mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.CCA_DATA_PERMISSION_REQUEST.getAction());
			mData.put(INFO_FIELDS.RECIPIENTS.getAction(), Arrays.asList(data));

			mailProducer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
					JsonUtil.mapToJSON(mData));

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

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
