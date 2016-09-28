package fr.medes.android.util.encryption;

public interface EncryptionProvider {

	String getName();

	byte[] encrypt(byte[] decrypted);

	byte[] decrypt(byte[] encrypted);
}
