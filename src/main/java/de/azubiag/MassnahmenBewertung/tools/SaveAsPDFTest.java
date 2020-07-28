package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.azubiag.MassnahmenBewertung.upload.Upload;

public class SaveAsPDFTest {

	public static void main(String[] args) {
		// G:\Denis Neu\Programmspeicher\eclipse\workspace9&14\MassnahmenBewertung\src\main\java\de\azubiag\MassnahmenBewertung\tools\Probedaten.txt
		saveAsPDF();
	}

	public static void saveAsPDF() {
		String[][] stringarray = new String[3][3];
		for(int i=0; i<stringarray.length; i++) {
			for(int j=0; j<stringarray[i].length; j++) {
				stringarray[i][j]=i+" "+j;
			}
		}
		Document document = new Document();
		try {
			try {
				File file = new File(Upload.getInstance().getRepositoryPfad()+"\\iTextTable.pdf");
				PdfWriter.getInstance(document, new FileOutputStream(file));
			} catch (GitAPIException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			document.open();

			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Chunk chunk = new Chunk("Hello World", font);
			 
			document.add(chunk);
			
			PdfPTable table = new PdfPTable(3);
			
			for(int i=0; i<stringarray.length; i++) {
				for(int j=0; j<stringarray[i].length; j++) {
					table.addCell(stringarray[i][j]);
				}
			}

			document.add(table);
			document.close();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
