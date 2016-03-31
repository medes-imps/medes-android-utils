package fr.medes.android.content;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.CancellationSignal;
import android.support.v4.os.OperationCanceledException;

public abstract class SimpleCursorLoader<D extends Cursor> extends AsyncTaskLoader<D> {

	final ForceLoadContentObserver mObserver;

	D mCursor;
	CancellationSignal mCancellationSignal;

	public SimpleCursorLoader(Context context) {
		super(context);
		mObserver = new ForceLoadContentObserver();
	}

	abstract protected D getCursor(Context context, CancellationSignal cancellationSignal);

	@Override
	public D loadInBackground() {
		synchronized (this) {
			if (isLoadInBackgroundCanceled()) {
				throw new OperationCanceledException();
			}
			mCancellationSignal = new CancellationSignal();
		}
		try {
			D cursor = getCursor(getContext(), mCancellationSignal);
			if (cursor != null) {
				try {
					// Ensure the cursor window is filled.
					cursor.getCount();
					cursor.registerContentObserver(mObserver);
				} catch (RuntimeException ex) {
					cursor.close();
					throw ex;
				}
			}
			return cursor;
		} finally {
			synchronized (this) {
				mCancellationSignal = null;
			}
		}
	}

	@Override
	public void cancelLoadInBackground() {
		super.cancelLoadInBackground();

		synchronized (this) {
			if (mCancellationSignal != null) {
				mCancellationSignal.cancel();
			}
		}
	}

	@Override
	public void deliverResult(D cursor) {
		if (isReset()) {
			// An async query came in while the loader is stopped
			if (cursor != null) {
				cursor.close();
			}
			return;
		}
		Cursor oldCursor = mCursor;
		mCursor = cursor;

		if (isStarted()) {
			super.deliverResult(cursor);
		}

		if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
			oldCursor.close();
		}
	}

	@Override
	protected void onStartLoading() {
		if (mCursor != null) {
			deliverResult(mCursor);
		}
		if (takeContentChanged() || mCursor == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(D cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		if (mCursor != null && !mCursor.isClosed()) {
			mCursor.close();
		}
		mCursor = null;
	}
}