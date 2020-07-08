package de.azubiag.MassnahmenBewertung.datenstrukturen;

/**
 * @author Luna
 * 
 */

/** Bewertung <b>eines</b> Referenten durch einen Azubi */

public class BewertungReferent {
	
	/**
	 * Der Name des Referenten
	 */
	public String name;
	
	public String getName() {
		return name;
	}

	public int vorbereitet;
	public int getVorbereitet() {
		return vorbereitet;
	}

	public int fachwissen;
	public int getFachwissen() {
		return fachwissen;
	}

	public int getEingehenAufProbleme() {
		return thematischeProbleme;
	}
	
	public int thematischeProbleme;
	
	
	public int inhalteVermitteln;
	
	public int getInhalteVermitteln() {
		return inhalteVermitteln;
	}

	public int verhalten;
	
	public int getVerhalten() {
		return verhalten;
	}

	public String bemerkungen;
	
	public String getBemerkungen() {
		return bemerkungen;
	}

	public BewertungReferent(String[] array) {
		
		name = array[0];
		vorbereitet  = Integer.parseInt(array[1]);
		fachwissen  = Integer.parseInt(array[2]);
		thematischeProbleme  = Integer.parseInt(array[3]);
		inhalteVermitteln  = Integer.parseInt(array[4]);
		verhalten  = Integer.parseInt(array[5]);
		bemerkungen = array[6];
	}
	
}
