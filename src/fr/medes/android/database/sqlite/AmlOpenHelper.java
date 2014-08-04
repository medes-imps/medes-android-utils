package fr.medes.android.database.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

abstract public class AmlOpenHelper extends SQLiteOpenHelper {

	private final Context mContext;

	public AmlOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	public final Context getContext() {
		return mContext;
	}

	abstract public <T extends Cursor> T query(Uri uri);
}
