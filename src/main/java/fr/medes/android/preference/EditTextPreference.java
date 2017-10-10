package fr.medes.android.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * A {@link Preference} that allows for string input.
 * <p/>
 * The string input is displayed in the summary.
 */
public class EditTextPreference extends android.support.v7.preference.EditTextPreference {

	public EditTextPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EditTextPreference(Context context) {
		super(context);
	}

	@Override
	public CharSequence getSummary() {
		String summary = super.getSummary().toString();
		return String.format(summary, getText());
	}

}
