package fr.medes.android.update;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Application representation object
 * <p/>
 * You will find here below an example of a formatted XML file containing application information for update:
 * 
 * <pre>
 *         {@code
 * 	<application id="546">
 * 		<label>A Label</label>
 * 		<package>a.package.name</package>
 * 		<versionCode>45</versionCode>
 * 		<versionName>2.9.5-beta</versionName>
 * 		<size>5466547</size>
 * 		<file>myapp-2.9.5b.apk</file>
 * 	</application>
 * }
 * </pre>
 * <p/>
 * The server folder structure should be as describe below:
 * 
 * <pre>
 * {@code
 * http://my.server.com/update/package/a.package.name.xml
 * http://my.server.com/update/apk/myapp-2.9.5.apk
 * }
 * </pre>
 * 
 * @author Medes-IMPS
 */
public class MarketApp implements Parcelable {

	public static interface Tags {
		// Attributes
		public static final String ATTR_ID = "id";

		// Tags
		public static final String APPLICATION = "application";
		public static final String PACKAGE = "package";
		public static final String LABEL = "label";
		public static final String DESCRIPTION = "description";
		public static final String VERSION_CODE = "versionCode";
		public static final String FILE = "file";
		public static final String SIZE = "size";
	}

	private long id;
	private String packageName;
	private String label;
	private String description;
	private String file;
	private int versionCode;
	private int size;

	/**
	 * Get the application identifier.
	 * 
	 * @return The application identifier
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the application identifier.
	 * 
	 * @param id The application identifier
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Get the application package name.
	 * 
	 * @return The application package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Set the application package name.
	 * 
	 * @param packageName The application package name
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Get the application label.
	 * 
	 * @return The application label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the application label.
	 * 
	 * @param label The application label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Get the application description.
	 * 
	 * @return The application description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the application description.
	 * 
	 * @param description The application description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the file name of the application.
	 * 
	 * @return The application file name
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Set the application file name.
	 * 
	 * @param file The application file name
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * Get the application version code.
	 * 
	 * @return The application version code
	 */
	public int getVersionCode() {
		return versionCode;
	}

	/**
	 * Set the application version code.
	 * 
	 * @param versionCode The application version code
	 */
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * Get the application size.
	 * 
	 * @return The application size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Set the applicatuion size.
	 * 
	 * @param size The application size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Check if the application described by this content is installed and can be updated.
	 * 
	 * @param context The current context
	 * @return {@code true} if the application can be updated, {@code false} otherwise
	 */
	public boolean isUpdatable(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(packageName, 0);
			if (info.versionCode < versionCode) {
				return true;
			}
		} catch (NameNotFoundException e) {
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeInt(size);
		dest.writeInt(versionCode);
		dest.writeString(packageName);
		dest.writeString(label);
		dest.writeString(description);
		dest.writeString(file);
	}

	// Parcelable
	public static final Parcelable.Creator<MarketApp> CREATOR = new Creator<MarketApp>() {

		@Override
		public MarketApp[] newArray(int size) {
			return new MarketApp[size];
		}

		@Override
		public MarketApp createFromParcel(Parcel source) {
			MarketApp app = new MarketApp();
			app.setId(source.readLong());
			app.setSize(source.readInt());
			app.setVersionCode(source.readInt());
			app.setPackageName(source.readString());
			app.setLabel(source.readString());
			app.setDescription(source.readString());
			app.setFile(source.readString());
			return app;
		}
	};

	/**
	 * File parser to extract application informations from a file.
	 */
	public static class Parser {

		/**
		 * Try to extract an application from a given file. The application has to be described using an XML
		 * description.
		 * 
		 * @param is The input stream
		 * @return The extracted application
		 * @throws XmlPullParserException
		 * @throws IOException
		 */
		public static MarketApp parse(InputStream is) throws XmlPullParserException, IOException {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, null);
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				if (parser.getEventType() == XmlPullParser.START_TAG && Tags.APPLICATION.equals(parser.getName())) {
					return parseApplication(parser);
				}
			}
			return null;
		}

		private static MarketApp parseApplication(XmlPullParser parser) throws XmlPullParserException, IOException {
			MarketApp app = new MarketApp();
			app.setId(Long.parseLong(parser.getAttributeValue(null, Tags.ATTR_ID)));
			while (parser.getEventType() != XmlPullParser.END_TAG || !Tags.APPLICATION.equals(parser.getName())) {
				if (parser.next() == XmlPullParser.START_TAG) {
					String name = parser.getName();
					if (Tags.DESCRIPTION.equals(name)) {
						app.setDescription(parser.nextText());
					} else if (Tags.FILE.equals(name)) {
						app.setFile(parser.nextText());
					} else if (Tags.LABEL.equals(name)) {
						app.setLabel(parser.nextText());
					} else if (Tags.PACKAGE.equals(name)) {
						app.setPackageName(parser.nextText());
					} else if (Tags.VERSION_CODE.equals(name)) {
						app.setVersionCode(Integer.parseInt(parser.nextText()));
					} else if (Tags.SIZE.equals(name)) {
						app.setSize(Integer.parseInt(parser.nextText()));
					}
				}
			}
			return app;
		}
	}

}
