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

	public HtmlCreator(ArrayList<String> referentenList, String templateFile, String fragebogenOutputFile) {
		this.refListe = referentenList;
		this.saveFile = fragebogenOutputFile;
		this.inputFile = templateFile;

	}

	public void createHtml() throws IOException {
		File file = new File(inputFile);
		doc = Jsoup.parse(file, "UTF-8");
		int prefix = 0;

		Element bodyElement = doc.getElementsByTag("body").first();

		konfiguriereMassnahmenBox();
		
		int anzahlReferenten = refListe.size();
		bodyElement.attr("anzahlreferenten", Integer.toString(anzahlReferenten));
	
		for (String ref : refListe) {
			bodyElement.attr("referent" + prefix, ref);
			elementListe.add(makeRefBox(ref, prefix));
			prefix++;
		}
		addElementsToHtml();
		saveHtml(saveFile);

		System.out.println("doc = " + doc.outerHtml());
	}

	/* Erzeugen der Paragraphen für die Warnmeldungen 
	 */
	public void konfiguriereMassnahmenBox() throws IOException {
		
	    Element elementFieldset0 = doc.getElementById("massnahme_fs0");
		appendWarningIntoTableRow(elementFieldset0, "[name=a_r0]", "a_r0");
		appendWarningIntoTableRow(elementFieldset0, "[name=a_r1]", "a_r1");
   
		Element elementFieldset1 = doc.getElementById("massnahme_fs1");
		appendWarningIntoTableRow(elementFieldset1, "[name=b_r0]", "b_r0");

	}

	public Element makeRefBox(String referent, int prefix) throws IOException {

		// hole das Fieldset von den Refereten
		Element elementFieldset = doc.getElementsByAttributeValue("id", "referent").first().clone();
		// Ändere Name
		Element refName = elementFieldset.getElementById("name");
		refName.text("Name: " + referent);

		// Passe die RadioButtons an
		String buttonSelector;
		for (int dozentenFragenIndex = 0; dozentenFragenIndex <= 4; dozentenFragenIndex++) {
			buttonSelector = "[name=r"+ Integer.toString(dozentenFragenIndex) + "]";

			String radioButtonName = Integer.toString(prefix) + "_r" + Integer.toString(dozentenFragenIndex);
			appendWarningIntoTableRow(elementFieldset, buttonSelector, radioButtonName);

			for (int radioButtonIndex = 0; radioButtonIndex <= 4; radioButtonIndex++) {
				Element buttonId = elementFieldset.select(buttonSelector).first();
				buttonId.attr("name", radioButtonName);
			}

			Element textarea = elementFieldset.select("textarea").first();
			textarea.attr("id", Integer.toString(prefix) + "_text");

		}
		return elementFieldset;

	}

	public void appendWarningIntoTableRow(Element e, String buttonSelector, String radioButtonName) {
		Element tabeleRowElement = e.select(buttonSelector).first().parent().parent();
		Element paragraph = createHiddenWarningParagraph(radioButtonName);
		String tempText = "<tr><td>" + paragraph.outerHtml() + "</td></tr>";
		tabeleRowElement.before(tempText);

	}

	public Element createHiddenWarningParagraph(String radioButtonName) {
		Element warningTag = new Element("p");
		warningTag.attr("hidden", "true");
		warningTag.attr("class", "warnung");
		warningTag.attr("id", radioButtonName + "_warnung");
		warningTag.text("*********** Bitte Ausfüllen! *****************");

		return warningTag;

	}

	public void addElementsToHtml() {
		Element element = doc.getElementsByAttributeValue("id", "referent").first();
		element.remove();
		
		Element elementOKButton = doc.getElementById("OKButton");
		
		for (Element e : elementListe) {
			elementOKButton.before(e);
		}
	}

	public void saveHtml(String fileName) throws IOException {
		File file = new File(fileName);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(doc.outerHtml());
		}
	}

}
