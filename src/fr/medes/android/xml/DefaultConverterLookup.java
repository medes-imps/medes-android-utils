package fr.medes.android.xml;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import fr.medes.android.xml.converters.BooleanConverter;
import fr.medes.android.xml.converters.ByteArrayConverter;
import fr.medes.android.xml.converters.Converter;
import fr.medes.android.xml.converters.ConverterLookup;
import fr.medes.android.xml.converters.DateConverter;
import fr.medes.android.xml.converters.EnumConverter;
import fr.medes.android.xml.converters.EnumMultiConverter;
import fr.medes.android.xml.converters.FloatConverter;
import fr.medes.android.xml.converters.GpsConverter;
import fr.medes.android.xml.converters.IntegerConverter;
import fr.medes.android.xml.converters.LongConverter;
import fr.medes.android.xml.converters.RolesConverter;
import fr.medes.android.xml.converters.StringConverter;
import fr.medes.android.xml.mapper.Mapper;

public class DefaultConverterLookup implements ConverterLookup {

	protected final Set<Converter> converters = new HashSet<Converter>();

	public DefaultConverterLookup(Context context, Mapper mapper) {
		converters.add(new BooleanConverter());
		converters.add(new ByteArrayConverter());
		converters.add(new DateConverter());
		converters.add(new EnumConverter());
		converters.add(new EnumMultiConverter(context));
		converters.add(new FloatConverter());
		converters.add(new GpsConverter());
		converters.add(new IntegerConverter());
		converters.add(new LongConverter());
		converters.add(new RolesConverter());
		converters.add(new StringConverter());
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
