package fr.medes.android.app;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

	private static final String ARG_HOUR = "hour";
	private static final String ARG_MINUTE = "minute";

	public static TimePickerFragment newInstance(int hourOfDay, int minute) {
		final Bundle bundle = new Bundle();
		bundle.putInt(ARG_HOUR, hourOfDay);
		bundle.putInt(ARG_MINUTE, minute);
		final TimePickerFragment fragment = new TimePickerFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private Callback mCallback;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int hour = getArguments().getInt(ARG_HOUR);
		final int minute = getArguments().getInt(ARG_MINUTE);
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
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
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		if (mCallback == null) {
			return;
		}
		mCallback.onTimeSet(hourOfDay, minute, getTag());
	}

	public interface Callback {
		void onTimeSet(int hourOfDay, int minute, String tag);
	}
}