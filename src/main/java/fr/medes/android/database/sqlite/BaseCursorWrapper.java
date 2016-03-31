package fr.medes.android.database.sqlite;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import fr.medes.android.util.FormatHelper;

public class BaseCursorWrapper extends CursorWrapper {

	public BaseCursorWrapper(Cursor cursor) {
		super(cursor);
	}

	public byte[] getBlob(String columnName) {
		return getBlob(getColumnIndexOrThrow(columnName));
	}

	public String getString(String columnName) {
		return getString(getColumnIndexOrThrow(columnName));
	}

	public long getLong(String columnName) {
		return getLong(getColumnIndexOrThrow(columnName));
	}

	public Date getDate(String columnName) {
		Long date = getAsLong(columnName);
		if (date != null) {
			return new Date(date);
		}
		return null;
	}

	public double getDouble(String columnName) {
		return getDouble(getColumnIndexOrThrow(columnName));
	}

	public int getInt(String columnName) {
		return getInt(getColumnIndexOrThrow(columnName));
	}

	public boolean getBoolean(String columnName) {
		return getInt(columnName) != 0;
	}

	public Long getAsLong(String columnName) {
		return FormatHelper.toLong(getString(columnName));
	}

	public Integer getAsInteger(String columnName) {
		return FormatHelper.toInteger(getString(columnName));
	}

	public Float getAsFloat(String columnName) {
		return FormatHelper.toFloat(getString(columnName));
	}

	public Boolean getAsBoolean(String columnName) {
		return FormatHelper.toBoolean(getString(columnName));
	}
}
