package fr.medes.android.preference;

import android.support.annotation.StringRes;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

abstract public class MyPreferenceFragmentCompat extends PreferenceFragmentCompat {

	public static final String ARG_SHOULD_INFLATE = "should_inflate";

	public <T extends Preference> T findPreference(@StringRes int resId) {
		return (T) super.findPreference(getString(resId));
	}
}
