package fr.medes.android.xml.converters;

import fr.medes.android.util.FormatHelper;

public class EnumConverter extends AbstractSingleValueConverter {

	@Override
	public Object fromString(String str) {
		return FormatHelper.toInteger(str);
	}

}
