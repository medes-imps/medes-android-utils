package fr.medes.android.xml.mapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import fr.medes.android.util.annotation.ReflectionUtils;
import fr.medes.android.util.annotation.ReflectionUtils.FieldCallback;
import fr.medes.android.xml.annotation.XmlAlias;
import fr.medes.android.xml.annotation.XmlOmitField;

public class ClassMapper implements Mapper {

	private Map<String, Class<?>> aliasToClassMap = new HashMap<String, Class<?>>();
	private Map<Class<?>, String> classToAliasMap = new HashMap<Class<?>, String>();
	private Map<Class<?>, Mapper> classToMapperMap = new HashMap<Class<?>, Mapper>();

	/**
	 * Add a {@link Class} and its serialized representation to this mapper.
	 * 
	 * @param name The serialized representation.
	 * @param type The Class represented.
	 */
	public void addClassAlias(String name, Class<?> type) {
		aliasToClassMap.put(name, type);
		classToAliasMap.put(type, name);
		processClass(type);
	}

	private void processClass(Class<?> clazz) {
		final FieldMapper mapper = new FieldMapper(this);
		ReflectionUtils.doWithFields(clazz, new FieldCallback() {

			@Override
			public void doWith(Field field) {
				if (field.isAnnotationPresent(XmlOmitField.class) || field.getName().equals("shadow$_klass_")
						|| field.getName().equals("shadow$_monitor_")) {
					mapper.omitField(field.getName());
					return;
				}

				XmlAlias alias = field.getAnnotation(XmlAlias.class);
				String tag = alias != null ? alias.value() : field.getName();
				mapper.addFieldAlias(tag, field.getName());
			}
		});
		classToMapperMap.put(clazz, mapper);
	}

	@Override
	public Class<?> realClass(String elementName) {
		return aliasToClassMap.get(elementName);
	}

	@Override
	public String serializedClass(Class<?> type) {
		return classToAliasMap.get(type);
	}

	@Override
	public String realMember(Class<?> type, String serialized) {
		return lookupMapperOfType(type).realMember(type, serialized);
	}

	@Override
	public String serializedMember(Class<?> type, String memberName) {
		return lookupMapperOfType(type).serializedMember(type, memberName);
	}

	@Override
	public boolean shouldSerializeMember(Class<?> definedIn, String fieldName) {
		return classToMapperMap.get(definedIn).shouldSerializeMember(definedIn, fieldName);
	}

	@Override
	public boolean isImmutableValueType(Class<?> type) {
		return !classToMapperMap.containsKey(type);
	}

	@Override
	public Mapper lookupMapperOfType(Class<?> type) {
		return classToMapperMap.get(type);
	}

}
