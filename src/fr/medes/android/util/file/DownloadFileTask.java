package fr.medes.android.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import fr.medes.android.os.BaseAsyncTask;

/**
 * An {@link AsyncTask} to download a file at a given URL.
 * <p/>
 * When calling {@link AsyncTask#execute(Object...)}, the first parameter must be the URL of the file to dosnload, the second
 * parameter must the destination of the file on the device. Make all the directories containing the file exist before calling
 * this method.
 * 
 * @author Medes-IMPS
 * 
 */
public class DownloadFileTask extends BaseAsyncTask<String, Integer, File> {

	/**
	 * Gently publish the updates only when at least one second has been elapsed since last published update.
	 */
	private static final long PUBLISH_TIME_LIMIT = 1000;

	private long lastTime = 0L;

	@Override
	protected File doInBackground(String... params) {
		try {
			final String urlString = params[0];
			final String path = params[1];
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a tipical 0-100%
			// progress bar

			// download the file
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];

			int count;
			int total = 0;
			while ((count = input.read(data)) != -1) {
				if (isCancelled()) {
					break;
				}
				total += count;
				// publishing the progress....
				long time = System.currentTimeMillis();
				if (time - lastTime > PUBLISH_TIME_LIMIT) {
					publishProgress(total);
					lastTime = time;
				}
				// publishing the progress....
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
			if (isCancelled()) {
				file.delete();
				return null;
			}
			return file;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}