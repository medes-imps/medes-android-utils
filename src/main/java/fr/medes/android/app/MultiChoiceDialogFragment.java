package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class MultiChoiceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {

	private static final String ARG_TITLE = "title";
	private static final String ARG_ITEMS = "items";
	private static final String ARG_CHECKED_ITEMS = "checkedItems";

	public static MultiChoiceDialogFragment newInstance(String title, CharSequence[] items, boolean[] checkedItems) {
		Bundle bundle = new Bundle();
		bundle.putString(ARG_TITLE, title);
		bundle.putCharSequenceArray(ARG_ITEMS, items);
		bundle.putBooleanArray(ARG_CHECKED_ITEMS, checkedItems);
		MultiChoiceDialogFragment fragment = new MultiChoiceDialogFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private Callback mCallback;
	private boolean[] mCheckedItems;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final String title = getArguments().getString(ARG_TITLE);
		final CharSequence[] items = getArguments().getCharSequenceArray(ARG_ITEMS);
		mCheckedItems = getArguments().getBooleanArray(ARG_CHECKED_ITEMS);
		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMultiChoiceItems(items, mCheckedItems, this)
				.setPositiveButton(android.R.string.ok, this)
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
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		mCheckedItems[which] = isChecked;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (mCallback == null) {
			return;
		}

		mCallback.onCheckedItemsSet(mCheckedItems, getTag());
	}


	public interface Callback {

		void onCheckedItemsSet(boolean[] checkedItems, String tag);
	}

}
