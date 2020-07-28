package de.azubiag.MassnahmenBewertung.datenstrukturen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Luna
 * 
 */

/** Bewertung <b>eines</b> Referenten durch einen Azubi */

public class BewertungReferent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8694075946155473512L;


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
	
	public String toString() {
		return String.format("BewertungReferent [name = \"%s\", "
				+ "vorbereitet = %d, "
				+ "fachwissen = %d, "
				+ "eingehenAufThematischeProbleme = %d, "
				+ "inhalteVermitteln = %d, "
				+ "verhalten = %d, "
				+ "bemerkungen = \"%s\"]", name, vorbereitet, fachwissen, thematischeProbleme, inhalteVermitteln, verhalten, bemerkungen);
	}
	
	public final void writeObject(ObjectOutputStream os) {
		
		try {
			os.defaultWriteObject();
			os.writeUTF(name);
			os.writeInt(vorbereitet);
			os.writeInt(fachwissen);
			os.writeInt(thematischeProbleme);
			os.writeInt(inhalteVermitteln);
			os.writeInt(verhalten);
			os.writeUTF(bemerkungen);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public final void readObject(ObjectInputStream is) {
		
		try {
			is.defaultReadObject();
			name = is.readUTF();
			vorbereitet = is.readInt();
			fachwissen = is.readInt();
			thematischeProbleme = is.readInt();
			inhalteVermitteln = is.readInt();
			verhalten = is.readInt();
			bemerkungen = is.readUTF();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
