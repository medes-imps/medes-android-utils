package fr.medes.android.xml.mapper;

/**
 * Allows to navigate between objects and their serialized representation.
 *
 * @author Medes-IMPS
 */
public interface Mapper {

	/**
	 * How a serialized class representation should be mapped back to a real class.
	 *
	 * @param elementName
	 * @return
	 */
	Class<?> realClass(String elementName);

	/**
	 * How a class name should be represented in its serialized form.
	 *
	 * @param type
	 * @return
	 */
	String serializedClass(Class<?> type);

	/**
	 * How a serialized member representation should be mapped back to a real member.
	 *
	 * @param type
	 * @param serialized
	 * @return
	 */
	String realMember(Class<?> type, String serialized);

	/**
	 * How a class member should be represented in its serialized form.
	 *
	 * @param type
	 * @param memberName
	 * @return
	 */
	String serializedMember(Class<?> type, String memberName);

	/**
	 * Determine whether a specific member should be serialized.
	 *
	 * @param definedIn
	 * @param fieldName
	 * @return
	 */
	boolean shouldSerializeMember(Class<?> definedIn, String fieldName);

	/**
	 * Whether this type is a simple immutable value (int, boolean, String, URL, etc. Immutable types will be repeatedly
	 * written in the serialized stream, instead of using object references.
	 *
	 * @param type
	 * @return
	 */
	boolean isImmutableValueType(Class<?> type);

	/**
	 * Finds a Mapper convenient for the given Class.
	 *
	 * @param type The class to find a mapper for.
	 * @return The mapper found or {@code null}.
	 */
	Mapper lookupMapperOfType(Class<?> type);

}
