/**
 * 
 */
package com.strandls.activity.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.mail_utility.model.EnumModel.CCA_DATA_PERMISSION_REQUEST;
import com.strandls.mail_utility.model.EnumModel.FIELDS;
import com.strandls.mail_utility.model.EnumModel.INFO_FIELDS;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.PERMISSION_GRANT;
import com.strandls.mail_utility.model.EnumModel.PERMISSION_REQUEST;
import com.strandls.mail_utility.producer.RabbitMQProducer;
import com.strandls.mail_utility.util.JsonUtil;
import com.strandls.activity.RabbitMqConnection;
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

	public void sendPermissionRequest(List<User> requestors, String ccaName, Long ccaId, String role, User requestee,
			String encryptedKey) {
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

	public void sendPermissionGrant(User requestee, String taxonName, String role, Long taxonId) {

		if (requestee.getEmail() != null) {
			try {
				Map<String, Object> data = new HashMap<>();
				data.put(FIELDS.TO.getAction(), new String[] { requestee.getEmail() });
				data.put(FIELDS.SUBSCRIPTION.getAction(), true);

				Map<String, Object> permissionGrantData = new HashMap<>();
				permissionGrantData.put(PERMISSION_GRANT.REQUESTEE_NAME.getAction(), requestee.getName());
				permissionGrantData.put(PERMISSION_GRANT.ROLE.getAction(), role);
				permissionGrantData.put(PERMISSION_GRANT.TAXON_ID.getAction(), taxonId);
				permissionGrantData.put(PERMISSION_GRANT.TAXON_NAME.getAction(), taxonName);

				data.put(FIELDS.DATA.getAction(), JsonUtil.unflattenJSON(permissionGrantData));

				Map<String, Object> mData = new HashMap<>();
				mData.put(INFO_FIELDS.TYPE.getAction(), MAIL_TYPE.PERMISSION_GRANTED.getAction());
				mData.put(INFO_FIELDS.RECIPIENTS.getAction(), Arrays.asList(data));

				mailProducer.produceMail(RabbitMqConnection.EXCHANGE, RabbitMqConnection.ROUTING_KEY, null,
						JsonUtil.mapToJSON(mData));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		}

	}

}
