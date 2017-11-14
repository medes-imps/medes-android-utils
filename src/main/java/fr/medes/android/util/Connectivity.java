package fr.medes.android.util;

import android.content.Context;

/**
 * Connectivity utility class
 */
public interface Connectivity {

	/**
	 * Check whether a data connection is available or not.
	 *
	 * @param context  The context of the application.
	 * @param listener The listener to send the result to.
	 */
	void isConnected(Context context, Listener listener);

	/**
	 * Listener interface to implement to receive the connectivity status result.
	 */
	interface Listener {

		/**
		 * Called when the connection status is known.
		 *
		 * @param connected Whether a data connection is available or not.
		 */
		void onConnected(boolean connected);
	}

	/**
	 * A registry of Connectivity. A registry implementation will typically delegate to the global
	 * instance.
	 */
	interface Registry {

		/**
		 * Returns the currently registered connectivity instance.
		 *
		 * @return the current connectivity instance
		 */
		Connectivity getCurrent();

		/**
		 * Set the current connectivity instance.
		 *
		 * @param connectivity the new current connectivity instance
		 */
		void setCurrent(Connectivity connectivity);

		Registry INSTANCE = new ConnectivityCheckerRegistryImpl();
	}

	/**
	 * An extensible implementation of a connectivity registry.
	 */
	class ConnectivityCheckerRegistryImpl implements Registry {

		private Connectivity connectivity;

		@Override
		public Connectivity getCurrent() {
			return connectivity;
		}

		@Override
		public void setCurrent(Connectivity connectivity) {
			this.connectivity = connectivity;
		}
	}

}
