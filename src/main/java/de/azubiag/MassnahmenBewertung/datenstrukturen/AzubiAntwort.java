package de.azubiag.MassnahmenBewertung.datenstrukturen;


import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für Kursbewertung durch _einen_ Azubi.
 */
public class AzubiAntwort {
	
	public BewertungMassnahme massnahme;         // Seite 2 d. Referenzdokuments
	
	public List<BewertungReferent> referenten;   // Seite 3 d. Referenzdokuments
	
	/* Erzeugt ein AzubiAntwort-Objekt.
	 * 
	 * @param kodiert : Entschlüsselter String
	 */
	public AzubiAntwort(String kodiert) {
		String[] array = kodiert.split("\\|", -1); // -1 verhindert, dass split() leere Strings am Ende verwirft und dann das Array zu kurz ist
		massnahme = new BewertungMassnahme(subarray(array, 0, 6));
		referenten = new ArrayList<>(10);
		int index = 6;
		while (index+6 < array.length) {
			referenten.add(new BewertungReferent(subarray(array, index, index+7)));
			index += 7;
		}
	}
	
	private static String[] subarray(String[] mainArray, int start, int end) {
		String[] newarray = new String[end-start];
		System.arraycopy(mainArray, start, newarray, 0, end-start);
		return newarray;
	}
	
	public static ArrayList<AzubiAntwort> konvertiereStringsInAzubiAntworten(List<String> strings) {
		ArrayList<AzubiAntwort> azubis = new ArrayList<>(20);
		for (String string : strings) azubis.add(new AzubiAntwort(string));
		return azubis;
	}
	
	@Override
	public String toString() {
		return String.format("AzubiAntwort [%s, %s]", massnahme, referenten);
	}
	
}
