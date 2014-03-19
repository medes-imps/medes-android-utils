package fr.medes.android.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import fr.medes.android.R;
import fr.medes.android.widget.GeoPicker;

public class GeoPickerDialog extends AlertDialog implements OnClickListener {

	private final GeoPicker mGeoPicker;
	private final OnGeoSetListener mListener;

	public interface OnGeoSetListener {
		public void onGeoSet(GeoPicker view, Double latitude, Double longitude);
	}

	public GeoPickerDialog(Context context, OnGeoSetListener listener, double latitude, double longitude) {
		this(context, listener, R.style.Theme_Dialog_Alert, latitude, longitude);
	}

	public GeoPickerDialog(Context context, OnGeoSetListener callback, int theme, double latitude, double longitude) {
		super(context, theme);

		setTitle(R.string.aml__dialog_geopicker_title);
		setButton(DialogInterface.BUTTON_POSITIVE, context.getText(android.R.string.ok), this);
		setButton(DialogInterface.BUTTON_NEGATIVE, context.getText(android.R.string.cancel), this);
		setIcon(android.R.drawable.ic_dialog_map);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.aml__geo_picker_dialog, null);
		setView(view);

		// initialize state
		mListener = callback;
		mGeoPicker = (GeoPicker) view.findViewById(R.id.aml__geoPicker);
		mGeoPicker.setLatitude(latitude);
		mGeoPicker.setLongitude(longitude);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_POSITIVE && mListener != null) {
			mGeoPicker.clearFocus();
			mListener.onGeoSet(mGeoPicker, mGeoPicker.getLatitude(), mGeoPicker.getLongitude());
		}
	}

}
