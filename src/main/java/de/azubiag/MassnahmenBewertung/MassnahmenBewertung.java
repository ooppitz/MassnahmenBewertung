package de.azubiag.MassnahmenBewertung;

import de.azubiag.MassnahmenBewertung.UI.MainApp;

public class MassnahmenBewertung  {

	public static void main(String[] args) {
		
		MainApp.main(args);
		System.exit(0);	// ansonsten würde der Thread weiterlaufen, der abfrägt, ob ein Fragebogen schon im Internet zu finden ist
	}


}