package fr.medes.android.util.encryption;

public class EncryptionManager implements EncryptionProvider {

	private static EncryptionManager instance = null;

	private EncryptionProvider proxy;

	public static EncryptionManager getInstance() {
		if (instance == null) {
			instance = new EncryptionManager();
		}
		return instance;
	}

	public void setProxy(EncryptionProvider proxy) {
		this.proxy = proxy;
	}

	public String getProviderName() {
		return proxy.getName();
	}

	@Override
	public byte[] decrypt(byte[] encrypted) {
		return proxy.decrypt(encrypted);
	}

	@Override
	public byte[] encrypt(byte[] decrypted) {
		return proxy.encrypt(decrypted);
	}

	@Override
	public String getName() {
		return proxy.getName();
	}
}
