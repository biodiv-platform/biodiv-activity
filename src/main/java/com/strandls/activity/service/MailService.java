package com.strandls.activity.service;

import java.util.List;

import com.strandls.activity.pojo.CommentLoggingData;
import com.strandls.activity.pojo.MailActivityData;
import com.strandls.activity.pojo.TaggedUser;
import com.strandls.mail_utility.model.EnumModel.MAIL_TYPE;
import com.strandls.mail_utility.model.EnumModel.OBJECT_TYPE;

public interface MailService {

	public void sendMail(MAIL_TYPE type, String objectType, Long objectId, Long userId, CommentLoggingData comment,
			MailActivityData activity, List<TaggedUser> taggedUsers,OBJECT_TYPE recordsType);

}
