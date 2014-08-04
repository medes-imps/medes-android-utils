package fr.medes.android.xml;

import java.util.HashSet;
import java.util.Set;

import fr.medes.android.xml.converters.Converter;
import fr.medes.android.xml.converters.ConverterLookup;

public class DefaultConverterLookup implements ConverterLookup {

	protected final Set<Converter> converters = new HashSet<Converter>();

	public DefaultConverterLookup() {
	}

	@Override
	public void addConverter(Converter converter) {
		converters.add(converter);
	}

	@Override
	public final Converter lookupConverterOfType(Class<? extends Converter> clazz) {
		for (Converter converter : converters) {
			if (converter.getClass().equals(clazz)) {
				return converter;
			}
		}
		return null;
	}

	@Override
	public final Converter lookupConverterForType(Class<?> clazz) {
		for (Converter converter : converters) {
			if (converter.canConvert(clazz)) {
				return converter;
			}
		}
		return null;
	}

}
