/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manuel.unverdorben
 */
/*
package de.azubiag.MassnahmenBewertung.htmlcreator.test;

import de.azubiag.MassnahmenBewertung.htmlcreator.HtmlCreator;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jgit.api.errors.GitAPIException;


public class App {

	public static void main(String[] args) throws IOException {

		ArrayList<String> referentenListe = new ArrayList<>();
		referentenListe.add("Smith");
		referentenListe.add("Collins");
		referentenListe.add("Webber");

		String seminarleitername = "testseminarleiter";
		String fragebogenname = "testfragebogenTest2.html";

		String fragebogenOutputPfad;
		try {
			fragebogenOutputPfad = Upload.getInstance().getRepositoryPfad() + "\\fragebogen\\" + seminarleitername
					+ "\\" + fragebogenname;

			String fragebogenTemplatePfad = Upload.getInstance().getRepositoryPfad() + "\\"
					+ "template\\template_fragebogen.html";

			// Schreibt den Fragebogen in das Repository

			int umfrageID = 2000;
			String startdatum = "20.2.2019";
			String enddatum = "25.7.2021";
			String auftragsnummer = "123456";

			String datum = "5.8.2020";

			// Beispiel Aufruf für die den HtmlCreator
			//new HtmlCreator(referentenListe, fragebogenTemplatePfad, fragebogenOutputPfad, umfrageID, startdatum,
					enddatum, auftragsnummer, seminarleitername, datum).createHtml();

		} catch (GitAPIException | IOException e) {

			Logger log = Logger.getLogger();
			log.logError(e);

		}

	}

}
*/