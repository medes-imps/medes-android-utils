package android.support.v4.app;

import android.view.View;

/**
 * List fragment utility class
 */
public class ListFragmentUtils {

	/**
	 * Convenient method to change custom list identifiers to internal identifiers not accessible
	 * otherwise.
	 *
	 * @param view                The containing the views.
	 * @param emptyId             The custom empty view identifier.
	 * @param progressContainerId The custom progress container view identifier.
	 * @param listContainerId     The custom list container view identifier.
	 */
	public static void setupIds(View view, int emptyId, int progressContainerId, int listContainerId) {
		View v = view.findViewById(emptyId);
		if (v != null) {
			v.setId(ListFragment.INTERNAL_EMPTY_ID);
		}
		v = view.findViewById(progressContainerId);
		if (v != null) {
			v.setId(ListFragment.INTERNAL_PROGRESS_CONTAINER_ID);
		}
		v = view.findViewById(listContainerId);
		if (v != null) {
			v.setId(ListFragment.INTERNAL_LIST_CONTAINER_ID);
		}
	}

}
