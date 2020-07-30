package de.azubiag.MassnahmenBewertung.testdaten;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.UI.FragebogenEigenschaften;
import de.azubiag.MassnahmenBewertung.UI.MultiAntwortParser;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

/**
 * Mögliche Probleme: - Wenn das Format sich ändert - Probleme mit anderer
 * Fragebogen-ID : ... Im Testbetrieb (--test), ignorieren der Fragebogen-IDs
 * ... Speichern eines "Fragebogen-Tabs"
 *
 */
public class Testdaten {

	static ArrayList<AzubiAntwort> listeAntworten = null;

	static String antwortString = "---AU2FsdGVkX19Nm/gDrRRVrAeQ42d0vfekAm7tmnYZQuf+/JWJ/Zt+yV9ajj7+Iz1HkXOJbU4cL2YVf1BRqgnwTqUKeuv+Mvxx8r5q76svmbyDQa3TZeggY6uJMSFhIuvmcaLRIj3oP2WY/MmqBB4D+G1qi95xwHKUWa547Aiih5mCVtDPmnXmTmZ3UdDjcGDP--- "
			+ "---AU2FsdGVkX1+KLZiXDZPW6GGgyU8+NWfgG80sIuGOCgVa+bi9HEjTlW8kAHXtIBJ+dCxZK1mHIK3T/Cb/DfYnE5XBvcD69q/+R6g0ZXTHTMlC1/0xv0/w89QkfWz93IoTySP5I5rzWojfJJ52iH7FlQuJJM+8ieSOn+GOqnHmmlcXqNEzh9iG5nKzmHDWx/rg--- "
			+ "---AU2FsdGVkX18ASd2vEhCG72khtLDOfhhd6B/UJ2E7Vk9JpeupYi6XhaGdRo5qNcEVWvmTzhQliKYMRD86dsh4A8DaHR/dgu03trGOn0Zz7jDOiUZ6DU34YrpRKIje08VraWAmFY0wqJFjpqTrRMwZ6UW2mGIpJfCl0alYdQiRz7kVkbsc+V/EEhZRH41uYqmu--- "
			+ "---AU2FsdGVkX19WhVHYm74w49LoV5SCtm+/M7BWKnTwtQ3rlKVnFX82q25vjX4pcRXHuBwUCVBQ/Ruf20swRCC492kkF0Ob2nViuIo6CGxC3kx8JfZNV72bcDsSlTpepmoECH5bvHkjE/Arue03JJwFH/PzddY6i2dNTXO5so3KIgnYFUPlrFm+arxGk/CW9R5V--- ";
	
	String dummy = ""
			+ "---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--- ";

	private static void erzeugeTestdaten() {
		ArrayList<String> antworten = MultiAntwortParser.parse(antwortString);

		listeAntworten = new ArrayList<AzubiAntwort>();
		for (String verschluesselt : antworten) {
			String klartext = Decrypt.decrypt_any_type(verschluesselt);
			listeAntworten.add(new AzubiAntwort(klartext, verschluesselt));
		}
	}

	public static AuswertungMassnahme getAuswertungMassnahme() {
		if (listeAntworten == null) {
			erzeugeTestdaten();
		}
		return AuswertungMassnahme.getAuswertungMassnahme(listeAntworten);
	}

	public static List<AuswertungReferent> getAuswertungReferenten() {
		if (listeAntworten == null) {
			erzeugeTestdaten();
		}
		return AuswertungReferent.getAuswertungenAllerReferenten(listeAntworten);
	}

	public static FragebogenEigenschaften getFragebogenEigenschaften() {

		FragebogenEigenschaften eigenschaften = new FragebogenEigenschaften("3. Ausbildungsjahr 2020", "Ramilya Moll",
				"F-34-A8988", LocalDate.now(), LocalDate.now(), LocalDate.now(), "http://github.io//unserlink.html");

		return eigenschaften;
	}

}
