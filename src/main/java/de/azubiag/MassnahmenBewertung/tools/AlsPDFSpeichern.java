package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

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

public class AlsPDFSpeichern {
	FragebogenEigenschaften fe;
	AuswertungMassnahme am;
	List<AuswertungReferent> ar;

	public static void main(String[] args) {
		new AlsPDFSpeichern();
		
		System.out.println("PDF wurde erzeugt!");
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
				File file = new File("C:\\Users\\denis\\AppData\\Local\\MassnahmenBewertung\\gfigithubaccess.github.io\\iTextTable.pdf");
				PdfWriter.getInstance(document, new FileOutputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			document.open();

			Font font = FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK);
			// Chunk chunk = new Chunk("Hello World", font);

			// document.add(chunk);

			Paragraph header = paragraphSetzen();
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

			Phrase t1b = druckeZeileMitBemerkungen("Bemerkungen dazu:", am.alleBemerkVerl);

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

			t2q1.add("\n");
			
			Phrase t2b1 = druckeZeileMitBemerkungen("Bemerkungen dazu", am.alleBemerkBetrng);

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

			Phrase rVorb;
			Phrase rFach;
			Phrase rEing;
			Phrase rInh;
			Phrase rVerh;
			Phrase rBem;

			for (int i = 0; i < ar.size(); i++) {
				rAlle.add(new Chunk("\nReferent / Referentin: " + ar.get(i).getName() + "\n"));
				Phrase durchschnitt3 = new Phrase();
				leerzeichenSetzen(durchschnitt3);
				durchschnitt3.add(new Chunk("-2 -1  0  1  2    Ø\n"));
				rAlle.add(durchschnitt3);

				rVorb = druckeZeileMitPunkten("Wie war ihr/sein Unterricht vorbereitet?", ar.get(i).stimmenProRadioBtnVorbereitung, ar.get(i).durchschnittVorbereitung);
				rVorb.add("\n");
				
				rFach = druckeZeileMitPunkten("Wie umfangreich war ihr/sein Fachwissen?", ar.get(i).stimmenProRadioBtnFachwissen, ar.get(i).durchschnittFachwissen);
				rFach.add("\n");
				
				rEing = druckeZeileMitPunkten("Wie ging sie/er auf spezielle thematische Probleme ein?", ar.get(i).stimmenProRadioBtnEingehenAufProbleme, ar.get(i).durchschnittEingehenAufProbleme);
				rEing.add("\n");
				
				rInh = druckeZeileMitPunkten("Wie verständlich konnte sie/er die Inhalte vermitteln?", ar.get(i).stimmenProRadioBtnInhaltsvermittlung, ar.get(i).durchschnittInhaltsvermittlung);
				rInh.add("\n");

				rVerh = druckeZeileMitPunkten("Wie sagte Ihnen ihr/sein Verhalten zu?", ar.get(i).stimmenProRadioBtnVerhalten, ar.get(i).durchschnittVerhalten);
				rVerh.add("\n");

				rBem = druckeZeileMitBemerkungen("\nBermerkungen zu: " + ar.get(i).name + "\n", ar.get(i).getBemerkungen());

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

	private Paragraph paragraphSetzen() {
		Paragraph paragraph = new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK));
		paragraph.setSpacingAfter(50);
		return paragraph;
	}

	private Phrase druckeZeileMitBemerkungen(String titel, List<String> bemerkungen) {
		Phrase paragraph = new Phrase(titel+"\n");
		for (int i = 0; i < bemerkungen.size(); i++) {
			paragraph.add(new Chunk(bemerkungen.get(i) + ";\n"));
		}
		return paragraph;
	}

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
