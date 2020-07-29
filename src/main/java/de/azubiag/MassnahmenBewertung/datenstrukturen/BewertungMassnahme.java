package de.azubiag.MassnahmenBewertung.datenstrukturen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/** Seite 2 des Referenzdokuments *
 * 
 */
public class BewertungMassnahme implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3089124079433422368L;
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
	
	@Override
	public String toString() {
		return String.format("BewertungMassnahme [organisation = %d, "
				+ "verlauf = %d, "
				+ "bemerkungVerlauf = \"%s\", "
				+ "betreuung = %d, "
				+ "bemerkungBetreuung = \"%s\", "
				+ "bemerkungReferentenAllgemein = \"%s\"]",
				organisation, verlauf, bemerkungVerlauf, betreuung, bemerkungBetreuung, bemerkungReferentenAllgemein);
	}
	
}
