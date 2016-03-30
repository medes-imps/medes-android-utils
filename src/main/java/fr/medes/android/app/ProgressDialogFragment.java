package fr.medes.android.app;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

	public static final String ARG_TITLE = "title";
	public static final String ARG_MESSAGE = "message";
	public static final String ARG_MAX = "max";
	public static final String ARG_CANCELABLE = "cancelable";
	public static final String ARG_STYLE = "style";
	public static final String ARG_NUMBER_FORMAT = "progressNumberFormat";

	public static ProgressDialogFragment newInstance() {
		return new ProgressDialogFragment();
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		boolean cancelable = args.getBoolean(ARG_CANCELABLE, false);
		String title = args.getString(ARG_TITLE);
		String message = args.getString(ARG_MESSAGE);
		int style = args.getInt(ARG_STYLE, ProgressDialog.STYLE_HORIZONTAL);
		int max = args.getInt(ARG_MAX);
		String numberFormat = args.getString(ARG_NUMBER_FORMAT);

		setCancelable(cancelable);

		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(style);
		dialog.setMax(max);
		if (numberFormat != null) {
			dialog.setProgressNumberFormat(numberFormat);
		}

		return dialog;
	}

	public void setProgress(int value) {
		ProgressDialog dialog = (ProgressDialog) getDialog();
		if (dialog != null) {
			dialog.setProgress(value);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		Callback callback = getProgressDialogFragmentCallback();
		if (callback != null) {
			callback.onCancel();
		}
	}

	private Callback getProgressDialogFragmentCallback() {
		Activity activity = getActivity();
		if (activity == null) {
			return null;
		}

		if (activity instanceof Callback) {
			return (Callback) activity;
		}
		return null;
	}

	public interface Callback {
		void onCancel();
	}

}
