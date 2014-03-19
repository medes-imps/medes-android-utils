package fr.medes.android.preference;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import fr.medes.android.util.encryption.EncryptionManager;

/**
 * A {@link Preference} that allows for encrypted string visualization.
 * <p>
 * The encrypted string is decrypted and displayed in the summary.
 */
public class EncryptedPreference extends Preference {

	private final EncryptionManager mManager;

	public EncryptedPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mManager = EncryptionManager.getInstance();
	}

	@Override
	public CharSequence getSummary() {
		final String enc = getPersistedString(null);
		if (enc != null) {
			return new String(mManager.decrypt(Base64.decodeBase64(enc.getBytes())));
		}
		return getPersistedString(getContext().getString(android.R.string.unknownName));
	}

}
