package fr.medes.android.app;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.medes.android.R;

public class MediaCapture extends Activity implements OnScanCompletedListener {

	public static final String EXTRA_MEDIA_CAPTURE_TYPE = "media_capture_type";

	public static final int CAPTURE_IMAGE = 0;
	public static final int CAPTURE_VIDEO = 1;

	private static final String TAG = MediaCapture.class.getName();

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2;

	private File mediaFile = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aml__media_content);

		if (savedInstanceState != null) {
			return;
		}

		int captureType = getCaptureType();
		if (captureType != CAPTURE_IMAGE && captureType != CAPTURE_VIDEO) {
			cancelOrFailed();
			return;
		}

		Intent intent;
		if (captureType == CAPTURE_IMAGE) {
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		} else {
			intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		}
		if (intent.resolveActivity(getPackageManager()) == null) {
			cancelOrFailed();
			return;
		}

		try {
			if (captureType == CAPTURE_IMAGE) {
				mediaFile = createImageFile();
			} else {
				mediaFile = createVideoFile();
			}
		} catch (IOException e) {
			cancelOrFailed();
			return;
		}

		// Continue only if the File was successfully created
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
		startActivityForResult(intent, captureType == CAPTURE_IMAGE ? CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE : CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("ImageCapture_path", mediaFile);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mediaFile = (File) savedInstanceState.getSerializable("ImageCapture_path");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE || requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) && resultCode == RESULT_OK) {
			MediaScannerConnection.scanFile(this, new String[]{mediaFile.getAbsolutePath()}, null, this);
		} else {
			cancelOrFailed();
		}
	}

	public int getCaptureType() {
		return getIntent().getIntExtra(EXTRA_MEDIA_CAPTURE_TYPE, -1);
	}

	private void cancelOrFailed() {
		if (mediaFile != null) {
			boolean deleted = mediaFile.delete();
			if (deleted) {
				Log.i(TAG, "File correctly deleted");
			} else {
				Log.i(TAG, "Could not delete file");
			}
		}
		setResult(RESULT_CANCELED);
		finish();

	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		if (uri != null) {
			setResult(RESULT_OK, new Intent().setData(uri));
			finish();
		} else {
			cancelOrFailed();
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp;
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
	}

	private File createVideoFile() throws IOException {
		// Create a video file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
		String imageFileName = "MPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return File.createTempFile(
				imageFileName,  /* prefix */
				".mpg",         /* suffix */
				storageDir      /* directory */
		);
	}

}
