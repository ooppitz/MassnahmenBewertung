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
	FragebogenEigenschaften fe;
	AuswertungMassnahme am;
	List<AuswertungReferent> ar;

	public static void main(String[] args) {
		new AlsPDFSpeichern();
	}

	public AlsPDFSpeichern() {
		beispielAuswertungMassnahmeErstellen();
		saveAsPDF();
	}

	public void beispielAuswertungMassnahmeErstellen() {
		fe = Testdaten.getFragebogenEigenschaften();
		am = Testdaten.getAuswertungMassnahme();
		ar = Testdaten.getAuswertungReferenten();
	}

	public void saveAsPDF() {
		Document document = new Document();
		try {
			try {
				File file = new File(Upload.getInstance().getRepositoryPfad() + "\\iTextTable.pdf");
				PdfWriter.getInstance(document, new FileOutputStream(file));
			} catch (GitAPIException | IOException e) {
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
			durchschnitt1.add(new Chunk("-2 -1  0  1  2   Ø\n"));

			Phrase t1q1 = new Phrase("Wie empfinden die Teilnehmer die Organisation des Seminars?");
			leerzeichenSetzen(t1q1);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q1.add(new Chunk(" " + String.valueOf(am.pktvertOrg[i]) + " "));
			}
			t1q1.add(new Chunk(" " + String.valueOf(am.durchschnOrg)));
			t1q1.add("\n");

			Phrase t1q2 = new Phrase("Wie empfinden die Teilnehmer den Verlauf des Seminars?");
			leerzeichenSetzen(t1q2);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q2.add(new Chunk(" " + String.valueOf(am.pktvertVerl[i]) + " "));
			}
			t1q2.add(new Chunk(" " + String.valueOf(am.durchschnVerl)));
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

			Paragraph title2 = new Paragraph("2. Maßnahmebetreuung", font);
			Phrase durchschnitt2 = new Phrase();
			leerzeichenSetzen(durchschnitt2);
			durchschnitt2.add(new Chunk("-2 -1  0  1  2   Ø\n"));

			Paragraph t2q1 = new Paragraph("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?", font);
			leerzeichenSetzen(t2q1);
			for (int i = 0; i < am.pktvertBetrng.length; i++) {
				t2q1.add(new Chunk(" " + String.valueOf(am.pktvertBetrng[i]) + " "));
			}
			t2q1.add(new Chunk(" " + String.valueOf(am.durchschnBetrng)));

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
				durchschnitt3.add(new Chunk("-2 -1  0  1  2   Ø\n"));
				rAlle.add(durchschnitt3);

				rVorb.add(new Chunk("Wie war ihr/sein Unterricht vorbereitet?"));
				leerzeichenSetzen(rVorb);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rVorb.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.VORBEREITUNG, j)) + " "));
				}
				rVorb.add(new Chunk(" " + String.valueOf(ar.get(i).durchschnittVorbereitung)));
				rVorb.add("\n");

				rFach.add(new Chunk("Wie umfangreich war ihr/sein Fachwissen?"));
				leerzeichenSetzen(rFach);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rFach.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.FACHWISSEN, j)) + " "));
				}
				rFach.add(new Chunk(" " + String.valueOf(ar.get(i).durchschnittFachwissen)));
				rFach.add("\n");

				rEing.add(new Chunk("Wie ging sie/er auf spezielle thematische Probleme ein?"));
				leerzeichenSetzen(rEing);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rEing.add(new Chunk(" "
							+ String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.EINGEHENAUFPROBLEME, j)) + " "));
				}
				rEing.add(new Chunk(" " + String.valueOf(ar.get(i).durchschnittEingehenAufProbleme)));
				rEing.add("\n");

				rInh.add(new Chunk("Wie verständlich konnte sie/er die Inhalte vermitteln?"));
				leerzeichenSetzen(rInh);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rInh.add(new Chunk(" "
							+ String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.INHALTSVERMITTLUNG, j)) + " "));
				}
				rInh.add(new Chunk(" " + String.valueOf(ar.get(i).durchschnittInhaltsvermittlung)));
				rInh.add("\n");

				rVerh.add(new Chunk("Wie sagte Ihnen ihr/sein Verhalten zu?"));
				leerzeichenSetzen(rVerh);
				for (int j = 0; j < ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rVerh.add(new Chunk(
							" " + String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.VORBEREITUNG, j)) + " "));
				}
				rVerh.add(new Chunk(" " + String.valueOf(ar.get(i).durchschnittVerhalten)));
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

	void leerzeichenSetzen(Paragraph paragraph) {
		while (paragraph.getContent().length() < 65) {
			paragraph.add(" ");
		}
	}

	void leerzeichenSetzen(Phrase phrase) {
		while (phrase.getContent().length() < 65) {
			phrase.add(" ");
		}
	}
}
