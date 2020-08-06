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
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import de.azubiag.MassnahmenBewertung.UI.FragebogenEigenschaften;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
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

		File outputFile = null;

		try {
			outputFile = new File(Upload.getInstance().getProgrammDatenOrdner() + "\\output.pdf");
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveAsPDF(outputFile, feTest, amTest, arTest);
		System.out.println("Datei " + outputFile + " wurde erzeugt.");

	}

	/**
	 * Speichert die Ergebnisse in einem PDF File.
	 * 
	 * @param file Das File, in dem die Ergebnisse gesichert werden.
	 */
	public static void saveAsPDF(File file, FragebogenEigenschaften fe, AuswertungMassnahme am,
			List<AuswertungReferent> ar) {

		try {

			Document document = new Document();

			PdfWriter.getInstance(document, new FileOutputStream(file));

			document.open();

			druckeMassnahmenBewertung(document, fe, am);

			druckeParagraphReferentenIndividuell(document, ar);

			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void druckeMassnahmenBewertung(Document document, FragebogenEigenschaften fe, AuswertungMassnahme am)
			throws DocumentException {

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

		Phrase zeileAlleBemVerl = druckeZeileMitBemerkungen("\nBemerkungen dazu:", am.alleBemerkVerl);

		massnahmenverlauf.add(titelMassnahmenverlauf);
		massnahmenverlauf.add(spaltenMassnahmenverlauf);
		massnahmenverlauf.add(zeilePktOrg);
		massnahmenverlauf.add(zeilePktVerl);
		massnahmenverlauf.add(zeileAlleBemVerl);

		document.add(massnahmenverlauf);

		Paragraph massnahmenbetreuung = paragraphSetzen();

		Phrase titelMassnahmenbetreuung = titelSetzen("2. Maßnahmenbetreuung");

		Phrase spaltenMassnahmenbetreuung = punkteSpaltenSetzen();

		Phrase zeilePktBetrng = druckeZeileMitPunkten("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?",
				am.pktvertBetrng, am.durchschnBetrng);

		Phrase zeileAlleBemBetrng = druckeZeileMitBemerkungen("\nBemerkungen dazu:", am.alleBemerkBetrng);

		massnahmenbetreuung.add(titelMassnahmenbetreuung);
		massnahmenbetreuung.add(spaltenMassnahmenbetreuung);
		massnahmenbetreuung.add(zeilePktBetrng);
		massnahmenbetreuung.add(zeileAlleBemBetrng);

		document.add(massnahmenbetreuung);

		Paragraph bewertungReferentenAllgemein = paragraphSetzen();

		Phrase titelBewertungReferentenAllgemein = titelSetzen("3. Bewertung der Referenten bzw. Referentinnen:");

		Phrase zeileAlleBemRefAllg = druckeZeileMitBemerkungen("", am.alleBemerkRefAllg);

		bewertungReferentenAllgemein.add(titelBewertungReferentenAllgemein);
		bewertungReferentenAllgemein.add(zeileAlleBemRefAllg);

		document.add(bewertungReferentenAllgemein);
	}

	private static void druckeParagraphReferentenIndividuell(Document document, List<AuswertungReferent> ar)
			throws DocumentException {

		Paragraph bewertungReferentenIndividuell = paragraphSetzen();

		Phrase titelBewertungReferentenIndividuell = titelSetzen("4. Auswertung der Referenten:");

		bewertungReferentenIndividuell.add(titelBewertungReferentenIndividuell);

		for (int i = 0; i < ar.size(); i++) {

			Phrase titelReferentName, referentPktVorb, referentPktFach, referentPktEing, referentPktInh,
					referentPktVerh, referentBem;

			bewertungReferentenIndividuell.add("\n");

			titelReferentName = new Phrase(
					new Chunk("Referent / Referentin: " + ar.get(i).getName() + "\n").setUnderline(1, -3));

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

			referentBem = druckeZeileMitBemerkungen("\nBemerkungen zu " + ar.get(i).name + ":\n",
					ar.get(i).getBemerkungen());

			bewertungReferentenIndividuell.add(referentPktVorb);
			bewertungReferentenIndividuell.add(referentPktFach);
			bewertungReferentenIndividuell.add(referentPktEing);
			bewertungReferentenIndividuell.add(referentPktInh);
			bewertungReferentenIndividuell.add(referentPktVerh);
			bewertungReferentenIndividuell.add(referentBem);

		}

		document.add(bewertungReferentenIndividuell);
	}

	private static Phrase titelSetzen(String titel) {
		Phrase phrase = new Phrase(titel + "\n", FontFactory.getFont(FontFactory.COURIER_BOLD, 12, BaseColor.BLACK));
		return phrase;
	}

	private static Phrase punkteSpaltenSetzen() {
		Phrase phrase = new Phrase();
		leerzeichenSetzen(phrase);
		phrase.add(new Chunk("-2 -1  0 +1 +2    Ø\n"));
		return phrase;
	}

	private static Paragraph paragraphSetzen() {
		Paragraph paragraph = new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 10, BaseColor.BLACK));
		paragraph.setSpacingAfter(25);
		return paragraph;
	}

	private static Phrase druckeZeileMitBemerkungen(String titel, List<String> bemerkungen) {
		Phrase paragraph = new Phrase(titel + "\n");
		for (int i = 0; i < bemerkungen.size(); i++) {
			if (!bemerkungen.get(i).equals("")) {
				paragraph.add(new Chunk(bemerkungen.get(i) + ";\n"));
			}
		}
		return paragraph;
	}

	private static Phrase druckeZeileMitPunkten(String frageString, int[] pktvertArray, double durchschnitt) {
		Phrase phrase = new Phrase(frageString);
		leerzeichenSetzen(phrase);
		for (int i = 0; i < pktvertArray.length; i++) {
			if (pktvertArray[i] < 10) {
				phrase.add(" ");
			}
			phrase.add(new Chunk(String.valueOf(pktvertArray[i]) + " "));
		}
		if (durchschnitt >= 0) {
			phrase.add(" ");
		}
		phrase.add(new Chunk("  " + String.format("%1.2f\n", durchschnitt)));
		return phrase;
	}

	static void leerzeichenSetzen(Paragraph paragraph) {
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
