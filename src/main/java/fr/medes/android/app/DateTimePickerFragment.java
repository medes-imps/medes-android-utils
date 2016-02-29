package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class DateTimePickerFragment extends DialogFragment implements DateTimePickerDialog.OnDateTimeSetListener {

	private static final String ARG_YEAR = "year";
	private static final String ARG_MONTH_OF_YEAR = "month";
	private static final String ARG_DAY_OF_MONTH = "day";
	private static final String ARG_HOUR_OF_DAY = "hour";
	private static final String ARG_MINUTE = "minute";
	private static final String ARG_TAG = "tag";

	public static DateTimePickerFragment newInstance(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, String tag) {
		final Bundle bundle = new Bundle();
		bundle.putInt(ARG_YEAR, year);
		bundle.putInt(ARG_MONTH_OF_YEAR, monthOfYear);
		bundle.putInt(ARG_DAY_OF_MONTH, dayOfMonth);
		bundle.putInt(ARG_HOUR_OF_DAY, hourOfDay);
		bundle.putInt(ARG_MINUTE, minute);
		bundle.putString(ARG_TAG, tag);
		final DateTimePickerFragment fragment = new DateTimePickerFragment();
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
		final int hourOfDay = getArguments().getInt(ARG_HOUR_OF_DAY);
		final int minute = getArguments().getInt(ARG_MINUTE);
		final boolean is24HourView = DateFormat.is24HourFormat(getActivity());
		return new DateTimePickerDialog(getContext(), this, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView);
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
	public void onDateTimeSet(DatePicker datePicker, TimePicker timePicker, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
		if (mCallback == null) {
			return;
		}
		final String tag = getArguments().getString(ARG_TAG);
		mCallback.onDateTimeSet(year, monthOfYear, dayOfMonth, hourOfDay, minute, tag);
	}

	public interface Callback {
		void onDateTimeSet(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute, String tag);
	}
}