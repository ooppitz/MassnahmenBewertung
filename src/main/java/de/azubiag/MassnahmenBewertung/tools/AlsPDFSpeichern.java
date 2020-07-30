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
			header.add(new Phrase("Maßnahme von "+fe.von_datum+" bis "+fe.bis_datum+"\n"));
			header.add(new Phrase("Auftragsnummer: "+fe.auftrags_nummer+"\n"));
			header.add(new Phrase("Seminarleitung: "+fe.seminarleiter_name+"\n"));
			header.add(new Phrase("Datum: "+fe.ausstellungs_datum));
			
			document.add(header);
			
			Paragraph m1 = new Paragraph("", font);
			m1.setSpacingAfter(50);
			
			Phrase title1 = new Phrase("1. Maßnahmenverlauf");
			leerzeichenSetzen(title1);
			title1.add(new Chunk("-2 -1  0  1  2\n"));
			
			Phrase t1q1 = new Phrase("Wie empfinden die Teilnehmer die Organisation des Seminars?");
			leerzeichenSetzen(t1q1);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q1.add(new Chunk(" "+String.valueOf(am.pktvertOrg[i])+" "));
			}	
			t1q1.add("\n");
			
			Phrase t1q2 = new Phrase("Wie empfinden die Teilnehmer den Verlauf des Seminars?");
			leerzeichenSetzen(t1q2);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q2.add(new Chunk(" "+String.valueOf(am.pktvertVerl[i])+" "));
			}
			t1q2.add("\n");
			
			Paragraph t1b = new Paragraph("Bemerkungen dazu:\n", font);
			for (int i = 0; i < am.alleBemerkVerl.size(); i++) {
				t1b.add(new Chunk(am.alleBemerkVerl.get(i)+"; "));
			}
			
			m1.add(title1);
			m1.add(t1q1);
			m1.add(t1q2);
			m1.add(t1b);
			
			document.add(m1);
			
			Paragraph m2 = new Paragraph();
			m2.setSpacingAfter(50);
			
			Paragraph title2 = new Paragraph("2. Maßnahmebetreuung",font);
			leerzeichenSetzen(title2);
			title2.add(new Chunk("-2 -1  0  1  2"));
			
			Paragraph t2q1 = new Paragraph("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?",font);
			leerzeichenSetzen(t2q1);
			for (int i = 0; i < am.pktvertBetrng.length; i++) {
				t2q1.add(new Chunk(" "+String.valueOf(am.pktvertBetrng[i])+" "));
			}
			Paragraph t2b1 = new Paragraph("Bermerkungen dazu:\n",font);
			for (int i = 0; i < am.alleBemerkBetrng.size(); i++) {
				t2b1.add(new Chunk(am.alleBemerkBetrng.get(i)+"; ",font));
			}
			
			m2.add(title2);
			m2.add(t2q1);
			m2.add(t2b1);
			
			document.add(m2);
			
			Paragraph title3 = new Paragraph("3. Bewertung der Referenten bzw. Referentinnen:\n",font);
			for (int i = 0; i < am.alleBemerkRefAllg.size(); i++) {
				title3.add(new Chunk(am.alleBemerkRefAllg.get(i)+"; ",font));
			}
			title3.setSpacingAfter(50);
			
			document.add(title3);
			
			Paragraph title4 = new Paragraph("4. Auswertung der Referenten:", font);
			
			document.add(title4);
			
			Paragraph r = new Paragraph("", font);
			
			Phrase rVorb = new Phrase("");
			for(int i = 0; i<ar.size(); i++) {
				r.add(new Chunk(ar.get(i).name+"\n"));
				rVorb.add(new Chunk("Wie war Ihr/Sein Unterricht vorbereitet?"));
				leerzeichenSetzen(rVorb);
				for(int j=0; j<ar.get(i).stimmenProRadioBtnVorbereitung.length; j++) {
					rVorb.add(new Chunk(" "+String.valueOf(ar.get(i).getStimmenProRadioButton(Frage.VORBEREITUNG, j))+" "));	
				}	
				rVorb.add("\n");
				
				
				
				
				
				
				
				
				
				
				
				
				r.add(rVorb);
				rVorb.clear();
			}
			
			
			document.add(r);
			
			document.close();

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	void leerzeichenSetzen(Paragraph paragraph) {
		while(paragraph.getContent().length()<70) {
			paragraph.add(" ");
		}
	}
	
	void leerzeichenSetzen(Phrase phrase) {
		while(phrase.getContent().length()<70) {
			phrase.add(" ");
		}
	}
}
