package fr.medes.android.xml.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FieldMapper implements Mapper {

	private Mapper mapper;
	private Map<String, String> aliasToFieldMap = new HashMap<>();
	private Map<String, String> fieldToAliasMap = new HashMap<>();
	private Set<String> fieldsToOmit = new HashSet<>();

	public FieldMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	public void addFieldAlias(String alias, String fieldName) {
		aliasToFieldMap.put(alias, fieldName);
		fieldToAliasMap.put(fieldName, alias);
	}

	public void omitField(String fieldName) {
		fieldsToOmit.add(fieldName);
	}

	@Override
	public Class<?> realClass(String elementName) {
		return mapper.realClass(elementName);
	}

	@Override
	public String serializedClass(Class<?> type) {
		return mapper.serializedClass(type);
	}

	@Override
	public String realMember(Class<?> type, String serialized) {
		return aliasToFieldMap.get(serialized);
	}

	@Override
	public String serializedMember(Class<?> type, String memberName) {
		return fieldToAliasMap.get(memberName);
	}

	@Override
	public boolean shouldSerializeMember(Class<?> definedIn, String fieldName) {
		return !fieldsToOmit.contains(fieldName);
	}

	@Override
	public Mapper lookupMapperOfType(Class<?> type) {
		return mapper.lookupMapperOfType(type);
	}

	@Override
	public boolean isImmutableValueType(Class<?> type) {
		return mapper.isImmutableValueType(type);
	}

}
