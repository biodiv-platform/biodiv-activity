/** */
package com.strandls.activity.service.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.strandls.activity.service.ActivityService;
import com.strandls.activity.service.MailService;
import com.strandls.activity.service.NotificationService;

/**
 * @author Abhishek Rudra
 */
public class ActivityServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ActivityService.class).to(ActivityServiceImpl.class).in(Scopes.SINGLETON);
		bind(MailService.class).to(MailServiceImpl.class).in(Scopes.SINGLETON);
		bind(NotificationService.class).to(NotificationServiceImpl.class).in(Scopes.SINGLETON);
	}
}
