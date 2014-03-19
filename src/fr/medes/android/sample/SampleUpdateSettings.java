package fr.medes.android.sample;

import java.io.File;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import fr.medes.android.app.MyProgressDialog;
import fr.medes.android.app.MyProgressDialog.Formatter;
import fr.medes.android.library.R;
import fr.medes.android.os.BaseAsyncTask.Callback;
import fr.medes.android.update.CheckUpdateTask;
import fr.medes.android.update.MarketApp;
import fr.medes.android.util.file.DownloadFileTask;
import fr.medes.android.util.file.FileUtils;

public class SampleUpdateSettings extends PreferenceActivity implements OnPreferenceChangeListener,
		OnPreferenceClickListener {

	private static final int DIALOG_DOWNLOAD_ID = 1;

	private static final String KEY_UPDATE_SERVER = "updateServer";
	private static final String KEY_UPDATE_AVAILABLE = "updateAvailable";

	private Preference mUpdateAvailable;
	private EditTextPreference mUpdateServer;

	private MyProgressDialog mProgressDialog;

	private RetainObject retain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.aml__sample_settings);

		mUpdateServer = (EditTextPreference) findPreference(KEY_UPDATE_SERVER);
		mUpdateAvailable = findPreference(KEY_UPDATE_AVAILABLE);

		mUpdateServer.setOnPreferenceChangeListener(this);
		mUpdateAvailable.setOnPreferenceClickListener(this);

		retain = (RetainObject) getLastNonConfigurationInstance();
		if (retain == null) {
			retain = new RetainObject();
		}

		if (retain.checkUpdateTask != null) {
			retain.checkUpdateTask.setCallback(mCheckUpdateCallback);
		}

		if (retain.downloadFileTask != null) {
			retain.downloadFileTask.setCallback(mDownloadFileCallback);
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return retain;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (retain.checkUpdateTask != null) {
			retain.checkUpdateTask.setCallback(null);
		}

		if (retain.downloadFileTask != null) {
			retain.downloadFileTask.setCallback(null);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mUpdateServer) {
			executeCheckUpdate((String) newValue);
			return true;
		}
		return false;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mUpdateAvailable) {
			executeUpdateApplication();
			return true;
		}
		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_ID:
			mProgressDialog = new MyProgressDialog(this);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setTitle(R.string.aml__sample_update_app_title);
			mProgressDialog.setMessage(getString(R.string.aml__sample_update_app_message));
			mProgressDialog.setFormatter(new Formatter() {
				@Override
				public String format(int progress, int max) {
					String readableProgress = FileUtils.readableFileSize(progress);
					String readableMax = FileUtils.readableFileSize(max);
					return String.format("%s / %s", readableProgress, readableMax);
				}
			});
			mProgressDialog.setCancelable(true);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (retain.downloadFileTask != null) {
						retain.downloadFileTask.cancel(true);
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			return mProgressDialog;
		default:
			return super.onCreateDialog(id);
		}
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_DOWNLOAD_ID:
			mProgressDialog.setMax(retain.marketApp.getSize());
			mProgressDialog.setProgress(0);
			break;
		default:
			super.onPrepareDialog(id, dialog);
			break;
		}
	}

	private void executeCheckUpdate(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		retain.checkUpdateTask = new CheckUpdateTask();
		retain.checkUpdateTask.setCallback(mCheckUpdateCallback);
		retain.checkUpdateTask.execute(url, getPackageName());
	}

	private void executeUpdateApplication() {
		String server = mUpdateServer.getText();
		String url = server + (server.endsWith("/") ? "" : "/") + "apk/" + retain.marketApp.getFile();
		File dir = new File(Environment.getExternalStorageDirectory(), retain.marketApp.getFile());
		retain.downloadFileTask = new DownloadFileTask();
		retain.downloadFileTask.setCallback(mDownloadFileCallback);
		retain.downloadFileTask.execute(url, dir.getPath());
	}

	private void setUpdateAvailable(MarketApp app) {
		retain.marketApp = app;
		retain.checkUpdateTask.setCallback(null);
		retain.checkUpdateTask = null;
		if (app != null && app.isUpdatable(this)) {
			mUpdateAvailable.setEnabled(true);
			mUpdateAvailable.setSummary(R.string.aml__sample_update_available_summary_enabled);
		} else {
			mUpdateAvailable.setEnabled(false);
			mUpdateAvailable.setSummary(R.string.aml__sample_update_available_summary_disabled);
		}
	}

	private void downloadFinishedOrStopped(File result) {
		dismissDialog(DIALOG_DOWNLOAD_ID);

		if (retain.downloadFileTask != null) {
			retain.downloadFileTask.setCallback(null);
			retain.downloadFileTask = null;
		}

		if (result != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
			startActivity(intent);
		}
	}

	private final Callback<String, Void, MarketApp> mCheckUpdateCallback = new Callback<String, Void, MarketApp>() {

		@Override
		public void onAttachedToTask(Status status, MarketApp result) {
			if (status == Status.FINISHED) {
				// may not have been detached if no callback was registered when task ended.
				setUpdateAvailable(result);
			} else if (status == Status.RUNNING) {
				mUpdateAvailable.setEnabled(false);
			}
		}

		@Override
		public void onCancelled() {
			// Nothing to do
		}

		@Override
		public void onPostExecute(MarketApp result) {
			setUpdateAvailable(result);
		}

		@Override
		public void onPreExecute() {
			mUpdateAvailable.setEnabled(false);
		}

		@Override
		public void onProgressUpdate(Void... values) {
			// Nothing to do
		}

	};

	private final Callback<String, Integer, File> mDownloadFileCallback = new Callback<String, Integer, File>() {

		@Override
		public void onAttachedToTask(Status status, File file) {
			if (status == Status.FINISHED) {
				// may not have been detached if no callback was registered when task ended.
				onPostExecute(file);
			} else if (status == Status.RUNNING) {
				mUpdateAvailable.setEnabled(false);
			}
		}

		@Override
		public void onCancelled() {
			downloadFinishedOrStopped(null);
		}

		@Override
		public void onPostExecute(File result) {
			downloadFinishedOrStopped(result);
		}

		@Override
		public void onPreExecute() {
			showDialog(DIALOG_DOWNLOAD_ID);
		}

		@Override
		public void onProgressUpdate(Integer... values) {
			mProgressDialog.setProgress(values[0]);
		}

	};

	private static class RetainObject {
		private CheckUpdateTask checkUpdateTask;
		private DownloadFileTask downloadFileTask;
		private MarketApp marketApp;
	}

}
