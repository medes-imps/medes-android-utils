package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import fr.medes.android.R;

public class BooleanDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private static final String ARG_TITLE = "title";
	private static final String ARG_VALUE = "value";

	private Callback mCallback;

	public static BooleanDialogFragment newInstance(String title, Boolean value) {
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TITLE, title);
		bundle.putInt(ARG_VALUE, value != null ? (value ? 0 : 1) : -1);
		BooleanDialogFragment fragment = new BooleanDialogFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int value = getArguments().getInt(ARG_VALUE);
		final String title = getArguments().getString(ARG_TITLE);
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setSingleChoiceItems(R.array.aml__yes_no, value, this)
				.setNegativeButton(android.R.string.cancel, null)
				.create();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

		// Activities containing this fragment must implement its callbacks.
		if (!(context instanceof Callback)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		mCallback = (Callback) context;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		mCallback = null;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mCallback == null) {
			return;
		}
		mCallback.onBooleanSet(which == 0, getTag());
		dialog.dismiss();
	}

	public interface Callback {

		void onBooleanSet(Boolean value, String tag);
	}
}
