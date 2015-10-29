package fr.medes.android.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

/**
 * Created by julien on 23/10/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

	private static final String ARG_YEAR = "year";
	private static final String ARG_MONTH_OF_YEAR = "month";
	private static final String ARG_DAY_OF_MONTH = "day";
	private static final String ARG_TAG = "tag";

	public static DatePickerFragment newInstance(int year, int monthOfYear, int dayOfMonth, String tag) {
		final Bundle bundle = new Bundle();
		bundle.putInt(ARG_YEAR, year);
		bundle.putInt(ARG_MONTH_OF_YEAR, monthOfYear);
		bundle.putInt(ARG_DAY_OF_MONTH, dayOfMonth);
		bundle.putString(ARG_TAG, tag);
		final DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private Callback mCallback;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int year = getArguments().getInt(ARG_YEAR);
		final int monthOfYear = getArguments().getInt(ARG_MONTH_OF_YEAR);
		final int dayOfMonth = getArguments().getInt(ARG_DAY_OF_MONTH);
		return new DatePickerDialog(getContext(), this, year, monthOfYear, dayOfMonth);
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
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		if (mCallback == null) {
			return;
		}
		final String tag = getArguments().getString(ARG_TAG);
		mCallback.onDateSet(year, monthOfYear, dayOfMonth, tag);
	}

	public interface Callback {
		void onDateSet(int year, int monthOfYear, int dayOfMonth, String tag);
	}
}
