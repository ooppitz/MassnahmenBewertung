package de.azubiag.MassnahmenBewertung.crypto.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.azubiag.MassnahmenBewertung.crypto.Decrypt;

public class Execute_Decrypt {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		// For testing
		String original_text = "Hello World!";
		String cipherText = "AU2FsdGVkX1/c6KC9I/HrDudlW4maqW6KBbJz67ukMtk=";
		
		String result = Decrypt.decrypt_any_type(cipherText);
		test_against_original_text(result, original_text);
		System.out.println("\n" + result);
		
	}

	public static void test_against_original_text(String result, String original_text) {
		// test decrypted_text against original text 
		System.out.println("\"original_text\" equals \"decrypted_text\":   " + original_text.equals(result));
	}
}
