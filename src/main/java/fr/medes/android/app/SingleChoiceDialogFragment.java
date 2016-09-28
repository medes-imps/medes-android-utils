package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class SingleChoiceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private static final String ARG_TITLE = "title";
	private static final String ARG_ITEMS = "items";
	private static final String ARG_CHECKED_ITEM = "checkedItem";

	public static SingleChoiceDialogFragment newInstance(String title, CharSequence[] items, int checkedItem) {
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TITLE, title);
		bundle.putCharSequenceArray(ARG_ITEMS, items);
		bundle.putInt(ARG_CHECKED_ITEM, checkedItem);
		SingleChoiceDialogFragment fragment = new SingleChoiceDialogFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private Callback mCallback;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String title = getArguments().getString(ARG_TITLE);
		final CharSequence[] items = getArguments().getCharSequenceArray(ARG_ITEMS);
		final int checkedItem = getArguments().getInt(ARG_CHECKED_ITEM);
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setSingleChoiceItems(items, checkedItem, this)
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
		if (mCallback != null) {
			mCallback.onCheckedItemSet(which, getTag());
		}
		dialog.dismiss();
	}

	public interface Callback {

		void onCheckedItemSet(int checkedItem, String tag);
	}
}
