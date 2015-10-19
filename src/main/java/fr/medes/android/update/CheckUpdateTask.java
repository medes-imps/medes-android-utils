package fr.medes.android.update;

import java.io.InputStream;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;
import fr.medes.android.os.BaseAsyncTask;

/**
 * Download application information from a compatible server given the server address and the application package name
 * to explore.
 * <p/>
 * When calling the {@link AsyncTask#execute(Object...)} method the first parameter must be the URL to the server, the
 * second parameter should be the package name of the application to look for.
 * 
 * @author Medes-IMPS
 * 
 */
public class CheckUpdateTask extends BaseAsyncTask<String, Void, MarketApp> {

	@Override
	protected MarketApp doInBackground(String... params) {
		String server = params[0];
		String packageName = params[1];
		String url = server + (server.endsWith("/") ? "" : "/") + "package/" + packageName + ".xml";
		try {
			return MarketApp.Parser.parse((InputStream) new URL(url).getContent());
		} catch (Exception e) {
			Log.e(CheckUpdateTask.class.getName(), "Error parsing application description", e);
		}
		return null;
	}

}