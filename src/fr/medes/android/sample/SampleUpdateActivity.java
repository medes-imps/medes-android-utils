package fr.medes.android.sample;

import java.io.File;

import android.app.Activity;
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
import android.view.View;
import fr.medes.android.R;
import fr.medes.android.app.MyProgressDialog;
import fr.medes.android.app.MyProgressDialog.Formatter;
import fr.medes.android.os.BaseAsyncTask.Callback;
import fr.medes.android.update.CheckUpdateTask;
import fr.medes.android.update.MarketApp;
import fr.medes.android.util.file.DownloadFileTask;
import fr.medes.android.util.file.FileUtils;

/**
 * A sample activity to show how to combine {@link CheckUpdateTask} and {@link DownloadFileTask} to update an android
 * application over the air.
 * 
 * @author Medes-IMPS
 * 
 */
public class SampleUpdateActivity extends Activity implements View.OnClickListener {

	private static final int DIALOG_DOWNLOAD_ID = 1;

	private MyProgressDialog mProgressDialog;

	private View checkButton;
	private View installButton;

	private RetainObject retain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aml__sample_update);

		checkButton = findViewById(R.id.aml__update_check);
		installButton = findViewById(R.id.aml__update_install);

		checkButton.setOnClickListener(this);
		installButton.setOnClickListener(this);

		retain = (RetainObject) getLastNonConfigurationInstance();
		if (retain == null) {
			retain = new RetainObject();
		}

		installButton.setEnabled(retain.marketApp != null);

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
	public void onClick(View v) {
		final long id = v.getId();

		if (id == R.id.aml__update_check) {
			final String server = "http://192.168.0.43/market";
			String packageName = "org.imogene.android.vecmap";
			retain.checkUpdateTask = new CheckUpdateTask();
			retain.checkUpdateTask.setCallback(mCheckUpdateCallback);
			retain.checkUpdateTask.execute(server, packageName);

		} else if (id == R.id.aml__update_install) {
			final String url = "http://192.168.0.43/market/apk/" + retain.marketApp.getFile();
			final String path = Environment.getExternalStorageDirectory().getPath() + "/" + retain.marketApp.getFile();
			retain.downloadFileTask = new DownloadFileTask();
			retain.downloadFileTask.setCallback(mDownloadFileCallback);
			retain.downloadFileTask.execute(url, path);
		}
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

	private void downloadFinishedOrStopped(File result) {
		dismissDialog(DIALOG_DOWNLOAD_ID);

		checkButton.setEnabled(true);
		installButton.setEnabled(true);

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
				onPostExecute(result);
			} else if (status == Status.RUNNING) {
				checkButton.setEnabled(false);
				installButton.setEnabled(false);
			}
		}

		@Override
		public void onCancelled() {
			// Nothing to do
		}

		@Override
		public void onPostExecute(MarketApp result) {
			checkButton.setEnabled(true);
			installButton.setEnabled(result != null && result.isUpdatable(SampleUpdateActivity.this));
			retain.marketApp = result;
			retain.checkUpdateTask.setCallback(null);
			retain.checkUpdateTask = null;
		}

		@Override
		public void onPreExecute() {
			checkButton.setEnabled(false);
			installButton.setEnabled(false);
			retain.marketApp = null;
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
				checkButton.setEnabled(false);
				installButton.setEnabled(false);
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
