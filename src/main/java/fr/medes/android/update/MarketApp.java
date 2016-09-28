package fr.medes.android.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.os.Parcelable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Application representation object
 * <p/>
 * You will find here below an example of a formatted XML file containing application information for update:
 * <p/>
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
 * <p/>
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

	public interface Tags {
		// Tags
		String APPLICATION = "application";
		String LABEL = "label";
		String PACKAGE = "package";
		String VERSION_CODE = "versionCode";
		String VERSION_NAME = "versionName";
		String SIZE = "size";
		String NAME = "name";
	}

	private String label;
	private String packageName;
	private int versionCode;
	private String versionName;
	private int size;
	private String name;

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
	 * Get the application version name.
	 *
	 * @return The application version name
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * Set the application version name.
	 *
	 * @param versionName The application version name
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
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
	 * Get the application file name.
	 *
	 * @return The application file name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set application file name.
	 *
	 * @param name The application file name
	 */
	public void setName(String name) {
		this.name = name;
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
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(label);
		dest.writeString(packageName);
		dest.writeInt(versionCode);
		dest.writeString(versionName);
		dest.writeInt(size);
		dest.writeString(name);
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
			app.setLabel(source.readString());
			app.setPackageName(source.readString());
			app.setVersionCode(source.readInt());
			app.setVersionName(source.readString());
			app.setSize(source.readInt());
			app.setName(source.readString());
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
			while (parser.getEventType() != XmlPullParser.END_TAG || !Tags.APPLICATION.equals(parser.getName())) {
				if (parser.next() == XmlPullParser.START_TAG) {
					String name = parser.getName();
					if (Tags.LABEL.equals(name)) {
						app.setLabel(parser.nextText());
					} else if (Tags.PACKAGE.equals(name)) {
						app.setPackageName(parser.nextText());
					} else if (Tags.VERSION_CODE.equals(name)) {
						app.setVersionCode(Integer.parseInt(parser.nextText()));
					} else if (Tags.VERSION_NAME.equals(name)) {
						app.setVersionName(parser.nextText());
					} else if (Tags.SIZE.equals(name)) {
						app.setSize(Integer.parseInt(parser.nextText()));
					} else if (Tags.NAME.equals(name)) {
						app.setName(parser.nextText());
					}
				}
			}
			return app;
		}
	}

}
