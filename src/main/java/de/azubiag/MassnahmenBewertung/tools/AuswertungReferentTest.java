package de.azubiag.MassnahmenBewertung.tools;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.auswertung.Frage;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;

// TODO: Die Junit-Tests zum Laufen zu bringen

/**
 * 
 * @author Louisa
 *
 */
class AuswertungReferentTest {

	static ArrayList<BewertungReferent> bewertungen;
	static AuswertungReferent auswertungRef;

	static int[] expectedStimmenVorbereitung = { 5, 0, 0, 0, 0 };
	static int[] expectedStimmenFachwissen = { 0, 5, 0, 0, 0 };
	static int[] expectedStimmenEingehenAufProbleme = { 0, 0, 5, 0, 0 };
	static int[] expectedStimmenInhaltsvermittlung = { 0, 0, 0, 5, 0 };
	static int[] expectedStimmenVerhalten = { 0, 0, 0, 0, 5 };

	static Double expectedDurchschnittVorbereitung = new Double(-2);
	static Double expectedDurchschnittFachwissen = new Double(-1);
	static Double expectedDurchschnittEingehenAufProbleme = new Double(0);
	static Double expectedDurchschnittInhaltsvermittlung = new Double(1);
	static Double expectedDurchschnittVerhalten = new Double(2);

	static List<String> expectedBemerkungen = new ArrayList<>();

	@BeforeAll
	public static void setUpTestdata() {
		bewertungen = new ArrayList<BewertungReferent>();
		String [] inputForAzubiAntwort = {"Pfaffelhuber", "0", "1", "2", "3", "4",
				"mag ihn nicht"}; 
		
	
	//TODO: adjust to changes: static method getAuswertungenAllerReferenten(ArrayList <AzubiAntwort>);
	//TODO: change visibility AuswertungReferent attributes or write getters
			

		auswertungRef = new AuswertungReferent(bewertungen);

		expectedBemerkungen.add("nicht gerade gut ausgebildet");
		expectedBemerkungen.add("mag ihn nicht");
		expectedBemerkungen.add("so lala");
		expectedBemerkungen.add("prima Lehrer");
		expectedBemerkungen.add("klasse !");
	}

//	@Test
//	void test_01_AuswertungToString() {
//		AuswertungReferent auswRef = new AuswertungReferent(bewertungen);
//
//		assertEquals(".Pfaffelhuber \r\n" + "11111\r\n" + "11111\r\n"
//				+ "11111\r\n" + "11111\r\n" + "11111\r\n"
//				+ "Durchschnitt Vorbereitung: 0.0,\r\n"
//				+ "Durchschnitt Fachwissen: 0.0,\r\n"
//				+ "Durchschnitt EingehenAufProbleme: 0.0, \r\n"
//				+ "Durchschnitt Inhaltsvermittlung: 0.0, \r\n"
//				+ "Durchschnitt Verhalten: 0.0, \r\n"
//				+ "nicht gerade gut ausgebildet, mag ihn nicht, so lala, prima Lehrer, klasse !, .",
//				auswRef);
//
//	}
//test evaluation methods
	@Test

	void test_05_setName() {
		assertEquals(bewertungen.get(0).getName(), auswertungRef.name);
	}

	@Test
	void test_10_zaehleStimmenProRadioBTn() {

		assertArrayEquals(expectedStimmenVorbereitung,
				auswertungRef.stimmenProRadioBtnVorbereitung);
		assertArrayEquals(expectedStimmenFachwissen,
				auswertungRef.stimmenProRadioBtnFachwissen);
		assertArrayEquals(expectedStimmenEingehenAufProbleme,
				auswertungRef.stimmenProRadioBtnEingehenAufProbleme);
		assertArrayEquals(expectedStimmenInhaltsvermittlung,
				auswertungRef.stimmenProRadioBtnInhaltsvermittlung);
		assertArrayEquals(expectedStimmenVerhalten,
				auswertungRef.stimmenProRadioBtnVerhalten);
	}

	@Test
	void test_15_berechneDurchschnittJeFrage() {

		assertEquals(expectedDurchschnittVorbereitung,
				auswertungRef.durchschnittVorbereitung);
		assertEquals(expectedDurchschnittFachwissen,
				auswertungRef.durchschnittFachwissen);
		assertEquals(expectedDurchschnittEingehenAufProbleme,
				auswertungRef.durchschnittEingehenAufProbleme);
		assertEquals(expectedDurchschnittInhaltsvermittlung,
				auswertungRef.durchschnittInhaltsvermittlung);
		assertEquals(expectedDurchschnittVerhalten,
				auswertungRef.durchschnittVerhalten);
	}

	@Test
	void test_20_sammleBemerkungen() {

		assertEquals(expectedBemerkungen, auswertungRef.bemerkungen);
	}

//test getter methods
	@Test
	void test_25_getBemerkungen() {

		assertEquals(expectedBemerkungen, auswertungRef.getBemerkungen());
	}

	@Test
	void test_30_getDurchschnitt() {
		assertEquals(expectedDurchschnittVorbereitung,
				auswertungRef.getDurchschnitt(Frage.VORBEREITUNG));
		assertEquals(expectedDurchschnittFachwissen,
				auswertungRef.getDurchschnitt(Frage.FACHWISSEN));
		assertEquals(expectedDurchschnittEingehenAufProbleme,
				auswertungRef.getDurchschnitt(Frage.EINGEHENAUFPROBLEME));
		assertEquals(expectedDurchschnittInhaltsvermittlung,
				auswertungRef.getDurchschnitt(Frage.INHALTSVERMITTLUNG));
		assertEquals(expectedDurchschnittVerhalten,
				auswertungRef.getDurchschnitt(Frage.VERHALTEN));
	}

	@Test
	void test_35_getName() {
		assertEquals(bewertungen.get(0).getName(), auswertungRef.getName());
	}

	@Test
	void test_40_getStimmenProRadioButtonFuerFrage() {

		int[][] expectedArray = { expectedStimmenVorbereitung,
				expectedStimmenFachwissen, expectedStimmenEingehenAufProbleme,
				expectedStimmenInhaltsvermittlung, expectedStimmenVerhalten };
		Frage[] fragen = { Frage.VORBEREITUNG, Frage.FACHWISSEN,
				Frage.EINGEHENAUFPROBLEME, Frage.INHALTSVERMITTLUNG,
				Frage.VERHALTEN };

		for (int i = 0; i < fragen.length; i++) {
			for (int j = 0; j < 5; j++) {
				assertEquals(expectedArray[i][j], auswertungRef
						.getStimmenProRadioButton(fragen[i], j));

			}
		}
	}

}
