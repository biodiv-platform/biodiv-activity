/** */
package com.strandls.activity.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author Abhishek Rudra
 */
public class ActivityControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ActivityController.class).in(Scopes.SINGLETON);
	}
}
