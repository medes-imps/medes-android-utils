package fr.medes.android.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An easy adapter to resource data to views defined in an XML file.
 * <p/>
 * If no appropriate binding can be found, an {@link IllegalStateException} is thrown.
 */
public class ResourceAdapter extends BaseAdapter {

	private int[] mTo;
	private final int[][] mData;
	private final LayoutInflater mInflater;
	private final int mResource;

	/**
	 * Constructor
	 *
	 * @param context  The context where the View associated with this SimpleAdapter is running
	 * @param data     An array of integer arrays. Each entry in the array corresponds to one row in the list.
	 * @param resource Resource identifier of a view layout that defines the views for this list
	 *                 item. The layout file should include at least those named views defined in "to"
	 * @param to       The views that should display resource from the resource array.
	 */
	public ResourceAdapter(Context context, int[][] data, @LayoutRes int resource, @IdRes int[] to) {
		mInflater = LayoutInflater.from(context);
		mData = data;
		mResource = resource;
		mTo = to;
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		return mData[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(mInflater, position, convertView, parent, mResource);
	}

	private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = inflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}

		bindView(position, v);

		return v;
	}

	private void bindView(int position, View view) {
		final int[] item = (int[]) getItem(position);

		final int[] to = mTo;
		for (int i = 0; i < to.length; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final int data = item[i];
				if (v instanceof TextView) {
					// Note: keep the instanceof TextView check at the bottom of these
					// ifs since a lot of views are TextViews (e.g. CheckBoxes).
					((TextView) v).setText(data);
				} else if (v instanceof ImageView) {
					((ImageView) v).setImageResource(data);
				} else {
					throw new IllegalStateException(v.getClass().getName() + " is not a " +
							" view that can be bounds by this ResourceAdapter");
				}
			}
		}
	}

}