package de.azubiag.MassnahmenBewertung;

import de.azubiag.MassnahmenBewertung.UI.MainApp;

public class MassnahmenBewertung  {

	public static void main(String[] args) {
		
		MainApp.main(args);
		
		MainApp.upload.synchronisieren("Speichern der offenen Tabs");
	}


}