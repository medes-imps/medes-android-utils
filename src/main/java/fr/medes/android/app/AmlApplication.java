package fr.medes.android.app;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

public abstract class AmlApplication extends MultiDexApplication {

	private static Context mContext;

	public static Context getContext() {
		return mContext;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
	}

}
