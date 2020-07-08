package de.azubiag.MassnahmenBewertung.datenstrukturen;

/** Seite 2 des Referenzdokuments *
 * 
 */
public class BewertungMassnahme {

	public int organisation;
	public int verlauf;
	public String bemerkungVerlauf;
	
	public int betreuung;
	public String bemerkungBetreuung;
	
	public String bemerkungReferentenAllgemein;
	
	public BewertungMassnahme(String[] array) {
		organisation = Integer.parseInt(array[0]);
		verlauf = Integer.parseInt(array[1]);
		bemerkungVerlauf = array[2];
		betreuung = Integer.parseInt(array[3]);
		bemerkungBetreuung = array[4];
		bemerkungReferentenAllgemein = array[5];
	}
}
