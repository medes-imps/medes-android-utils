package fr.medes.android.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import fr.medes.android.R;

public class LocationProviderDialogFragment extends DialogFragment implements LocationListener {

	private static final String ARG_PROVIDER = "provider";
	private static final String ARG_TAG = "tag";

	public static LocationProviderDialogFragment newInstance(String provider, String tag) {
		Bundle args = new Bundle();
		args.putString(ARG_PROVIDER, provider);
		args.putString(ARG_TAG, tag);
		LocationProviderDialogFragment fragment = new LocationProviderDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private LocationManager mLocationManager;
	private String mProvider;

	private Callback mCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProvider = getArguments().getString(ARG_PROVIDER);

		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (!mLocationManager.isProviderEnabled(mProvider)) {
			Toast.makeText(getActivity(), getString(R.string.aml__provider_disabled, mProvider), Toast.LENGTH_LONG).show();
			dismiss();
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getString(R.string.aml__obtaining_location));
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(this);
		return dialog;
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
	public void onResume() {
		super.onResume();
		Log.i("TAG", "request location updates");
		mLocationManager.requestLocationUpdates(mProvider, 0, 0, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i("TAG", "remove updates");
		mLocationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		dismiss();
		if (mCallback != null) {
			mCallback.onLocationSet(location, getArguments().getString(ARG_TAG));
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// Nothing to do
	}

	@Override
	public void onProviderEnabled(String provider) {
		// Nothing to do
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Nothing to do
	}

	public interface Callback {
		void onLocationSet(Location location, String tag);
	}
}
