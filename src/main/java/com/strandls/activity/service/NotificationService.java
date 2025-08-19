package com.strandls.activity.service;

import com.strandls.activity.pojo.MailActivityData;

public interface NotificationService {

	public void sendNotification(MailActivityData activity, String objectType, Long objectId, String title,
			String content);
}
