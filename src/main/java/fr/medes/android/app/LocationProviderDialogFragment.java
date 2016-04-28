package fr.medes.android.app;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import fr.medes.android.R;

public class LocationProviderDialogFragment extends DialogFragment implements LocationListener {

	private static final String ARG_PROVIDER = "provider";

	public static LocationProviderDialogFragment newInstance(String provider) {
		Bundle args = new Bundle();
		args.putString(ARG_PROVIDER, provider);
		LocationProviderDialogFragment fragment = new LocationProviderDialogFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private static final int REQUEST_PERMISSION = 1;

	private LocationManager mLocationManager;
	private String mProvider;

	private Callback mCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProvider = getArguments().getString(ARG_PROVIDER);

		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
		if (mLocationManager.isProviderEnabled(mProvider)) {
			if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				mLocationManager.requestLocationUpdates(mProvider, 0, 0, this);
			} else {
				requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
			}
		} else {
			Toast.makeText(getActivity(), getString(R.string.aml__provider_disabled, mProvider), Toast.LENGTH_LONG).show();
			dismiss();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			mLocationManager.removeUpdates(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_PERMISSION:
				for (int grant : grantResults) {
					if (grant != PackageManager.PERMISSION_GRANTED) {
						Toast.makeText(getContext(), R.string.aml__permission_not_granted, Toast.LENGTH_LONG).show();
						onLocationChanged(null);
						return;
					}
				}
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		dismissAllowingStateLoss();
		if (mCallback != null) {
			mCallback.onLocationSet(location, getTag());
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
