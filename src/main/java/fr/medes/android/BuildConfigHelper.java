package fr.medes.android;

import java.lang.reflect.Field;

public class BuildConfigHelper {

	private static final Class<?> BUILD_CONFIG_CLASS = getBuildConfigClass();

	public static Object getBuildConfigValue(String fieldName) {
		return getDeclaredFieldValue(BUILD_CONFIG_CLASS, fieldName);
	}

	private static Class<?> getBuildConfigClass() {
		try {
			String className = "fr.medes.android.BuildConfigWrapper";
			Class c = Class.forName(className);
			return getDeclaredFieldValue(c, "BUILD_CONFIG_CLASS");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T getDeclaredFieldValue(Class<?> clazz, String fieldName) {
		try {
			Field f = clazz.getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T) f.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
