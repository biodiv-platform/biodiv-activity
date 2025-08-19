/** */
package com.strandls.activity;

import com.strandls.user.controller.UserServiceApi;

import jakarta.ws.rs.core.HttpHeaders;

/**
 * @author Abhishek Rudra
 */
public class Headers {

	public UserServiceApi addUserHeader(UserServiceApi userService, String authHeader) {
		userService.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, authHeader);
		return userService;
	}
}
