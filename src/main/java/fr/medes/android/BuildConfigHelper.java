package fr.medes.android;

import java.lang.reflect.Field;

public class BuildConfigHelper {

	public static final String APPLICATION_ID = (String) getBuildConfigValue("APPLICATION_ID");

	private static Object getBuildConfigValue(String fieldName) {
		try {
			String className = "fr.medes.android.BuildConfigWrapper";
			Class c = Class.forName(className);
			Field f = c.getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
