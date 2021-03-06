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
		double gesamtVorbereitung = 0;
		double gesamtFachwissen = 0;
		double gesamtEingehenAufProbleme = 0;
		double gesamtInhaltsvermittlung = 0;
		double gesamtVerhalten = 0;

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

		String durchschnitte = String.format("Durchschnitt Vorbereitung: %.2f,\nDurchschnitt Fachwissen: %.2f,\nDurchschnitt EingehenAufProbleme: %.2f, \nDurchschnitt Inhaltsvermittlung: %.2f, \nDurchschnitt Verhalten: %.2f, \n",  getDurchschnitt(Frage.VORBEREITUNG),  getDurchschnitt(Frage.FACHWISSEN),  getDurchschnitt(Frage.EINGEHENAUFPROBLEME),  getDurchschnitt(Frage.INHALTSVERMITTLUNG),  getDurchschnitt(Frage.VERHALTEN) 

				
				);

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
		for (String bemerkung : bemerkungen) {
			
			if (!bemerkung.equals("")) {
				bemerkungenString += bemerkung + ", ";
			}
		}

		return  getName() + " \n" + verteilung + durchschnitte + bemerkungenString ;
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

	public static List<AuswertungReferent> getAuswertungenAllerReferenten(List<AzubiAntwort> azubiAntworten) {
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
