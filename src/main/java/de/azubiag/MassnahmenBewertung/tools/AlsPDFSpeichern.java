package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.upload.Upload;

public class AlsPDFSpeichern {
	AzubiAntwort aa;
	AuswertungMassnahme am;
	ArrayList<String> als = new ArrayList<>();

	public static void main(String[] args) {
		new AlsPDFSpeichern();
	}

	public AlsPDFSpeichern() {
		beispielAuswertungMassnahmeErstellen();
		saveAsPDF();
	}

	public void beispielAuswertungMassnahmeErstellen() {
		als.add("1|3|0|4|Nö|3||Lass mich in Ruhe|Robert Hackfuß|3|1|2|2|0|Warum trägt er immer ein Hackebeil?|Franz Karrenschlepper|3|1|0|0|3|Seine Augen sind immer weit offen.Das ist unheimlich!|");
		als.add("2|4|3|1|Alles war toll|4|Das war super toll|Es war klasse!|Robert Hackfuß|4|4|4|4|0|Er macht mir Angst!|Franz Karrenschlepper|2|0|0|0|4||");
		als.add("5|5|4|4||0|Gr8 b8 m8 r8 8/8|Werf Sie raus!!!|Robert Hackfuß|1|2|1|0|3||Franz Karrenschlepper|0|1|1|0|0|Er hätte beim Karrenschleppen bleiben sollen.");
		am = am.getAuswertungMassnahme(aa.konvertiereStringsInAzubiAntworten(als));
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
			Paragraph m1 = new Paragraph();
			
			Paragraph title1 = new Paragraph("1. Maßnahmenverlauf", font);
			leerzeichenSetzen(title1);
			title1.add(new Chunk("-2 -1  0  1  2"));
			
			Paragraph t1q1 = new Paragraph("Wie empfinden die Teilnehmer die Organisation des Seminars?",font);
			leerzeichenSetzen(t1q1);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q1.add(new Chunk(" "+String.valueOf(am.pktvertOrg[i])+" "));
			}	
			
			Paragraph t1q2 = new Paragraph("Wie empfinden die Teilnehmer den Verlauf des Seminars?", font);
			leerzeichenSetzen(t1q2);
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q2.add(new Chunk(" "+String.valueOf(am.pktvertVerl[i])+" "));
			}
			
			Paragraph t1b = new Paragraph("Bemerkungen dazu:\n", font);
			for (int i = 0; i < am.alleBemerkVerl.size(); i++) {
				t1b.add(new Phrase(am.alleBemerkVerl.get(i)+"; "));
			}
			
			m1.add(title1);
			m1.add(t1q1);
			m1.add(t1q2);
			m1.add(t1b);
			
			document.add(m1);
			
			Paragraph m2 = new Paragraph("", font);
			
			Paragraph title2 = new Paragraph("2. Maßnahmebetreuung");
			leerzeichenSetzen(title2);
			title2.add(new Chunk("-2 -1  0  1  2"));
			
			Paragraph t2q1 = new Paragraph("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?");
			leerzeichenSetzen(t2q1);
			for (int i = 0; i < am.alleBemerkBetrng.size(); i++) {
				t2q1.add(new Phrase(String.valueOf(am.pktvertBetrng[i])));
			}
			
			for (int i = 0; i < am.alleBemerkRefAllg.size(); i++) {
				document.add(new Phrase(am.alleBemerkRefAllg.get(i)));
			}
			
			m2.add(title2);
			m2.add(t2q1);
			
			document.add(m2);
			
			document.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void leerzeichenSetzen(Paragraph paragraph) {
		while(paragraph.getContent().length()<70) {
			paragraph.add(" ");
		}
	}
	
}
