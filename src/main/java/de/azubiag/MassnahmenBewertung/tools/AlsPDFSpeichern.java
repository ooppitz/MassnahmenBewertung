package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import de.azubiag.MassnahmenBewertung.UI.FragebogenEigenschaften;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.auswertung.Frage;
import de.azubiag.MassnahmenBewertung.testdaten.Testdaten;
import de.azubiag.MassnahmenBewertung.upload.Upload;

public class AlsPDFSpeichern {
	
	static FragebogenEigenschaften feTest;
	static AuswertungMassnahme amTest;
	static List<AuswertungReferent> arTest;

	public static void main(String[] args) {
		
		feTest = Testdaten.getFragebogenEigenschaften();
		amTest = Testdaten.getAuswertungMassnahme();
		arTest = Testdaten.getAuswertungReferenten();
		
		File outputFile;
		try {
			outputFile = new File( Upload.getInstance().getRepositoryPfad() + "output.pdf");
			saveAsPDF(outputFile, feTest, amTest, arTest);
			System.out.println("Datei " + outputFile + " wurde erzeugt.");
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}	
	}

	/** Speichert die Ergebnisse (wovon???) in einem PDF File.
	 * 
	 * @param file Das File, in dem die Ergebnisse gesichert werden.
	 */
	public static void saveAsPDF(File file, FragebogenEigenschaften fe, AuswertungMassnahme am, List<AuswertungReferent> ar) {
		
		Document document = new Document();
		try {
			try {
				PdfWriter.getInstance(document, new FileOutputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			document.open();

			Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
			// Chunk chunk = new Chunk("Hello World", font);

			// document.add(chunk);

			Paragraph header = new Paragraph("", font);
			header.setSpacingAfter(50);
			header.add(new Phrase("Maßnahme von " + fe.von_datum + " bis " + fe.bis_datum + "\n"));
			header.add(new Phrase("Auftragsnummer: " + fe.auftrags_nummer + "\n"));
			header.add(new Phrase("Seminarleitung: " + fe.seminarleiter_name + "\n"));
			header.add(new Phrase("Datum: " + fe.ausstellungs_datum));

			document.add(header);

			Paragraph m1 = new Paragraph("", font);
			m1.setSpacingAfter(50);

			Phrase title1 = new Phrase("1. Maßnahmenverlauf\n");
			Phrase durchschnitt1 = new Phrase();
			leerzeichenSetzen(durchschnitt1);
			durchschnitt1.add(new Chunk("-2 -1  0  1  2    Ø\n"));
			
			Phrase t1q1 = druckeZeileMitPunkten("Wie empfinden die Teilnehmer die Organisation des Seminars?", am.pktvertOrg, am.durchschnOrg);
			
			t1q1.add("\n");

			Phrase t1q2 = druckeZeileMitPunkten("Wie empfinden die Teilnehmer den Verlauf des Seminars?", am.pktvertVerl, am.durchschnVerl);

			t1q2.add("\n");

			Paragraph t1b = new Paragraph("Bemerkungen dazu:\n", font);
			for (int i = 0; i < am.alleBemerkVerl.size(); i++) {
				t1b.add(new Chunk(am.alleBemerkVerl.get(i) + ";\n"));
			}

			m1.add(title1);
			m1.add(durchschnitt1);
			m1.add(t1q1);
			m1.add(t1q2);
			m1.add(t1b);

			document.add(m1);

			Paragraph m2 = new Paragraph("", font);
			m2.setSpacingAfter(50);

			Phrase title2 = new Phrase("2. Maßnahmebetreuung\n");
			Phrase durchschnitt2 = new Phrase();
			leerzeichenSetzen(durchschnitt2);
			durchschnitt2.add(new Chunk("-2 -1  0  1  2    Ø\n"));

			Phrase t2q1 = druckeZeileMitPunkten("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?", am.pktvertBetrng, am.durchschnBetrng);

			Paragraph t2b1 = new Paragraph("Bermerkungen dazu:\n", font);
			for (int i = 0; i < am.alleBemerkBetrng.size(); i++) {
				t2b1.add(new Chunk(am.alleBemerkBetrng.get(i) + ";\n", font));
			}

			m2.add(title2);
			m2.add(durchschnitt2);
			m2.add(t2q1);
			m2.add(t2b1);

			document.add(m2);

			Paragraph title3 = new Paragraph("3. Bewertung der Referenten bzw. Referentinnen:\n", font);
			for (int i = 0; i < am.alleBemerkRefAllg.size(); i++) {
				title3.add(new Chunk(am.alleBemerkRefAllg.get(i) + ";\n", font));
			}
			title3.setSpacingAfter(50);

			document.add(title3);

			Paragraph title4 = new Paragraph("4. Auswertung der Referenten:\n", font);

			document.add(title4);

			Paragraph rAlle = new Paragraph("", font);

			Phrase rVorb = new Phrase();
			Phrase rFach = new Phrase();
			Phrase rEing = new Phrase();
			Phrase rInh = new Phrase();
			Phrase rVerh = new Phrase();
			Phrase rBem = new Phrase();

			for (int i = 0; i < ar.size(); i++) {
				rAlle.add(new Chunk("\nReferent / Referentin: " + ar.get(i).getName() + "\n"));
				Phrase durchschnitt3 = new Phrase();
				leerzeichenSetzen(durchschnitt3);
				durchschnitt3.add(new Chunk("-2 -1  0  1  2    Ø\n"));
				rAlle.add(durchschnitt3);

				rVorb.add(new Chunk("Wie war ihr/sein Unterricht vorbereitet?"));
				leerzeichenSetzen(rVorb);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rVorb.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.VORBEREITUNG, j)) + " "));
				}
				if (ar.get(i).durchschnittVorbereitung >= 0) {
					rVorb.add(" ");
				}
				rVorb.add(new Chunk("  " + String.format("%1.2f", ar.get(i).durchschnittVorbereitung)));
				rVorb.add("\n");

				rFach.add(new Chunk("Wie umfangreich war ihr/sein Fachwissen?"));
				leerzeichenSetzen(rFach);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rFach.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.FACHWISSEN, j)) + " "));
				}
				if (ar.get(i).durchschnittFachwissen >= 0) {
					rFach.add(" ");
				}
				rFach.add(new Chunk("  " + String.format("%1.2f", ar.get(i).durchschnittFachwissen)));
				rFach.add("\n");

				rEing.add(new Chunk("Wie ging sie/er auf spezielle thematische Probleme ein?"));
				leerzeichenSetzen(rEing);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rEing.add(new Chunk(" "
							+ String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.EINGEHENAUFPROBLEME, j)) + " "));
				}
				if (ar.get(i).durchschnittEingehenAufProbleme >= 0) {
					rEing.add(" ");
				}
				rEing.add(new Chunk("  " + String.format("%1.2f", ar.get(i).durchschnittEingehenAufProbleme)));
				rEing.add("\n");

				rInh.add(new Chunk("Wie verständlich konnte sie/er die Inhalte vermitteln?"));
				leerzeichenSetzen(rInh);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rInh.add(new Chunk(" "
							+ String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.INHALTSVERMITTLUNG, j)) + " "));
				}
				if (ar.get(i).durchschnittInhaltsvermittlung >= 0) {
					rInh.add(" ");
				}
				rInh.add(new Chunk("  " + String.format("%1.2f", ar.get(i).durchschnittInhaltsvermittlung)));
				rInh.add("\n");

				rVerh.add(new Chunk("Wie sagte Ihnen ihr/sein Verhalten zu?"));
				leerzeichenSetzen(rVerh);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rVerh.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.VORBEREITUNG, j)) + " "));
				}
				if (ar.get(i).durchschnittVerhalten >= 0) {
					rVerh.add(" ");
				}
				rVerh.add(new Chunk("  " + String.format("%1.2f", ar.get(i).durchschnittVerhalten)));
				rVerh.add("\n");

				rBem.add(new Chunk("\nBermerkungen zu: " + ar.get(i).name + "\n"));
				for (int j = 0; j < ar.get(i).bemerkungen.size(); j++) {
					rBem.add(new Chunk(ar.get(i).bemerkungen.get(j) + ";\n"));
				}

				rAlle.add(rVorb);
				rVorb.clear();

				rAlle.add(rFach);
				rFach.clear();

				rAlle.add(rEing);
				rEing.clear();

				rAlle.add(rInh);
				rInh.clear();

				rAlle.add(rVerh);
				rVerh.clear();

				rAlle.add(rBem);
				rBem.clear();

			}

			document.add(rAlle);

			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

<<<<<<< HEAD
	private Phrase druckeZeileMitPunkten(String frageString, int[] pktvertArray, double durchschnitt) {
		Phrase phrase = new Phrase(frageString);
		leerzeichenSetzen(phrase);
		for (int i = 0; i < pktvertArray.length; i++) {
			phrase.add(new Chunk(" " + String.valueOf(pktvertArray[i]) + " "));
		}
		if (durchschnitt >= 0) {
			phrase.add(" ");
		}
		phrase.add(new Chunk("  " + String.format("%1.2f", durchschnitt)));
		return phrase;
	}

	void leerzeichenSetzen(Paragraph paragraph) {
=======
	static void leerzeichenSetzen(Paragraph paragraph) {
>>>>>>> Manuel_addFileLocationPickerPDF
		while (paragraph.getContent().length() < 65) {
			paragraph.add(" ");
		}
	}

	static void leerzeichenSetzen(Phrase phrase) {
		while (phrase.getContent().length() < 65) {
			phrase.add(" ");
		}
	}
}
