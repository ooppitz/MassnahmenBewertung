package de.azubiag.MassnahmenBewertung.auswertung.test;

import java.util.ArrayList;
import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

public class App {

	private static final String VERSION = "1.0";

	/**
	 * Liest Antwort-Strings (verschlüsselt oder unverschlüsselt )aus einem File
	 * oder von der Kommandozeile und verarbeitet sie. Der Output sollte eine
	 * komplette Auswertung sein.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String verschluesselteAntwort1 = "---AU2FsdGVkX187J7t88AiFwFQeFQ3PSAIfca4+FMiyTpIAAJnOBbJm69+6ZpYUsJPmrdvBNHzuxco92C9HIByAObiKPOwzmy1MtL2qY98/H/IQXUbyDTb37EBZED/Nmp289sdTfaQLkX5lVkIbEQNyp51bqhFKfILhEx1rBCaiB8A7gzVt73jhRbxH+C4WXqC2---";
		String verschluesselteAntwort2 = "---AU2FsdGVkX187J7t88AiFwFQeFQ3PSAIfca4+FMiyTpIAAJnOBbJm69+6ZpYUsJPmrdvBNHzuxco92C9HIByAObiKPOwzmy1MtL2qY98/H/IQXUbyDTb37EBZED/Nmp289sdTfaQLkX5lVkIbEQNyp51bqhFKfILhEx1rBCaiB8A7gzVt73jhRbxH+C4WXqC2---";

		String entschluesselteAntwort1 = Decrypt.decrypt_any_type(verschluesselteAntwort1);
		String entschluesselteAntwort2 = Decrypt.decrypt_any_type(verschluesselteAntwort2);

		AzubiAntwort a1 = new AzubiAntwort(entschluesselteAntwort1);
		AzubiAntwort a2 = new AzubiAntwort(entschluesselteAntwort2);

		ArrayList<AzubiAntwort> liste = new ArrayList<>();
		liste.add(a1);
		liste.add(a2);

		System.out.println(a1.verifyID);
		System.out.println(a2.verifyID);

		System.out.println(AuswertungMassnahme.getAuswertungMassnahme(liste));
		var referenten = AuswertungReferent.getAuswertungenAllerReferenten(liste);
		for (AuswertungReferent referent : referenten) {
			System.out.println(referent);
		}

	}
}
