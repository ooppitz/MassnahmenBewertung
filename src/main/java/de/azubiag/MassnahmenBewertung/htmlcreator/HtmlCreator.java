/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.azubiag.MassnahmenBewertung.htmlcreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author manuel.unverdorben
 */
public class HtmlCreator {

	ArrayList<String> refListe;
	ArrayList<Element> elementListe = new ArrayList<>();
	String inputFile;
	String saveFile;
	Document doc;

	public HtmlCreator(ArrayList<String> refListe, String inputFile, String saveFile) {
		this.refListe = refListe;
		this.saveFile = saveFile;
		this.inputFile = inputFile;

	}

	public void createHtml() throws IOException {
		File file = new File(inputFile);
		doc = Jsoup.parse(file, "UTF-8");
		int prefix = 0;
		for (String ref : refListe) {
			elementListe.add(makeRefBox(ref, prefix));
			prefix++;
		}
		addElementsToHtml();
		saveHtml(saveFile);

	}

	public Element makeRefBox(String referent, int iterator) throws IOException {

		// hole das Fieldset von den Refereten
		Element element = doc.getElementsByAttributeValue("id", "referent").first().clone();
		// Ändere Name
		Element refName = element.getElementById("name");
		refName.text("Name: " + referent);

		// Passe die RadioButtons an
		String buttonSelector;
		for (int i = 1; i <= 5; i++) {
			buttonSelector = "[name=r";
			buttonSelector += Integer.toString(i) + "]";
			for (int j = 1; j <= 5; j++) {

				Element buttonId = element.select(buttonSelector).first();

				buttonId.attr("name", Integer.toString(iterator) + "_r" + Integer.toString(i));
			}

			Element textarea = element.select("textarea").first();
			textarea.attr("name", Integer.toString(iterator) + "_text");

		}
		return element;

	}

	public void addElementsToHtml() {
		Element element = doc.getElementsByAttributeValue("id", "referent").first();
		element.remove();
		for (Element e : elementListe) {
			doc.select("body").append(e.outerHtml());
		}
	}

	public void saveHtml(String fileName) throws IOException {
		File file = new File(fileName);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(doc.outerHtml());
		}
	}

}
