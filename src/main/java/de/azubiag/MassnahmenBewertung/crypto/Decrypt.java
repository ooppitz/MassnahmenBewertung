package de.azubiag.MassnahmenBewertung.crypto;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt {

	/* Die Methode analysiert den cipherText, um festzustellen, welcher Crypto-Algorithmus verwendet wurde.
	 * Die Entschlüsselung geschieht mit dem passenden Algorithmus.
	 * 
	 * @return Klartext-String oder null, wenn ein Fehler aufgetreten ist
	 */
	public static String decrypt_any_type(String cipherText) {
		
		String decrypted_text = null;

		// NOTE: Dieser Alg. kann dazu führen, dass bei Verschlüsselung mit Option B die Payload verändert wird.
		// remove cipherText padding
		cipherText = cipherText.replace("-", "").replace("<", "").replace(">", "").replace(" ", "").replace("\n", "");
		
		// First Char in cipherText indicates type of encryption
		try {
			switch (cipherText.charAt(0)) {
			case 'A':
				decrypted_text = decrypt_type_A(cipherText.substring(1));
				break;
			case 'B':
				decrypted_text = decrypt_type_B(cipherText.substring(1));
				break;
			}
		} catch (IllegalArgumentException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException 
				| NullPointerException | InvocationTargetException e) {
		}

		return decrypted_text;
	}

	/*
	 * Dekodiert einen String mit dem AES Algorithmus.
	 * 
	 * Der mit AES kodierte String kann nur folgende Zeichen enthalten "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
	 */
	public static String decrypt_type_A(String encrypted_text) throws NoSuchAlgorithmException, NoSuchPaddingException,
	InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvocationTargetException, IllegalArgumentException {
		// Key
		String secret = decrypt_type_B("Ktqhuds");

		byte[] cipherData = Base64.getDecoder().decode(encrypted_text);
		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
		SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
		IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

		byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
		Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] decryptedData = aesCBC.doFinal(encrypted);
		String decrypted_text = new String(decryptedData, StandardCharsets.UTF_8);

		return decrypted_text;
	}

	/*
	 * Dekodiert einen String mit einem primitiven Algorithmus, der das unterste Bit toggelt.
	 */
	public static String decrypt_type_B(String encrypted_text) {
		String decrypted_text = "";
		for (int i = 0; i < encrypted_text.length(); i++) {
			char a = encrypted_text.charAt(i);
			char b = (char) (a ^ 1);
			decrypted_text = decrypted_text + b;
		}
		return decrypted_text;
	}

	/*
	 * Code von Stackoverflow. Löst ein Problem mit einer Begrenzung auf 256 Zeichen.
	 */
	public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

		int digestLength = md.getDigestLength();
		int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
		byte[] generatedData = new byte[requiredLength];
		int generatedLength = 0;

		try {
			md.reset();

			// Repeat process until sufficient data has been generated
			while (generatedLength < keyLength + ivLength) {

				// Digest data (last digest if available, password data, salt if available)
				if (generatedLength > 0)
					md.update(generatedData, generatedLength - digestLength, digestLength);
				md.update(password);
				if (salt != null)
					md.update(salt, 0, 8);
				md.digest(generatedData, generatedLength, digestLength);

				// additional rounds
				for (int i = 1; i < iterations; i++) {
					md.update(generatedData, generatedLength, digestLength);
					md.digest(generatedData, generatedLength, digestLength);
				}

				generatedLength += digestLength;
			}

			// Copy key and IV into separate byte arrays
			byte[][] result = new byte[2][];
			result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
			if (ivLength > 0)
				result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

			return result;

		} catch (DigestException e) {
			throw new RuntimeException(e);

		} finally {
			// Clean out temporary data
			Arrays.fill(generatedData, (byte)0);
		}
	}

}
