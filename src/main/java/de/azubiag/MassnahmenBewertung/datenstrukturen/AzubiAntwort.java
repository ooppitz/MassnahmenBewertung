package de.azubiag.MassnahmenBewertung.datenstrukturen;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse für Kursbewertung durch _einen_ Azubi.
 */
public class AzubiAntwort implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3149579952902991635L;

	public BewertungMassnahme massnahme;         // Seite 2 d. Referenzdokuments
	
	public List<BewertungReferent> referenten;   // Seite 3 d. Referenzdokuments
	
	public int umfrageID;
	
	public int antwortID;
	
	public String verschlüsselterString;
	
	/** Erzeugt ein AzubiAntwort-Objekt.
	 * 
	 * @param kodiert : Entschlüsselter String
	 */
	public AzubiAntwort(String kodiert, String verschlüsselt) {
		verschlüsselterString = verschlüsselt;
		String[] array = kodiert.split("\\|", -1); // -1 verhindert, dass split() leere Strings am Ende verwirft und dann das Array zu kurz ist
		umfrageID = Integer.parseInt(array[0]);
		antwortID = Integer.parseInt(array[1]);
		massnahme = new BewertungMassnahme(subarray(array, 2, 8));
		referenten = new ArrayList<>(10);
		int index = 8;
		while (index+6 < array.length) {
			referenten.add(new BewertungReferent(subarray(array, index, index+7)));
			index += 7;
		}
	}
	
	/**
	 * @deprecated
	 */
	public AzubiAntwort(String kodiert) {	//TODO: Aufrufe hiervon abändern
		verschlüsselterString = kodiert;
		String[] array = kodiert.split("\\|", -1); // -1 verhindert, dass split() leere Strings am Ende verwirft und dann das Array zu kurz ist
		umfrageID = Integer.parseInt(array[0]);
		antwortID = Integer.parseInt(array[1]);
		massnahme = new BewertungMassnahme(subarray(array, 2, 8));
		referenten = new ArrayList<>(10);
		int index = 8;
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
		for (String string : strings) azubis.add(new AzubiAntwort(string,string));		// TODO: verschlüsselte Strings hinzufügen
		return azubis;
	}
	
	@Override
	public String toString() {
		return String.format("AzubiAntwort [%s, %s, verifyID=%d, randomID=%d]", massnahme, referenten, umfrageID, antwortID);
	}
	
}
