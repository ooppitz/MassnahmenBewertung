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

	//Kategorisierung
	final static int BEW_ORGANISATION=0;
	final static int BEW_VERLAUF=1;
	final static int BEW_BETREUUNG=2;
	final static int BEW_REFERENT=3;

	//Maßnahmeverlauf:
	int[] pktvertOrg = new int[5];
	double durchschnOrg;

	int[] pktvertVerl = new int[5];
	double durchschnVerl;

	ArrayList <String> alleBemerkVerl;

	//Maßnahmebetreuung
	int[] pktvertBetrng = new int[5];
	double durchschnBetrng;

	ArrayList <String> alleBemerkBetrng;

	//Referentenbewertung
	ArrayList <String> alleBemerkRefAllg;

	//Anzahl der eingegangenen Bewertungen werden aufgez�hlt
	int anzahl;


	/*
	 * @params AzubiAntwort-Objekte 
	 */
	public AuswertungMassnahme(List<BewertungMassnahme> list) {
		initialisieren();
		bearbeitungkursbewertung(list);
	}

	//Variablen werden initialisiert
	void initialisieren() {
		durchschnOrg=0;
		durchschnVerl=0;
		durchschnBetrng=0;
		alleBemerkVerl=new ArrayList<>();
		alleBemerkBetrng=new ArrayList<>();
		alleBemerkRefAllg=new ArrayList<>();
		for (int i=0; i<pktvertOrg.length; i++) {
			pktvertOrg[i]=0;
			pktvertVerl[i]=0;
			pktvertBetrng[i]=0;
		}
	}
	
	//Die Bearbeitung beginnt
	void bearbeitungkursbewertung(List<BewertungMassnahme> eingehendesergebnis) {
		anzahl=eingehendesergebnis.toArray().length;
		for(int i=0;i<anzahl;i++) {
		kursbewertungPunkteUndBemerkung(eingehendesergebnis.get(i));
		durchschnittAufzaehlen();
		}
		durchschnittFertigBerechnen();
	}

	//Es beginnt die Berechnung des Durchschnitts
	private void durchschnittAufzaehlen() {
		durchschnOrg=0;
		durchschnVerl=0;
		durchschnBetrng=0;
		for (int i=0; i<pktvertOrg.length; i++) {
			durchschnOrg+=pktvertOrg[i]*(i+1);
			durchschnVerl+=pktvertVerl[i]*(i+1);
			durchschnBetrng+=pktvertBetrng[i]*(i+1);
		}
	}

	//Die Kursbewertungen werden aufgez�hlt
	private void kursbewertungPunkteUndBemerkung(BewertungMassnahme eingehendesergebnis) {
		pktvertOrg[eingehendesergebnis.organisation]++;
		pktvertVerl[eingehendesergebnis.verlauf]++;
		pktvertBetrng[eingehendesergebnis.betreuung]++;
		alleBemerkVerl.add(eingehendesergebnis.bemerkungVerlauf);
		alleBemerkBetrng.add(eingehendesergebnis.bemerkungBetreuung);
		alleBemerkRefAllg.add(eingehendesergebnis.bemerkungReferentenAllgemein);
	}

	private void durchschnittFertigBerechnen() {
		durchschnOrg/=anzahl;
		durchschnVerl/=anzahl;
		durchschnBetrng/=anzahl;
	}

	int[] getPunkteverteilung(int art) {
		int[] rueckgabe;
		switch(art) {
		case BEW_ORGANISATION:
			rueckgabe=pktvertOrg;
			break;
		case BEW_VERLAUF:
			rueckgabe=pktvertVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe=pktvertBetrng;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}

	double getDurchschnitt(int art) {
		double rueckgabe;
		switch(art) {
		case BEW_ORGANISATION:
			rueckgabe=durchschnOrg;
			break;
		case BEW_VERLAUF:
			rueckgabe=durchschnVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe=durchschnBetrng;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}

	ArrayList<String> getBemerkungen(int art) {
		ArrayList<String> rueckgabe;
		switch(art) {
		case BEW_VERLAUF:
			rueckgabe=alleBemerkVerl;
			break;
		case BEW_BETREUUNG:
			rueckgabe=alleBemerkBetrng;
			break;
		case BEW_REFERENT:
			rueckgabe=alleBemerkRefAllg;
			break;
		default:
			throw new IllegalArgumentException("�bergabeparameter ist ung�ltig");
		}
		return rueckgabe;
	}
	
	public static List<BewertungMassnahme> konvertiereBewertungMassnahmeInAuswertungMassnahme(List<String> alleMassnahmenInString) {
		List<BewertungMassnahme> bm= new ArrayList<>(20);
		for(String s: alleMassnahmenInString) {
			bm.add(new AzubiAntwort(s).massnahme);
		}
		return bm;
	}
	
	@Override
	public String toString() {
		return String.format("AuswertungMassnahme Organisation[Punkteverteilung: %s, Durchschnitt: %2.2f]\n"
				+ "AuswertungMassnahme Verlauf[Punkteverteilung: %s, Durchschnitt: %2.2f, Bemerkungen: %s]\n"
				+ "AuswertungMassnahme Betreuung[Punkteverteilung: %s, Durchschnitt: %2.2f, Bemerkungen: %s]\n"
				+ "AuswertungMassnahme Referenten[Bemerkungen: %s]", 
				Arrays.toString(getPunkteverteilung(BEW_ORGANISATION)),getDurchschnitt(BEW_ORGANISATION), 
				Arrays.toString(getPunkteverteilung(BEW_VERLAUF)),getDurchschnitt(BEW_VERLAUF),getBemerkungen(BEW_VERLAUF),
				Arrays.toString(getPunkteverteilung(BEW_BETREUUNG)),getDurchschnitt(BEW_BETREUUNG),getBemerkungen(BEW_BETREUUNG),
				getBemerkungen(BEW_REFERENT));
	}
}
