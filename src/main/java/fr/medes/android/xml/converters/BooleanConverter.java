package fr.medes.android.xml.converters;

import fr.medes.android.util.FormatHelper;

/**
 * Converts a boolean primitive or java.lang.Boolean wrapper to a String.
 * 
 * @author Medes-IMPS
 * 
 */
public class BooleanConverter extends AbstractSingleValueConverter {

	@Override
	public boolean canConvert(Class<?> clazz) {
		return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
	}

	@Override
	public Object fromString(String str) {
		return FormatHelper.toBoolean(str);
	}

}
