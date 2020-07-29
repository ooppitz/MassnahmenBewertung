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

public class SaveAsPDFTest {
	AzubiAntwort aa;
	AuswertungMassnahme am;
	ArrayList<String> als = new ArrayList<>();

	public static void main(String[] args) {
		new SaveAsPDFTest();
	}

	public SaveAsPDFTest() {
		createExampleAuswertungMassnahme();
		saveAsPDF();
	}

	public void createExampleAuswertungMassnahme() {
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

			// Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			// Chunk chunk = new Chunk("Hello World", font);

			// document.add(chunk);
			
			Chunk space = new Chunk("                    ");
			Paragraph title1 = new Paragraph("Maßnahmeverlauf");
			Paragraph t1q1 = new Paragraph("Wie empfinden die Teilnehmer die Organisation des Seminars?");
			for (int i = 0; i < am.pktvertOrg.length; i++) {
				t1q1.add(new Chunk(String.valueOf(am.pktvertOrg[i])+" "));
			}
			System.out.println(t1q1.size());
			Paragraph t1q2 = new Paragraph("Wie empfinden die Teilnehmer den Verlauf des Seminars");
			Paragraph t1b = new Paragraph("Bemerkungen dazu:");
			Chapter massnahmeverlauf = new Chapter(title1, 1);
			massnahmeverlauf.add(t1q1);
			massnahmeverlauf.add(t1q2);
			massnahmeverlauf.add(t1b);
			
			
			document.add(massnahmeverlauf);
			
			for (int i = 0; i < am.pktvertVerl.length; i++) {
				document.add(new Phrase(String.valueOf(am.pktvertVerl[i])));
			}
			for (int i = 0; i < am.alleBemerkVerl.size(); i++) {
				document.add(new Phrase(am.alleBemerkVerl.get(i)));
			}
			for (int i = 0; i < am.alleBemerkBetrng.size(); i++) {
				document.add(new Phrase(String.valueOf(am.pktvertBetrng[i])));
			}
			for (int i = 0; i < am.alleBemerkRefAllg.size(); i++) {
				document.add(new Phrase(am.alleBemerkRefAllg.get(i)));
			}
			document.add(new Phrase("Durchschnitt Organisation:"));
			document.add(new Phrase(String.valueOf(am.durchschnOrg)));
			document.add(new Phrase("Durchschnitt Verlauf:"));
			document.add(new Phrase(String.valueOf(am.durchschnVerl)));
			document.add(new Phrase("Durchschnitt Betreuung:"));
			document.add(new Phrase(String.valueOf(am.durchschnBetrng)));
			document.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
