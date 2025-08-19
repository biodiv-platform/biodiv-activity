package com.strandls.activity.service.impl;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFileUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertyFileUtil.class);

	private PropertyFileUtil() {
		// private constructor to prevent instantiation
	}

	/**
	 * Loads the given property file from the classpath. Assumes the file is inside
	 * `src/main/resources/`.
	 */
	public static Properties fetchProperty(String fileName) {
		Properties properties = new Properties();

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");

			properties.load(in);
			return properties;

		} catch (Exception e) {
			logger.error("❌ Failed to load properties file '{}': {}", fileName, e.getMessage());
		}
		return properties;
	}

	/** Loads a specific property from the given file in classpath. */
	public static String fetchProperty(String fileName, String propertyName) {
		Properties properties = fetchProperty(fileName);
		String value = properties.getProperty(propertyName);

		if (value == null) {
			logger.warn("⚠️ Property '{}' not found in '{}'", propertyName, fileName);
		}

		return value;
	}
}
