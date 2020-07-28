package de.azubiag.MassnahmenBewertung.testdaten;

import java.util.List;

/**
 * Mögliche Probleme: 
 * - Wenn das Format sich ändert
 * - Probleme mit anderer Fragebogen-ID : 
 *   ... Im Testbetrieb (--test), ignorieren der Fragebogen-IDs
 *   ... Speichern eines "Fragebogen-Tabs"
 *
 */
public class Testdaten {
	

	String[] antwortStrings1 = { 
			"---fadsjfkdsajfdasjlfadsj fldasjkfads jflöafj söfdsaj föasl---", 
			"---fadsjlfkdsajoifsafdsafdasfdsafdasfasdfsadfdsawejogfjdifp---"
	};
	
	String[] antwortStrings2 = { 
			"---fadsjfkdsajfdasjlfadsj fldasjkfads jflöafj söfdsaj föasl---", 
			"---fadsjlfkdsajoifsafdsafdasfdsafdasfasdfsadfdsawejogfjdifp---"
	};
	
	String[] antwortStrings3 = { 
			"---fadsjfkdsajfdasjlfadsj fldasjkfads jflöafj söfdsaj föasl---", 
			"---fadsjlfkdsajoifsafdsafdasfdsafdasfasdfsadfdsawejogfjdifp---"
	};
	
	/* Liefert jeweils den nächsten Testdatensatz */
	public static List<String> getTestdaten() {
		
		return null;
	}

}
