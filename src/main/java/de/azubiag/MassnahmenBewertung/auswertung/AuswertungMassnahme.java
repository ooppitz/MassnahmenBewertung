package de.azubiag.MassnahmenBewertung.auswertung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;

/**
 * 
 * @author Denis Bode
 *
 */
public class AuswertungMassnahme {

	// Kategorisierung
	final static int BEW_ORGANISATION = 0;
	final static int BEW_VERLAUF = 1;
	final static int BEW_BETREUUNG = 2;
	final static int BEW_REFERENT = 3;

	// Maßnahmeverlauf:
	public int[] pktvertOrg = new int[5];
	public double durchschnOrg;

	public int[] pktvertVerl = new int[5];
	public double durchschnVerl;

	public ArrayList<String> alleBemerkVerl;

	// Maßnahmebetreuung
	public int[] pktvertBetrng = new int[5];
	public double durchschnBetrng;

	public ArrayList<String> alleBemerkBetrng;

	// Referentenbewertung
	public ArrayList<String> alleBemerkRefAllg;

	// Anzahl der eingegangenen Bewertungen werden aufgez�hlt
	int anzahl;


	
	/**
	 * Die öffentlich verfügbare Methode, welche die Liste an Bewertungen des
	 * Maßnahmenteils auswertet. Dabei ruft es die Initialiserung der eigenen
	 * Variablen auf, bevor es die Bearbeitung der Liste als Methode aufruft.
	 * 
	 * @param List list
	 * @param <T>  BewertungMassnahme
	 */
	public AuswertungMassnahme(List<BewertungMassnahme> list) {
		initialisieren();
		bearbeitungkursbewertung(list);
	}

	/**
	 * Die Variablen von AuswertungMassnahme werden initialisiert.
	 */
	// Variablen werden initialisiert
	private void initialisieren() {
		durchschnOrg = 0;
		durchschnVerl = 0;
		durchschnBetrng = 0;
		alleBemerkVerl = new ArrayList<>();
		alleBemerkBetrng = new ArrayList<>();
		alleBemerkRefAllg = new ArrayList<>();
		for (int i = 0; i < pktvertOrg.length; i++) {
			pktvertOrg[i] = 0;
			pktvertVerl[i] = 0;
			pktvertBetrng[i] = 0;
		}
	}

	/**
	 * Die ankommende Liste an Bewertungen des Maßnahmenteils werden gezählt und
	 * bearbeitet. Es wird für jede Bewertung die Punktewahl gezählt und für den
	 * Durchschnitt aufgezählt. Sobald alles gezählt ist, wird der Durchschnitt
	 * fertig berechnet.
	 * 
	 * @param eingehendesergebnis List
	 * @param <T>                 BewertungMassnahme
	 */
	// Die Bearbeitung beginnt
	private void bearbeitungkursbewertung(List<BewertungMassnahme> eingehendesergebnis) {
		anzahl = eingehendesergebnis.toArray().length;
		for (int i = 0; i < anzahl; i++) {
			kursbewertungPunkteUndBemerkung(eingehendesergebnis.get(i));
			durchschnittAufzaehlen();
		}
		durchschnittFertigBerechnen();
	}

	/**
	 * Der Durchschnitt der Kategorien Organisation, Verlauf und Betreuung bekommt
	 * hier die Punkte addiert, welche dann in einer anderen Methode durch die
	 * Anzahl der Bewertungen des Maßnahmenteils geteilt werden.
	 */
	// Es beginnt die Berechnung des Durchschnitts
	private void durchschnittAufzaehlen() {
		durchschnOrg = 0;
		durchschnVerl = 0;
		durchschnBetrng = 0;
		for (int i = 0; i < pktvertOrg.length; i++) {
			durchschnOrg += pktvertOrg[i] * (i + 1);
			durchschnVerl += pktvertVerl[i] * (i + 1);
			durchschnBetrng += pktvertBetrng[i] * (i + 1);
		}
	}

	/**
	 * Die Punkte und Bemerkungen der Kategorien Organisation, Verlauf, Betreuung
	 * und Referenten(nur allgemeine Bemerkungen) werden hier aufgezählt.
	 * 
	 * @param eingehendesergebnis BewertungMassnahme
	 */
	// Die Kursbewertungen werden aufgez�hlt
	private void kursbewertungPunkteUndBemerkung(BewertungMassnahme eingehendesergebnis) {
		pktvertOrg[eingehendesergebnis.organisation]++;
		pktvertVerl[eingehendesergebnis.verlauf]++;
		pktvertBetrng[eingehendesergebnis.betreuung]++;
		alleBemerkVerl.add(eingehendesergebnis.bemerkungVerlauf);
		alleBemerkBetrng.add(eingehendesergebnis.bemerkungBetreuung);
		alleBemerkRefAllg.add(eingehendesergebnis.bemerkungReferentenAllgemein);
	}

	/**
	 * Die aufgezählten Punkte der Kategorien Organisation, Verlauf und Betreuung
	 * wird durch die Anzahl der Bewertungen des Maßnahmenteils geteilt, um den
	 * Punktedurchschnit der jeweiligen Kategorien zu bekommen.
	 */
	// Die Berechnung des Durchschnitts wird hier vollendet
	private void durchschnittFertigBerechnen() {
		durchschnOrg /= anzahl;
		durchschnVerl /= anzahl;
		durchschnBetrng /= anzahl;
		
		// BUGFIX: Werte sollen von -2 zu +2 gehen, nicht von 0 bis 5
		durchschnOrg -= 3;
		durchschnVerl -= 3;
		durchschnBetrng -= 3;
	}

	/**
	 * Es wird eine Punkteverteilung zurückgegeben. In Abhängigkeit davon, welche
	 * numerische Konstante übergeben wurde, wird ausgewählt, ob die
	 * Punkteverteilung von Organisation, Verlauf oder Betreuung zurückgegeben
	 * werden soll.
	 * 
	 * @param art int(Konstante)
	 * @return rueckgabe []int
	 */
	// Die Punkteverteilung wird in Abhängigkeit der Konstante zurückgegeben
	private int[] getPunkteverteilung(int art) {
		int[] rueckgabe;
		switch (art) {
		case BEW_ORGANISATION:
			rueckgabe = pktvertOrg;
			break;
		case BEW_VERLAUF:
			rueckgabe = pktvertVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe = pktvertBetrng;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}

	/**
	 * Es wird ein Durchschnitt zurückgegeben. In Abhängigkeit davon, welche
	 * numerische Konstante übergeben wurde, wird ausgewählt, ob der Durchschnitt
	 * von Organisation, Verlauf oder Betreuung zurückgegeben werden soll.
	 * 
	 * @param art int(Konstante)
	 * @return rueckgabe double
	 */
	// Der Durchschnitt wird in Abhängigkeit der Konstante zurückgegeben
	private double getDurchschnitt(int art) {
		double rueckgabe;
		switch (art) {
		case BEW_ORGANISATION:
			rueckgabe = durchschnOrg;
			break;
		case BEW_VERLAUF:
			rueckgabe = durchschnVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe = durchschnBetrng;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}

	/**
	 * Es wird eine Sammlung von Bemerkungen zurückgegeben. In Abhängigkeit davon,
	 * welche numerische Konstante übergeben wurde, wird ausgewählt, ob die
	 * Bemerkungen von Organisation, Verlauf oder Betreuung zurückgegeben werden
	 * soll.
	 * 
	 * @param art int
	 * @return rueckgabe ArrayList
	 * @param <T> String
	 */
	// Die ArrayList von Bemerkungen wird in Abhängigkeit der Konstante
	// zurückgegeben
	private ArrayList<String> getBemerkungen(int art) {
		ArrayList<String> rueckgabe;
		switch (art) {
		case BEW_VERLAUF:
			rueckgabe = alleBemerkVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe = alleBemerkBetrng;
			break;
		case BEW_REFERENT:
			rueckgabe = alleBemerkRefAllg;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}

	/**
	 * Speziell für die Klasse "TestFeeder3" erstellte Methode, welche die String
	 * von buffer an den Konstruktor von AuswertungMassnahme als einen passenden
	 * Parameter übergibt.
	 * 
	 * @param alleMassnahmenInString List
	 * @param <T>                    String
	 * @return bm List
	 * @param <T> BewertungMassnahme
	 */
	// Die Strings der BewertungMassnahme von AzubiAntwort wird in eine Liste
	// umgewandelt
	public static List<BewertungMassnahme> konvertiereBewertungMassnahmeInAuswertungMassnahme(
			List<String> alleMassnahmenInString) {
		List<BewertungMassnahme> bm = new ArrayList<>(20);
		for (String s : alleMassnahmenInString) {
			bm.add(new AzubiAntwort(s).massnahme);
		}
		return bm;
	}

	

	public static AuswertungMassnahme getAuswertungMassnahme(List<AzubiAntwort> antworten) {
		
		// Erzeugen einer Liste von OBjekten vom Typ BewertungMassnahme
		
		List<BewertungMassnahme> massnahmenBewertungen = new ArrayList<BewertungMassnahme>(); //  = AuswertungMassnahme.konvertiereBewertungMassnahmeInAuswertungMassnahme(buffer)
		
		for(AzubiAntwort a : antworten) {
			massnahmenBewertungen.add(a.massnahme);
		}
		
		AuswertungMassnahme auswertung = new AuswertungMassnahme(massnahmenBewertungen);
		
		return auswertung;
		
	}
	
	
	/**
	 * Die für die Auswertung wichtigen Variablen werden in der Konsole ausgegeben
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return String.format(
				"AuswertungMassnahme Organisation[Punkteverteilung: %s, Durchschnitt: %2.2f]\n"
						+ "AuswertungMassnahme Verlauf[Punkteverteilung: %s, Durchschnitt: %2.2f, Bemerkungen: %s]\n"
						+ "AuswertungMassnahme Betreuung[Punkteverteilung: %s, Durchschnitt: %2.2f, Bemerkungen: %s]\n"
						+ "AuswertungMassnahme Referenten[Bemerkungen: %s]",
				Arrays.toString(getPunkteverteilung(BEW_ORGANISATION)), getDurchschnitt(BEW_ORGANISATION),
				Arrays.toString(getPunkteverteilung(BEW_VERLAUF)), getDurchschnitt(BEW_VERLAUF),
				getBemerkungen(BEW_VERLAUF), Arrays.toString(getPunkteverteilung(BEW_BETREUUNG)),
				getDurchschnitt(BEW_BETREUUNG), getBemerkungen(BEW_BETREUUNG), getBemerkungen(BEW_REFERENT));
	}
}
