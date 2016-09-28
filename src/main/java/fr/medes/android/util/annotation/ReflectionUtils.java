package fr.medes.android.util.annotation;

import java.lang.reflect.Field;

/**
 * Simple utility class for working with the reflection API.
 *
 * @author Medes-IMPS
 */
public class ReflectionUtils {

	/**
	 * Callback interface invoked on each field in the hierarchy.
	 *
	 * @author Medes-IMPS
	 */
	public interface FieldCallback {

		/**
		 * Perform an operation using the given field.
		 *
		 * @param field The field to operate on.
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 */
		void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;

	}

	/**
	 * Invoke the given callback on all fields in the target class, going up the class hierarchy to get all declared
	 * fields.
	 *
	 * @param clazz    The target class to analyze.
	 * @param callback The callback to invoke for each field.
	 */
	public static void doWithFields(Class<?> clazz, FieldCallback callback) {
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			doWithFields(superClass, callback);
		}
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length > 0) {
			for (Field field : fields) {
				try {
					callback.doWith(field);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns a {@link Field} object for the field with the given name which is declared in the a {@link Class} or its
	 * parents.
	 *
	 * @param clazz     the class containing the field
	 * @param fieldName the name of the field
	 * @return the {@link Field} object
	 * @throws NoSuchFieldException if the requested field can not be found.
	 */
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			return getField(superClass, fieldName);
		}
		throw new NoSuchFieldException();
	}

	/**
	 * Returns the field value of a field or <code>null</code> if the field is not present or empty.
	 *
	 * @param clazz     the class to find the field within
	 * @param fieldName the field name
	 * @param object    object to access
	 * @return the field value or <code>null</code>
	 */
	public static Object getFieldValue(Class<?> clazz, String fieldName, Object object) {
		try {
			Field field = getField(clazz, fieldName);
			return field.get(object);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns a {@link Class} object for the class with the given name which is declared in the {@link Class} or its
	 * parents.
	 *
	 * @param clazz The class containing the class
	 * @param name  The name of the class to search
	 * @return The inner class
	 */
	public static Class<?> getClass(Class<?> clazz, String name) {
		// Search class in the given class
		Class<?>[] classes = clazz.getClasses();
		if (classes != null && classes.length > 0) {
			for (Class<?> c : classes) {
				if (c.getSimpleName().equals(name)) {
					return c;
				}
			}
		}
		// Search class in the implemented interfaces
		// WARNING Columns for BinaryFile are in Binary interface
		Class<?>[] interfaces = clazz.getInterfaces();
		for (Class<?> interfaze : interfaces) {
			Class<?> result = getClass(interfaze, name);
			if (result != null) {
				return result;
			}
		}
		// Search class in inherited classes
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			Class<?> result = getClass(superClass, name);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

}
