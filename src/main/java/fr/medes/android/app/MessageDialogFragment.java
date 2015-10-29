package fr.medes.android.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by julien on 23/10/15.
 */
public class MessageDialogFragment extends DialogFragment {

	private static final String ARG_TITLE = "title";
	private static final String ARG_MESSAGE = "message";

	public static MessageDialogFragment newInstance(String title, String message) {
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TITLE, title);
		bundle.putString(ARG_MESSAGE, message);
		MessageDialogFragment fragment = new MessageDialogFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String title = getArguments().getString(ARG_TITLE);
		final String message = getArguments().getString(ARG_MESSAGE);
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.create();
	}
}
