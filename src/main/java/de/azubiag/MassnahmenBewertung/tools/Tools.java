package de.azubiag.MassnahmenBewertung.tools;

public class Tools {

	/** Die Methode erzeugt einen String, der als File- oder Ordnername benutzt werden kann 
	 *  - Leerzeichen werden durch _ ersetzt
	 *  - Umlaute werden durch ue, ae, ... ersetzt
	 *  - Punkte "." werden entfernt
	 *  - der Filename wird in Kleinbuchstaben umgewandelt
	 * */
	public static String normalisiereString(String s) {
		
		return s.toLowerCase();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}