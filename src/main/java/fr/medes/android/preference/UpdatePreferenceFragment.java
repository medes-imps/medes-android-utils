package fr.medes.android.preference;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.Preference;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

import fr.medes.android.BuildConfigHelper;
import fr.medes.android.R;
import fr.medes.android.app.ProgressDialogFragment;
import fr.medes.android.os.BaseAsyncTask;
import fr.medes.android.update.CheckUpdateTask;
import fr.medes.android.update.MarketApp;
import fr.medes.android.util.file.DownloadFileTask;

public class UpdatePreferenceFragment extends MyPreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

	private static final String PROGRESS_DIALOG_FRAGMENT_TAG = "progressdialog";

	private static final int REQUEST_PERMISSION_UPDATE_APP = 1;

	private Preference mUpdateAvailable;
	private EditTextPreference mUpdateServer;

	private CheckUpdateTask mCheckUpdateTask;
	private DownloadFileTask mDownloadFileTask;

	private MarketApp mMarketApp;

	@Override
	public void onCreatePreferences(Bundle bundle, String s) {
		Bundle args = getArguments();

		if (args != null && args.getBoolean(ARG_SHOULD_INFLATE, true)) {
			setPreferencesFromResource(R.xml.aml__update_preferences, s);

//			mUpdateServer = (EditTextPreference) findPreference(R.string.aml__pref_update_server);
			mUpdateAvailable = findPreference(R.string.aml__pref_update_available);
		}

		refreshPreferenceState();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mCheckUpdateTask != null) {
			mCheckUpdateTask.setCallback(mCheckUpdateCallback);
		}
		if (mDownloadFileTask != null) {
			mDownloadFileTask.setCallback(mDownloadFileCallback);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mCheckUpdateTask != null) {
			mCheckUpdateTask.setCallback(null);
		}
		if (mDownloadFileTask != null) {
			mDownloadFileTask.setCallback(null);
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
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_PERMISSION_UPDATE_APP:
				if (checkPermissionsGranted(grantResults)) {
					executeUpdateApplication();
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}


	public void setPreferences(Preference updateAvailable, EditTextPreference updateServer) {
		mUpdateAvailable = updateAvailable;
		mUpdateServer = updateServer;

		refreshPreferenceState();
	}

	private void refreshPreferenceState() {
		if (mUpdateAvailable == null || mUpdateServer == null) {
			return;
		}

		mUpdateServer.setOnPreferenceChangeListener(this);
		mUpdateAvailable.setOnPreferenceClickListener(this);

		executeCheckUpdate(mUpdateServer.getText());

		setRetainInstance(true);
	}

	public void cancelDownload() {
		mDownloadFileTask.cancel(true);
	}

	private void executeCheckUpdate(String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		mCheckUpdateTask = new CheckUpdateTask();
		mCheckUpdateTask.setCallback(mCheckUpdateCallback);
		mCheckUpdateTask.execute(url, getContext().getPackageName());
	}

	private void setUpdateAvailable(MarketApp app) {
		mMarketApp = app;
		if (mCheckUpdateTask != null) {
			mCheckUpdateTask.setCallback(null);
			mCheckUpdateTask = null;
		}
		if (app != null && app.isUpdatable(getContext())) {
			mUpdateAvailable.setEnabled(true);
			mUpdateAvailable.setSummary(R.string.aml__preferences_update_available_enabled);
		} else {
			mUpdateAvailable.setEnabled(false);
			mUpdateAvailable.setSummary(R.string.aml__preferences_update_available_disabled);
		}
	}

	private void executeUpdateApplication() {
		if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_UPDATE_APP);
			return;
		}
		if (mMarketApp == null) {
			return;
		}
		String server = mUpdateServer.getText();
		String url = server + (server.endsWith("/") ? "" : "/") + "apk/" + mMarketApp.getName();
		File dir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), mMarketApp.getName());
		mDownloadFileTask = new DownloadFileTask();
		mDownloadFileTask.setCallback(mDownloadFileCallback);
		mDownloadFileTask.execute(url, dir.getPath());
	}

	private void downloadFinishedOrStopped(File result) {
		dismissProgressDialogFragment();

		if (mDownloadFileTask != null) {
			mDownloadFileTask.setCallback(null);
			mDownloadFileTask = null;
		}

		if (result != null) {
			Uri uri = FileProvider.getUriForFile(getContext(), BuildConfigHelper.<String>getBuildConfigValue("AUTHORITY_FILE_PROVIDER"), result);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(uri, "application/vnd.android.package-archive");
			startActivity(intent);
		}
	}

	private void showProgressDialogFragment() {
		Bundle args = new Bundle();
		args.putString(ProgressDialogFragment.ARG_TITLE, getString(R.string.aml__update_download_title));
		args.putString(ProgressDialogFragment.ARG_MESSAGE, getString(R.string.aml__update_download_message));
		args.putBoolean(ProgressDialogFragment.ARG_CANCELABLE, true);
		args.putInt(ProgressDialogFragment.ARG_STYLE, ProgressDialog.STYLE_HORIZONTAL);
		args.putInt(ProgressDialogFragment.ARG_MAX, mMarketApp.getSize() / 1000);
		args.putString(ProgressDialogFragment.ARG_NUMBER_FORMAT, getString(R.string.aml__update_download_format));
		ProgressDialogFragment fragment = ProgressDialogFragment.newInstance();
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), PROGRESS_DIALOG_FRAGMENT_TAG);
	}

	private void updateProgress(int value) {
		ProgressDialogFragment fragment = getProgressDialogFragment();
		if (fragment != null) {
			fragment.setProgress(value / 1000);
		}
	}

	private void dismissProgressDialogFragment() {
		ProgressDialogFragment fragment = getProgressDialogFragment();
		if (fragment != null) {
			fragment.dismissAllowingStateLoss();
		}
	}

	private ProgressDialogFragment getProgressDialogFragment() {
		Fragment fragment = getFragmentManager().findFragmentByTag(PROGRESS_DIALOG_FRAGMENT_TAG);
		return (ProgressDialogFragment) fragment;
	}

	private boolean checkPermissionsGranted(int[] grantResults) {
		for (int grant : grantResults) {
			if (grant != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(getContext(), R.string.aml__permission_not_granted, Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////
	// Callbacks
	////////////////////////////////////////////////////////////////////////////////////////////////


	private final BaseAsyncTask.Callback<String, Void, MarketApp> mCheckUpdateCallback = new BaseAsyncTask.BaseCallback<String, Void, MarketApp>() {

		@Override
		public void onAttachedToTask(AsyncTask.Status status, MarketApp result) {
			if (status == AsyncTask.Status.FINISHED) {
				// may not have been detached if no callback was registered when task ended.
				setUpdateAvailable(result);
			} else if (status == AsyncTask.Status.RUNNING) {
				mUpdateAvailable.setEnabled(false);
			}
		}

		@Override
		public void onPostExecute(MarketApp result) {
			setUpdateAvailable(result);
		}

		@Override
		public void onPreExecute() {
			mUpdateAvailable.setEnabled(false);
		}

	};


	private final BaseAsyncTask.Callback<String, Integer, File> mDownloadFileCallback = new BaseAsyncTask.BaseCallback<String, Integer, File>() {

		@Override
		public void onAttachedToTask(AsyncTask.Status status, File file) {
			if (status == AsyncTask.Status.FINISHED) {
				// may not have been detached if no callback was registered when task ended.
				onPostExecute(file);
			} else if (status == AsyncTask.Status.RUNNING) {
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
			showProgressDialogFragment();
		}

		@Override
		public void onProgressUpdate(Integer... values) {
			updateProgress(values[0]);
		}

	};

}
