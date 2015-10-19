package fr.medes.android.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * A {@link Preference} that allows for string input.
 * <p>
 * The string input is displayed in the summary.
 */
public class MyEditTextPreference extends EditTextPreference {

	public MyEditTextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public CharSequence getSummary() {
		return getPersistedString(getContext().getString(android.R.string.unknownName));
	}

}
