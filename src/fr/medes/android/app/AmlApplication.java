package fr.medes.android.app;

import android.app.Application;
import fr.medes.android.database.sqlite.AmlOpenHelper;

abstract public class AmlApplication extends Application {

	abstract public AmlOpenHelper getOpenHelper();

}
