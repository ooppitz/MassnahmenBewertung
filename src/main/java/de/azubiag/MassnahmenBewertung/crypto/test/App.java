package de.azubiag.MassnahmenBewertung.crypto.test;

import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.crypto.Decrypt;

public class App {

	public static void main(String[] args)  {

		// For testing
		String original_text = "Hello World!";
		String cipherText = "---AU2FsdGVkX18Hfx/uQqsOVkTmCzj89C+n7ChAgoh69rrPyJhNYzGdX79uOg0te+hcGHnzIgztVX06m1av5qoXiO7S9VI1Te7FE/93jg43FaNz3zIG4M/BkyRWbmCqLre4VR1eMHTyZrvyc6rWrc3eqHRCZRicDgdMLJzBCswPrx+S3hOAzURnrlCn84e7JaisYLuAl9Y5qRLOZHCtF6Hl3v3r+qc0Z+vjT5LpUlzkkto=---";
		// !
		
		// cipherText manipulation
		cipherText = ";ö)▬º☺╣5ì5☺51f┴l♦█╚AôTq├☻«↔î÷KÝ}Õ3þÅÅHJ{♠?▼3_";
		Scanner sc = new Scanner(System.in);
		System.out.print("cippherText: ");
		cipherText = sc.next();
		sc.close();
		System.out.println(cipherText);
		
		
		String result = Decrypt.decrypt_any_type(cipherText);
		test_against_original_text(result, original_text);
		System.out.println("\n" + result);

	}

	public static void test_against_original_text(String result, String original_text) {
		// test decrypted_text against original text
		System.out.println("\"original_text\" equals \"decrypted_text\":   " + original_text.equals(result));
	}
}
