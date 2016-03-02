package fr.medes.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import fr.medes.android.R;

public class DateTimePickerDialog extends AlertDialog implements OnClickListener, OnCheckedChangeListener, OnDateChangedListener,
		OnTimeChangedListener {

	private static final String HOUR = "hour";
	private static final String MINUTE = "minute";
	private static final String IS_24_HOUR = "is24hour";
	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";

	private final Switch mSwitch;
	private final TextView mTextView;
	private final DatePicker mDatePicker;
	private final TimePicker mTimePicker;
	private final OnDateTimeSetListener mDateTimeSetListener;

	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateTimeSetListener {

		/**
		 * @param datePicker  The date view associated with this listener.
		 * @param timePicker  The time view associated with this listener.
		 * @param year        The year that was set.
		 * @param monthOfYear The month that was set (0-11) for compatibility with {@link java.util.Calendar}.
		 * @param dayOfMonth  The day of the month that was set.
		 * @param hourOfDay   The hour that was set.
		 * @param minute      The minute that was set.
		 */
		void onDateTimeSet(DatePicker datePicker, TimePicker timePicker, int year, int monthOfYear, int dayOfMonth,
						   int hourOfDay, int minute);
	}

	/**
	 * @param context      The context the dialog is to run in.
	 * @param listener     How the parent is notified that the date is set.
	 * @param year         The initial year of the dialog.
	 * @param monthOfYear  The initial month of the dialog.
	 * @param dayOfMonth   The initial day of the dialog.
	 * @param hourOfDay    The initial hour of the dialog.
	 * @param minute       The initial minute of the dialog.
	 * @param is24HourView Whether this is a 24 hour view, or AM/PM.
	 */
	public DateTimePickerDialog(Context context, OnDateTimeSetListener listener, int year, int monthOfYear, int dayOfMonth,
								int hourOfDay, int minute, boolean is24HourView) {
		this(context, 0, listener, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView);
	}

	static int resolveDialogTheme(Context context, int resid) {
		if (resid == 0) {
			final TypedValue outValue = new TypedValue();
			context.getTheme().resolveAttribute(android.R.attr.datePickerDialogTheme, outValue, true);
			context.getTheme().resolveAttribute(android.R.attr.timePickerDialogTheme, outValue, true);
			return outValue.resourceId;
		} else {
			return resid;
		}
	}

	/**
	 * @param context      The context the dialog is to run in.
	 * @param theme        the theme to apply to this dialog
	 * @param listener     How the parent is notified that the date is set.
	 * @param year         The initial year of the dialog.
	 * @param monthOfYear  The initial month of the dialog.
	 * @param dayOfMonth   The initial day of the dialog.
	 * @param hourOfDay    The initial hour of the dialog.
	 * @param minute       The initial minute of the dialog.
	 * @param is24HourView Whether this is a 24 hour view, or AM/PM.
	 */
	public DateTimePickerDialog(Context context, int theme, OnDateTimeSetListener listener, int year, int monthOfYear,
								int dayOfMonth, int hourOfDay, int minute, boolean is24HourView) {
		super(context, resolveDialogTheme(context, theme));

		mDateTimeSetListener = listener;

		final Context themeContext = getContext();
		final LayoutInflater inflater = LayoutInflater.from(themeContext);
		final View view = inflater.inflate(R.layout.aml__date_time_picker_dialog, null);
		setView(view);
		setButton(BUTTON_POSITIVE, themeContext.getString(android.R.string.ok), this);
		setButton(BUTTON_NEGATIVE, themeContext.getString(android.R.string.cancel), this);

		// initialize state
		mDatePicker = (DatePicker) view.findViewById(R.id.aml__datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);

		mTimePicker = (TimePicker) view.findViewById(R.id.aml__timePicker);
		mTimePicker.setIs24HourView(is24HourView);
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minute);
		mTimePicker.setOnTimeChangedListener(this);

		mTextView = (TextView) view.findViewById(R.id.aml__dateTimePickerHeader);

		mSwitch = (Switch) view.findViewById(R.id.aml__switchDateTime);
		mSwitch.setOnCheckedChangeListener(this);
		mSwitch.setChecked(true);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		/* do nothing */
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		/* do nothing */
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mDatePicker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
		mTimePicker.setVisibility(isChecked ? View.GONE : View.VISIBLE);
		mTextView.setText(isChecked ? R.string.aml__dateTimePicker_date : R.string.aml__dateTimePicker_time);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case BUTTON_POSITIVE:
				if (mDateTimeSetListener != null) {
					mDateTimeSetListener.onDateTimeSet(mDatePicker, mTimePicker, mDatePicker.getYear(), mDatePicker.getMonth(),
							mDatePicker.getDayOfMonth(), mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute());
				}
				break;
			case BUTTON_NEGATIVE:
				cancel();
				break;
		}
	}

	public void updateDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
		mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
		mTimePicker.setCurrentHour(hourOfDay);
		mTimePicker.setCurrentMinute(minute);
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(YEAR, mDatePicker.getYear());
		state.putInt(MONTH, mDatePicker.getMonth());
		state.putInt(DAY, mDatePicker.getDayOfMonth());
		state.putInt(HOUR, mTimePicker.getCurrentHour());
		state.putInt(MINUTE, mTimePicker.getCurrentMinute());
		state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		final int year = savedInstanceState.getInt(YEAR);
		final int month = savedInstanceState.getInt(MONTH);
		final int day = savedInstanceState.getInt(DAY);
		final int hour = savedInstanceState.getInt(HOUR);
		final int minute = savedInstanceState.getInt(MINUTE);
		mDatePicker.init(year, month, day, this);
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(minute);
		mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
	}

}
