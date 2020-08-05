package de.azubiag.MassnahmenBewertung.crypto.test;

import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.crypto.Decrypt;

public class App {

	public static void main(String[] args)  {

		// For testing
		String original_text = "Hello World!";
		String cipherText = "AU2FsdGVkX18uWzEdDvZ41IB4pfO8UvmVyO0AciP93e67w/tzAbB7l0GpDzadKAcRDsnM7LAR5G+x5F/3w3mMpOERbhReKQN8w6RLNeOIltjoXCUSg8/jKGRDRlc09T2g";
		// !
		
		// cipherText manipulation
		/*
		cipherText = ";ö)▬º☺╣5ì5☺51f┴l♦█╚AôTq├☻«↔î÷KÝ}Õ3þÅÅHJ{♠?▼3_";
		Scanner sc = new Scanner(System.in);
		System.out.print("cippherText: ");
		cipherText = sc.next();
		sc.close();
		System.out.println(cipherText);
		*/
		
		
		String result = Decrypt.decrypt_any_type(cipherText);
		test_against_original_text(result, original_text);
		System.out.println("\n" + result);

	}

	public static void test_against_original_text(String result, String original_text) {
		// test decrypted_text against original text
		System.out.println("\"original_text\" equals \"decrypted_text\":   " + original_text.equals(result));
	}
}
