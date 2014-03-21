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
	private final OnGeoSetListener mCallBack;

	/**
	 * The callback used to indicate the user is done filling in the coordinates.
	 */
	public interface OnGeoSetListener {

		/**
		 * @param view The view associated with this listener.
		 * @param latitude The latitude that was set.
		 * @param longitude The longitude that was set.
		 */
		public void onGeoSet(GeoPicker view, Double latitude, Double longitude);
	}

	/**
	 * 
	 * @param context The context the dialog is to run in.
	 * @param callBack How the parent is notified that the coordinates are set.
	 * @param latitude The initial latitude of the dialog.
	 * @param longitude The initial longitude of the dialog.
	 */
	public GeoPickerDialog(Context context, OnGeoSetListener callBack, double latitude, double longitude) {
		this(context, callBack, 0, latitude, longitude);
	}

	/**
	 * 
	 * @param context The context the dialog is to run in.
	 * @param callback How the parent is notified that the coordinates are set.
	 * @param theme The theme to apply to this dialog.
	 * @param latitude The initial latitude of the dialog.
	 * @param longitude The initial longitude of the dialog.
	 */
	public GeoPickerDialog(Context context, OnGeoSetListener callback, int theme, double latitude, double longitude) {
		super(context, theme);

		setTitle(R.string.aml__dialog_geopicker_title);
		setButton(DialogInterface.BUTTON_POSITIVE, context.getText(android.R.string.ok), this);
		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.aml__geo_picker_dialog, null);
		setView(view);

		// initialize state
		mCallBack = callback;
		mGeoPicker = (GeoPicker) view.findViewById(R.id.aml__geoPicker);
		mGeoPicker.setLatitude(latitude);
		mGeoPicker.setLongitude(longitude);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		tryNotifyDateSet();
	}

	private void tryNotifyDateSet() {
		if (mCallBack != null) {
			mGeoPicker.clearFocus();
			mCallBack.onGeoSet(mGeoPicker, mGeoPicker.getLatitude(), mGeoPicker.getLongitude());
		}
	}

	@Override
	protected void onStop() {
		tryNotifyDateSet();
		super.onStop();
	}

}
