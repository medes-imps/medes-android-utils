package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import fr.medes.android.widget.GeoPicker;

public class GeoPickerDialogFragment extends DialogFragment implements GeoPickerDialog.OnGeoSetListener {

	private static final String ARG_LATITUDE = "latitude";
	private static final String ARG_LONGITUDE = "longitude";

	public static GeoPickerDialogFragment newInstance(double latitude, double longitude) {
		Bundle bundle = new Bundle();
		bundle.putDouble(ARG_LATITUDE, latitude);
		bundle.putDouble(ARG_LONGITUDE, longitude);
		GeoPickerDialogFragment fragment = new GeoPickerDialogFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	private Callback mCallback;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final double latitude = getArguments().getDouble(ARG_LATITUDE);
		final double longitude = getArguments().getDouble(ARG_LONGITUDE);
		return new GeoPickerDialog(getContext(), this, latitude, longitude);
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
	public void onGeoSet(GeoPicker view, double latitude, double longitude) {
		if (mCallback == null) {
			return;
		}
		mCallback.onGeoSet(latitude, longitude, getTag());
	}

	public interface Callback {
		void onGeoSet(double latitude, double longitude, String tag);
	}
}
