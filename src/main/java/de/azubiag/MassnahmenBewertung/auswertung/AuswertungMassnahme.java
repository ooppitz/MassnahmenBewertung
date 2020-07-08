package de.azubiag.MassnahmenBewertung.auswertung;

import java.util.ArrayList;

import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;

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
	AuswertungMassnahme(ArrayList<BewertungMassnahme> bewertungen) {
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
	void bearbeitungkursbewertung(BewertungMassnahme[] eingehendesergebnis) {
		anzahl=eingehendesergebnis.length;
		for(int i=0;i<anzahl;i++) {
		kursbewertungPunkteUndBemerkung(eingehendesergebnis[i]);
		durchschnittAufzaehlen();
		}
		durchschnittFertigBerechnen();
	}

	//Es beginnt die Berechnung des Durchschnitts
	private void durchschnittAufzaehlen() {
		anzahl++;
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
		pktvertOrg[eingehendesergebnis.verlauf]++;
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

}
