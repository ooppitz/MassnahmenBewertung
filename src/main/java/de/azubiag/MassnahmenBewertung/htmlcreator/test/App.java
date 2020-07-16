/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.azubiag.MassnahmenBewertung.htmlcreator.test;

import de.azubiag.MassnahmenBewertung.UI.MainApp;
import de.azubiag.MassnahmenBewertung.htmlcreator.HtmlCreator;
import de.azubiag.MassnahmenBewertung.upload.Upload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author manuel.unverdorben
 */
public class App {

	public static void main(String[] args) throws IOException {

		ArrayList<String> referentenListe = new ArrayList<>();
		referentenListe.add("Dosterschill");
		referentenListe.add("Steiner");

		String seminarleitername = "testseminarleiter";
		String fragebogenname = "testfragebogenTest2.html";

		String fragebogenOutputPfad;
		try {
			fragebogenOutputPfad = Upload.getInstance().getRepositoryPfad() + "\\fragebogen\\" + seminarleitername + "\\"
					+ fragebogenname;
			
			String fragebogenTemplatePfad = Upload.getInstance().getRepositoryPfad() + "\\"
					+ "template\\template_fragebogen.html";

			// Schreibt den Fragebogen in das Repository
			new HtmlCreator(referentenListe, fragebogenTemplatePfad, fragebogenOutputPfad).createHtml();

		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
