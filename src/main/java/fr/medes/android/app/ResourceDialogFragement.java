package fr.medes.android.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.io.Serializable;

import fr.medes.android.widget.ResourceAdapter;

public class ResourceDialogFragement extends DialogFragment implements DialogInterface.OnClickListener {

	private static final String ARG_DATA = "data";
	private static final String ARG_RESOURCE = "resource";
	private static final String ARG_TO = "to";

	public static ResourceDialogFragement newInstance(int[][] data, @LayoutRes int resource, @IdRes int[] to) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_DATA, new IntMatrix(data));
		bundle.putInt(ARG_RESOURCE, resource);
		bundle.putIntArray(ARG_TO, to);
		ResourceDialogFragement fragement = new ResourceDialogFragement();
		fragement.setArguments(bundle);
		return fragement;
	}

	private Callback mCallback;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final int[][] data = ((IntMatrix) getArguments().getSerializable(ARG_DATA)).matrix;
		final int resource = getArguments().getInt(ARG_RESOURCE);
		final int[] to = getArguments().getIntArray(ARG_TO);
		return new AlertDialog.Builder(getActivity())
				.setAdapter(new ResourceAdapter(getContext(), data, resource, to), this)
				.create();
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
	public void onClick(DialogInterface dialog, int which) {
		if (mCallback != null) {
			mCallback.onClick(which, getTag());
		}
	}

	public interface Callback {

		void onClick(int which, String tag);
	}

	public static class IntMatrix implements Serializable {

		public static final int serialVersionUID = 1;

		private final int[][] matrix;

		public IntMatrix(int[][] matrix) {
			this.matrix = matrix;
		}

	}
}
