package de.azubiag.MassnahmenBewertung.auswertung;

import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;

/**
 * @author Louisa
 * 
 */

public class AuswertungReferent {

	public String name;

	public int[] stimmenProRadioBtnVorbereitung = new int[5];
	public int[] stimmenProRadioBtnFachwissen = new int[5];
	public int[] stimmenProRadioBtnEingehenAufProbleme = new int[5];
	public int[] stimmenProRadioBtnInhaltsvermittlung = new int[5];
	public int[] stimmenProRadioBtnVerhalten = new int[5];

	public double durchschnittVorbereitung;
	public double durchschnittFachwissen;
	public double durchschnittEingehenAufProbleme;
	public double durchschnittInhaltsvermittlung;
	public double durchschnittVerhalten;

	public List<String> bemerkungen = new ArrayList<String>();

	public AuswertungReferent(ArrayList<BewertungReferent> bewertungen) {

		setName(bewertungen.get(0).getName());

		zaehleStimmenProRadioBtn(bewertungen);

		berechneDurchschnittJeFrage(bewertungen);

		sammleBemerkungen(bewertungen);

	}

//TEST
//	public static void main(String[] args) {
//		ArrayList<BewertungReferent> bewertungen = new ArrayList<BewertungReferent>();
//		String[] dummy = { "Pfaffelhuber", "0", "1", "2", "3", "4", "nicht gerade gut ausgebildet" };
//
//		bewertungen.add(new BewertungReferent(dummy));
//		bewertungen.add(new BewertungReferent(dummy));
//		bewertungen.add(new BewertungReferent(dummy));
//		bewertungen.add(new BewertungReferent(dummy));
//		bewertungen.add(new BewertungReferent(dummy));
//
//		AuswertungReferent auswertungPfaffelhuber = new AuswertungReferent(bewertungen);
//
//		System.out.println(auswertungPfaffelhuber);
//	}

	private void berechneDurchschnittJeFrage(ArrayList<BewertungReferent> bewertungen) {
		int gesamtVorbereitung = 0;
		int gesamtFachwissen = 0;
		int gesamtEingehenAufProbleme = 0;
		int gesamtInhaltsvermittlung = 0;
		int gesamtVerhalten = 0;

		for (int i = 0; i <= 4; i++) {
			gesamtVorbereitung += stimmenProRadioBtnVorbereitung[i] * getWertungFuerIndex(i);
			gesamtFachwissen += stimmenProRadioBtnFachwissen[i] * getWertungFuerIndex(i);
			gesamtEingehenAufProbleme += stimmenProRadioBtnEingehenAufProbleme[i] * getWertungFuerIndex(i);
			gesamtInhaltsvermittlung += stimmenProRadioBtnInhaltsvermittlung[i] * getWertungFuerIndex(i);
			gesamtVerhalten += stimmenProRadioBtnVerhalten[i] * getWertungFuerIndex(i);
		}

		int anzahlBewertungen = bewertungen.size();
		durchschnittVorbereitung = gesamtVorbereitung / anzahlBewertungen;
		durchschnittFachwissen = gesamtFachwissen / anzahlBewertungen;
		durchschnittEingehenAufProbleme = gesamtEingehenAufProbleme / anzahlBewertungen;
		durchschnittInhaltsvermittlung = gesamtInhaltsvermittlung / anzahlBewertungen;
		durchschnittVerhalten = gesamtVerhalten / anzahlBewertungen;
	}

	public List<String> getBemerkungen() {
		return bemerkungen;
	}

	public Double getDurchschnitt(Frage frage) {
		Double returnValue = null;
		switch (frage) {

		case VORBEREITUNG: {
			returnValue = durchschnittVorbereitung;
			break;
		}
		case FACHWISSEN: {
			returnValue = durchschnittFachwissen;
			break;
		}
		case EINGEHENAUFPROBLEME: {
			returnValue = durchschnittEingehenAufProbleme;
			break;
		}
		case INHALTSVERMITTLUNG: {
			returnValue = durchschnittInhaltsvermittlung;
			break;
		}
		case VERHALTEN: {
			returnValue = durchschnittVerhalten;
			break;
		}
		}

		return returnValue;
	}

	public String getName() {
		return name;
	}

	public int getStimmenProRadioButton(Frage frage, int nrRadioButton) {

		int returnValue = -1;
		switch (frage) {
		case VORBEREITUNG: {
			returnValue = stimmenProRadioBtnVorbereitung[nrRadioButton];
			break;
		}
		case FACHWISSEN: {
			returnValue = stimmenProRadioBtnFachwissen[nrRadioButton];
			break;
		}
		case EINGEHENAUFPROBLEME: {
			returnValue = stimmenProRadioBtnEingehenAufProbleme[nrRadioButton];
			break;
		}
		case INHALTSVERMITTLUNG: {
			returnValue = stimmenProRadioBtnInhaltsvermittlung[nrRadioButton];
			break;
		}
		case VERHALTEN: {
			returnValue = stimmenProRadioBtnVerhalten[nrRadioButton];
			break;
		}
		}
		return returnValue;
	}

	private Integer getWertungFuerIndex(int index) {
		Integer returnValue = null;
		switch (index) {
		case 0: {

			returnValue = -2;
			break;
		}
		case 1: {

			returnValue = -1;
			break;
		}
		case 2: {

			returnValue = 0;
			break;
		}
		case 3: {

			returnValue = 1;
			break;
		}
		case 4: {

			returnValue = 2;
			break;
		}
		}
		return returnValue;
	}

	private void sammleBemerkungen(ArrayList<BewertungReferent> bewertungen) {

		for (BewertungReferent bewertungReferent : bewertungen) {

			if (bewertungReferent.getBemerkungen() != null) {
				bemerkungen.add(bewertungReferent.getBemerkungen());
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {

		String durchschnitte = "Durchschnitt Vorbereitung: " + getDurchschnitt(Frage.VORBEREITUNG)
				+ ",\nDurchschnitt Fachwissen: " + getDurchschnitt(Frage.FACHWISSEN)
				+ ",\nDurchschnitt EingehenAufProbleme: " + getDurchschnitt(Frage.EINGEHENAUFPROBLEME)
				+ ", \nDurchschnitt Inhaltsvermittlung: " + getDurchschnitt(Frage.INHALTSVERMITTLUNG)
				+ ", \nDurchschnitt Verhalten: " + getDurchschnitt(Frage.VERHALTEN) + ", \n";

		String verteilung = "";

		Frage[] fragen = { Frage.VORBEREITUNG, Frage.FACHWISSEN, Frage.EINGEHENAUFPROBLEME, Frage.INHALTSVERMITTLUNG,
				Frage.VERHALTEN };

		for (Frage frage : fragen) {

			for (int nrRadioBtn = 0; nrRadioBtn < 5; nrRadioBtn++) {
				verteilung += getStimmenProRadioButton(frage, nrRadioBtn);
			}
			verteilung += "\n";
		}

		String bemerkungenString = "";
		for (String string : bemerkungen) {
			bemerkungenString += string + ", ";
		}

		return "." + getName() + " \n" + verteilung + durchschnitte + bemerkungenString + ".";
	}

	private void zaehleStimmenProRadioBtn(ArrayList<BewertungReferent> bewertungen) {
		for (BewertungReferent aktBewertung : bewertungen) {

			stimmenProRadioBtnVorbereitung[aktBewertung.getVorbereitet()] += 1;
			stimmenProRadioBtnFachwissen[aktBewertung.getFachwissen()] += 1;
			stimmenProRadioBtnEingehenAufProbleme[aktBewertung.getEingehenAufProbleme()] += 1;
			stimmenProRadioBtnInhaltsvermittlung[aktBewertung.getInhalteVermitteln()] += 1;
			stimmenProRadioBtnVerhalten[aktBewertung.getVerhalten()] += 1;
		}
	}

	public static List<AuswertungReferent> getAuswertungenAllerReferenten(ArrayList<AzubiAntwort> azubiAntworten) {
		List<AuswertungReferent> auswertungenAllerReferenten = new ArrayList<>();

		List<ArrayList<BewertungReferent>> sortierteBewertungen = new ArrayList<>();

		int anzahlReferenten = azubiAntworten.get(0).referenten.size();

		for (int positionRefImFragebogen = 0; positionRefImFragebogen < anzahlReferenten; positionRefImFragebogen++) {

			ArrayList<BewertungReferent> bewertungenEinesReferenten = new ArrayList<BewertungReferent>();

			for (int j = 0; j < azubiAntworten.size(); j++) {
				bewertungenEinesReferenten.add(azubiAntworten.get(j).referenten.get(positionRefImFragebogen));
			}

			auswertungenAllerReferenten.add(new AuswertungReferent(bewertungenEinesReferenten));
		}
		return auswertungenAllerReferenten;

	}

}
