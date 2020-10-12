package de.azubiag.MassnahmenBewertung.tools;

public class Tools {

	/** Die Methode erzeugt einen String, der als File- oder Ordnername benutzt werden kann 
	 *  - Leerzeichen werden durch _ ersetzt
	 *  - Umlaute werden durch ue, ae, ... ersetzt
	 *  - Punkte "." werden entfernt
	 *  - der Filename wird in Kleinbuchstaben umgewandelt
	 * */
	public static String normalisiereString(String s) {
		s = s.toLowerCase();
		s = s.replace("ß", "sz");
		s = s.replace("ä", "ae");
		s = s.replace("ö", "oe");
		s = s.replace("ü", "ue");
		s = s.replaceAll("[^a-zA-Z0-9]", "_"); 
		if(s.length()>30) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
