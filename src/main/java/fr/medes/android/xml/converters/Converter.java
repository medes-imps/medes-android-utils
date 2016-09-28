package fr.medes.android.xml.converters;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import fr.medes.android.xml.annotation.XmlConverter;

/**
 * Converter implementations are responsible marshalling Java objects to/from textual data.
 *
 * @author Medes-IMPS
 */
public interface Converter {

	/**
	 * Determines whether the converter can marshall a particular type.
	 *
	 * @param clazz the Class representing the object type to be converted.
	 * @return If it can marshall the type.
	 */
	boolean canConvert(Class<?> clazz);

	/**
	 * Convert textual data back into an object.
	 *
	 * @param parser The parser to read the text from.
	 * @return The resulting object.
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	Object parse(XmlPullParser parser) throws XmlPullParserException, IOException;

	/**
	 * Convert an object to textual data.
	 *
	 * @param serializer A serializer to write to.
	 * @param obj        The object to be marshalled.
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	void serialize(XmlSerializer serializer, Object obj) throws IllegalArgumentException, IllegalStateException,
			IOException;

	/**
	 * Integer value provided to the converter.
	 *
	 * @param integer The integer value.
	 * @see XmlConverter#integer()
	 */
	void setInteger(int integer);

	/**
	 * String value provided to the converter.
	 *
	 * @param string The string.
	 */
	void setString(String string);

}
