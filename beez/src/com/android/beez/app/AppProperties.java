package com.android.beez.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.res.AssetManager;

public class AppProperties {
	private static String PROPERTIES_FILE = "app.properties";
	private static Properties properties = null;

	public static Properties load(AssetManager assetManager) {
		// Read from the /assets directory
		try {
			InputStream inputStream = assetManager.open(PROPERTIES_FILE);
			properties = new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static int store(String key, String value) {
		try {
			properties.setProperty(key, value);
			properties.store(new FileOutputStream(PROPERTIES_FILE), null);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}

		return 0;
	}

	public static String get(String key) {
		if (properties == null) {
			return null;
		}
		return properties.getProperty(key);
	}
}
