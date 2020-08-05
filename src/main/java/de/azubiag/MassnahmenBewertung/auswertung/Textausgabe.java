package de.azubiag.MassnahmenBewertung.auswertung;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import de.azubiag.MassnahmenBewertung.UI.FragebogenEigenschaften;
import de.azubiag.MassnahmenBewertung.testdaten.Testdaten;

public class Textausgabe {

	static int zeilenNummer = 1;

	static ArrayList<String> zeilen = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		AuswertungMassnahme auswertungMassnahme = Testdaten.getAuswertungMassnahme();
		List<AuswertungReferent> auswertungenReferenten = Testdaten.getAuswertungReferenten();
		FragebogenEigenschaften eigenschaften = Testdaten.getFragebogenEigenschaften();
		
		ArrayList<String> ergebnis = erzeugeDarstellung(eigenschaften, auswertungMassnahme, auswertungenReferenten);
		
		String outputFileName = "c:/users/oliveroppitz/Simple.pdf";
		drucke(outputFileName, ergebnis);
		
		for(String s : ergebnis) {
			System.out.println(s);
		}

	}
	
	static void drucke(String outputFileName, ArrayList<String> text) throws IOException {
		
		PDDocument document = new PDDocument();
		PDFont fontMono = PDType1Font.COURIER;

		PDPage page = null;
		PDPageContentStream cos = null;
		PDRectangle rect = null;
		
		int fontSize = 8;
		int lineFeed = 12;
		int topMargin = 30;
		int leftMargin = 30;
		int linesPerPage = 64;
		int line = 1;

		int lineDebugCounter = 1;
		
		for (String s : text) {

			if (line % linesPerPage == 1) { // Create new page
				
				// Start to print on top of page
				line = 1; 
				
				// Close previous page, if any
				if (cos != null) {
					cos.close();
				}
				// PDRectangle rect = page1.getMediaBox();
				page = new PDPage(PDRectangle.A4);
				rect = page.getArtBox(); // rect can be used to get the page width and height
				document.addPage(page);
				cos = new PDPageContentStream(document, page);
			}

			// Define a text content stream using the selected font, move the cursor and
			// draw some text
			cos.beginText();
			cos.setFont(fontMono, fontSize);
			cos.newLineAtOffset(leftMargin, rect.getHeight() - topMargin - lineFeed * (line));
			line++;
			
			cos.showText(s);
			
			// cos.showText(lineDebugCounter + " " + s);
			// lineDebugCounter++;
			cos.endText();

		}

		// Make sure that the content stream is closed:
		cos.close();

		// Save the results and ensure that the document is properly closed:
		document.save(outputFileName);
		document.close();
	}

	static ArrayList<String> erzeugeDarstellung(FragebogenEigenschaften eigenschaften, AuswertungMassnahme auswertungMassnahme,
			List<AuswertungReferent> auswertungenReferenten) {

		addHeaderToGrid(eigenschaften);
		
		addBewertungVerlaufToGrid(auswertungMassnahme);
		addBewertungBetreuungToGrid(auswertungMassnahme);
		addBemerkungenReferentenAllgToGrid(auswertungMassnahme);
		
		addReferentenbewertungenToGrid(auswertungenReferenten);
		
		return zeilen;
	}
	

	static void outputLine(String line) {
		zeilen.add(line);
	}

	static void addTextToGrid(String textContent, int col, boolean linefeed) {
		outputLine(textContent);
	}

	static void addHeaderToGrid(FragebogenEigenschaften eigenschaften) {

		addTextToGrid("Maßnahme von " + eigenschaften.von_datum + " bis " + eigenschaften.bis_datum, 0, true);
		addTextToGrid("Auftragsnummer: " + eigenschaften.auftrags_nummer, 0, true);
		addTextToGrid("Seminarleitung: " + eigenschaften.seminarleiter_name, 0, true);
		addTextToGrid("Datum: " + eigenschaften.ausstellungs_datum, 0, true);
		addTextToGrid("  ", 0, true);
	}

	static void addBewertungVerlaufToGrid(AuswertungMassnahme auswertungMassnahme) {
		addPunkteVerlaufToGrid(auswertungMassnahme);
		addBemerkungenToGrid("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkVerl);
	}

	static void addPunkteVerlaufToGrid(AuswertungMassnahme auswertungMassnahme) {
		addTextToGrid("1. Maßnahmenverlauf:", 0, false);
		addWertungsheaderToGrid();
		addPunkteauswertungToGrid("Wie empfinden die Teilnehmer die Organisation des Seminars?",
				auswertungMassnahme.pktvertOrg, auswertungMassnahme.durchschnOrg);
		addPunkteauswertungToGrid("Wie empfinden die Teilnehmer den Verlauf des Seminars?",
				auswertungMassnahme.pktvertVerl, auswertungMassnahme.durchschnVerl);
	}

	static void addPunkteauswertungToGrid(String fragestellung, int[] punkteverteilung, double durchschnitt) {

		String zeile = String.format("%-75s %3d %3d %3d %3d %3d   % 2.2f", fragestellung, punkteverteilung[0], punkteverteilung[1],
				punkteverteilung[2], punkteverteilung[3], punkteverteilung[4], durchschnitt);
		outputLine(zeile);
		zeilenNummer++;
	}

	static void addWertungsheaderToGrid() {
		
		String zeile = String.format("%75s %3s %3s %3s %3s %3s   %3s ", "", "-2", "-1", "0", "+1", "+2", "Durchschnitt");
		outputLine(zeile);
		zeilenNummer++;
	}

	static void addBemerkungenToGrid(String ueberschrift, ArrayList<String> bemerkungenKategorie) {
		addTextToGrid(ueberschrift, 0, true);
		addTextToGrid("  ", 0, true);
		if (bemerkungenKategorie.isEmpty()) {
			addTextToGrid("(Es gibt keine Bemerkungen.)", 0, true);
		} else {
			int anzBemerkungen = bemerkungenKategorie.size();

			for (int i = 0; i < anzBemerkungen; i++) {
				String bemerkung = bemerkungenKategorie.get(i);
				addTextToGrid(bemerkung, 0, true);
				if (i < anzBemerkungen - 1) { // Bemerkungen verschiedener Teilnehmer voneinander trennen
					addTextToGrid("-------", 0, true);
				}
			}
		}
		addTextToGrid("  ", 0, true);
	}

	static void addBewertungBetreuungToGrid(AuswertungMassnahme auswertungMassnahme) {
		addTextToGrid("2. Maßnahmenbetreuung", 0, false);

		addWertungsheaderToGrid();
		addPunkteauswertungToGrid("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?",
				auswertungMassnahme.pktvertBetrng, auswertungMassnahme.durchschnBetrng);
		addBemerkungenToGrid("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkBetrng);
	}

	static void addBemerkungenReferentenAllgToGrid(AuswertungMassnahme auswertungMassnahme) {
		addBemerkungenToGrid("3.Bewertung der Referenten bzw. Referentinnen:", auswertungMassnahme.alleBemerkRefAllg);
	}

	static void addReferentenbewertungenToGrid(List<AuswertungReferent> auswertungenReferenten) {
		addTextToGrid("4.Auswertung der Referenten:", 0, true);
		addTextToGrid("  ", 0, true);

		for (AuswertungReferent auswertungReferent : auswertungenReferenten) {
			addTextToGrid("Referent / Referentin: " + auswertungReferent.getName(), 0, false);
			addWertungsheaderToGrid();

			addPunkteauswertungToGrid("Wie war ihr/sein Unterricht vorbereitet ?",
					auswertungReferent.stimmenProRadioBtnVorbereitung, auswertungReferent.durchschnittVorbereitung);
			addPunkteauswertungToGrid("Wie umfangreich war ihr/sein Fachwissen ? ",
					auswertungReferent.stimmenProRadioBtnFachwissen, auswertungReferent.durchschnittFachwissen);
			addPunkteauswertungToGrid("Wie ging sie/er auf spezielle thematische Probleme ein ? ",
					auswertungReferent.stimmenProRadioBtnEingehenAufProbleme,
					auswertungReferent.durchschnittEingehenAufProbleme);
			addPunkteauswertungToGrid("Wie verständlich sie/er die Inhalte vermitteln ? ",
					auswertungReferent.stimmenProRadioBtnInhaltsvermittlung,
					auswertungReferent.durchschnittInhaltsvermittlung);
			addPunkteauswertungToGrid("Wie sagte Ihnen ihr/sein Verhalten gegenüber den Seminarteilnehmern zu ? ",
					auswertungReferent.stimmenProRadioBtnVerhalten, auswertungReferent.durchschnittVerhalten);

			// ArrayList<String> bemerkungen =
			// filtereUndMischeList(auswertungReferent.getBemerkungen());
			ArrayList<String> bemerkungen = new ArrayList<String>(auswertungReferent.getBemerkungen());

			addTextToGrid("  ", 0, true);
			addBemerkungenToGrid("Bemerkungen zu: " + auswertungReferent.getName(), bemerkungen);
		}
	}

}
