package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class MessageDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	public static final String ARG_TITLE = "title";
	public static final String ARG_MESSAGE = "message";
	public static final String ARG_CANCEL = "cancel";
	public static final String ARG_TAG = "tag";

	public Callback mCallback;

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
		final boolean cancel = getArguments().getBoolean(ARG_CANCEL);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, this);
		if (cancel) {
			builder.setNegativeButton(android.R.string.cancel, this);
		}
		return builder.create();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		// Activities containing this fragment MAY implement its callbacks.
		if (context instanceof Callback) {
			mCallback = (Callback) context;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		mCallback = null;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mCallback != null) {
			mCallback.onClickDialog(which, getArguments().get(ARG_TAG));
		}
	}

	public interface Callback {
		void onClickDialog(int which, Object tag);
	}
}
