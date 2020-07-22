package de.azubiag.MassnahmenBewertung.auswertung.test;

import java.util.ArrayList;
import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

public class QuickTest1 {

	private static final String VERSION = "1.0";

	public static void main(String[] args) {
		QuickTest1 qt = new QuickTest1();
		qt.start();
	}

	private Scanner sc;
	
	public QuickTest1() {
		sc = new Scanner(System.in);
	}
	
	public void start() {
		System.out.println("Willkommen bei QuickTest " + VERSION);
		int selection = askSelection("Welche Funktion?", new String[] {
				"Dekodieren einzelne Antwort",
				"Auswerten",
				"Decrypt"
				});
		switch (selection) {
		case 0:
			einzelneAntwortenDekodieren();
			break;
		case 1:
			auswerten();
			break;
		case 2:
			decrypten();
			break;
		}
	}
	
	
	private void decrypten() {
		String input;
		do {
			input = sc.nextLine();
			String output = Decrypt.decrypt_any_type(input);
			System.out.println(output);
		} while (input.length() > 0);
	}

	private void auswerten() {
		ArrayList<AzubiAntwort> liste = new ArrayList<>();
		String input;
		do {
			input = dynamicDecode(sc.nextLine());
			liste.add(new AzubiAntwort(input));
		} while (input.length() > 0);
		
		int verifyID = liste.get(0).verifyID;
		boolean verified = true;
		for (AzubiAntwort aa : liste) {
			if(aa.verifyID != verifyID) {
				verified = false;
				break;
			}
		}
		System.out.println(verified? "VerifyID gültig, " + verifyID : "VerifyID ungültig");
		System.out.println(AuswertungMassnahme.getAuswertungMassnahme(liste));
		var referenten = AuswertungReferent.getAuswertungenAllerReferenten(liste);
		for (AuswertungReferent referent : referenten) {
			System.out.println(referent);
		}
	}

	private void einzelneAntwortenDekodieren() {
		String input;
		do {
			input = dynamicDecode(sc.nextLine());
			AzubiAntwort aa = new AzubiAntwort(input);
			System.out.println(aa);
		} while (input.length() > 0);		
	}

	private int askSelection(String prompt, String[] options) {
		if (options.length == 0) throw new IllegalArgumentException();
		boolean depp = true;
		int inputvalue = 0;
		do {
			System.out.println(prompt);
			for(int i=0; i<options.length; i++) {
				System.out.printf("%d -> %s\n", i+1, options[i]);
			}
			System.out.print(" > ");
			try {
				String rawinput = sc.nextLine();
				inputvalue = Integer.parseInt(rawinput);
				depp = inputvalue < 1 || inputvalue > options.length;
			} catch(NumberFormatException nfex) {
				depp = true;
			}
		} while (depp);
		return inputvalue - 1;
	}
	
	private String dynamicDecode(String input) {
		String midput = Decrypt.decrypt_any_type(input);
		return midput != null? midput : input;
	}
	
	

}
