package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import de.azubiag.MassnahmenBewertung.UI.FragebogenEigenschaften;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
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
				File file = new File(System.getenv("LOCALAPPDATA")
						+ "\\MassnahmenBewertung\\gfigithubaccess.github.io\\iTextTable.pdf");
				PdfWriter.getInstance(document, new FileOutputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
			document.open();

			// Chunk chunk = new Chunk("Hello World", font);

			// document.add(chunk);

			Paragraph kopf = paragraphSetzen();
			kopf.add(new Phrase("Maßnahme von " + fe.von_datum + " bis " + fe.bis_datum + "\n"));
			kopf.add(new Phrase("Auftragsnummer: " + fe.auftrags_nummer + "\n"));
			kopf.add(new Phrase("Seminarleitung: " + fe.seminarleiter_name + "\n"));
			kopf.add(new Phrase("Datum: " + fe.ausstellungs_datum));

			document.add(kopf);

			Paragraph massnahmenverlauf = paragraphSetzen();

			Phrase titelMassnahmenverlauf = titelSetzen("1. Maßnahmenverlauf");

			Phrase spaltenMassnahmenverlauf = punkteSpaltenSetzen();

			Phrase zeilePktOrg = druckeZeileMitPunkten("Wie empfinden die Teilnehmer die Organisation des Seminars?",
					am.pktvertOrg, am.durchschnOrg);

			Phrase zeilePktVerl = druckeZeileMitPunkten("Wie empfinden die Teilnehmer den Verlauf des Seminars?",
					am.pktvertVerl, am.durchschnVerl);

			Phrase zeileAlleBemVerl = druckeZeileMitBemerkungen("Bemerkungen dazu:", am.alleBemerkVerl);

			massnahmenverlauf.add(titelMassnahmenverlauf);
			massnahmenverlauf.add(spaltenMassnahmenverlauf);
			massnahmenverlauf.add(zeilePktOrg);
			massnahmenverlauf.add(zeilePktVerl);
			massnahmenverlauf.add(zeileAlleBemVerl);

			document.add(massnahmenverlauf);

			Paragraph massnahmenbetreuung = paragraphSetzen();

			Phrase titelMassnahmenbetreuung = titelSetzen("2. Maßnahmenbetreuung");

			Phrase spaltenMassnahmenbetreuung = punkteSpaltenSetzen();

			Phrase zeilePktBetrng = druckeZeileMitPunkten(
					"Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?", am.pktvertBetrng,
					am.durchschnBetrng);

			Phrase zeileAlleBemBetrng = druckeZeileMitBemerkungen("Bemerkungen dazu:", am.alleBemerkBetrng);

			massnahmenbetreuung.add(titelMassnahmenbetreuung);
			massnahmenbetreuung.add(spaltenMassnahmenbetreuung);
			massnahmenbetreuung.add(zeilePktBetrng);
			massnahmenbetreuung.add(zeileAlleBemBetrng);

			document.add(massnahmenbetreuung);

			Paragraph bewertungReferentenAllgemein = paragraphSetzen();

			Phrase titelBewertungReferentenAllgemein = druckeZeileMitBemerkungen(
					"3. Bewertung der Referenten bzw. Referentinnen:", am.alleBemerkRefAllg);

			bewertungReferentenAllgemein.add(titelBewertungReferentenAllgemein);

			document.add(bewertungReferentenAllgemein);

			Paragraph bewertungReferentenIndividuell = paragraphSetzen();

			Phrase titelBewertungReferentenIndividuell = titelSetzen("4. Auswertung der Referenten:");

			bewertungReferentenIndividuell.add(titelBewertungReferentenIndividuell);

			Phrase titelReferentName;
			Phrase referentPktVorb;
			Phrase referentPktFach;
			Phrase referentPktEing;
			Phrase referentPktInh;
			Phrase referentPktVerh;
			Phrase referentBem;

			for (int i = 0; i < ar.size(); i++) {

				titelReferentName = new Phrase("\nReferent / Referentin: " + ar.get(i).getName() + "\n");

				bewertungReferentenIndividuell.add(titelReferentName);

				Phrase durchschnitt3 = punkteSpaltenSetzen();

				bewertungReferentenIndividuell.add(durchschnitt3);

				referentPktVorb = druckeZeileMitPunkten("Wie war ihr/sein Unterricht vorbereitet?",
						ar.get(i).stimmenProRadioBtnVorbereitung, ar.get(i).durchschnittVorbereitung);

				referentPktFach = druckeZeileMitPunkten("Wie umfangreich war ihr/sein Fachwissen?",
						ar.get(i).stimmenProRadioBtnFachwissen, ar.get(i).durchschnittFachwissen);

				referentPktEing = druckeZeileMitPunkten("Wie ging sie/er auf spezielle thematische Probleme ein?",
						ar.get(i).stimmenProRadioBtnEingehenAufProbleme, ar.get(i).durchschnittEingehenAufProbleme);

				referentPktInh = druckeZeileMitPunkten("Wie verständlich konnte sie/er die Inhalte vermitteln?",
						ar.get(i).stimmenProRadioBtnInhaltsvermittlung, ar.get(i).durchschnittInhaltsvermittlung);

				referentPktVerh = druckeZeileMitPunkten("Wie sagte Ihnen ihr/sein Verhalten zu?",
						ar.get(i).stimmenProRadioBtnVerhalten, ar.get(i).durchschnittVerhalten);

				referentBem = druckeZeileMitBemerkungen("\nBermerkungen zu: " + ar.get(i).name + "\n",
						ar.get(i).getBemerkungen());

				bewertungReferentenIndividuell.add(referentPktVorb);
				referentPktVorb.clear();

				bewertungReferentenIndividuell.add(referentPktFach);
				referentPktFach.clear();

				bewertungReferentenIndividuell.add(referentPktEing);
				referentPktEing.clear();

				bewertungReferentenIndividuell.add(referentPktInh);
				referentPktInh.clear();

				bewertungReferentenIndividuell.add(referentPktVerh);
				referentPktVerh.clear();

				bewertungReferentenIndividuell.add(referentBem);
				referentBem.clear();

			}

			document.add(bewertungReferentenIndividuell);

			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private Phrase titelSetzen(String titel) {
		Phrase phrase = new Phrase(titel + "\n");
		return phrase;
	}

	private Phrase punkteSpaltenSetzen() {
		Phrase phrase = new Phrase();
		leerzeichenSetzen(phrase);
		phrase.add(new Chunk("-2 -1  0  1  2    Ø\n"));
		return phrase;
	}

	private Paragraph paragraphSetzen() {
		Paragraph paragraph = new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK));
		paragraph.setSpacingAfter(50);
		return paragraph;
	}

	private Phrase druckeZeileMitBemerkungen(String titel, List<String> bemerkungen) {
		Phrase paragraph = new Phrase(titel + "\n");
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
		phrase.add(new Chunk("  " + String.format("%1.2f\n", durchschnitt)));
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
