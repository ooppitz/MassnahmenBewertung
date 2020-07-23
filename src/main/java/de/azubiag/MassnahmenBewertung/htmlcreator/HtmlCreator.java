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
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author manuel.unverdorben
 */
public class HtmlCreator {

	ArrayList<String> refListe;
	
	String inputFile;
	String saveFile;
	Document doc;
	int verifyID;

	@Deprecated
	public HtmlCreator(ArrayList<String> referenten, String templatePath, String outputPath) {
		this(referenten, templatePath, outputPath, 0);
	}
	
	public HtmlCreator(ArrayList<String> referentenList, String templateFile, String fragebogenOutputFile, int verifyID) {
		this.refListe = referentenList;
		this.saveFile = fragebogenOutputFile;
		this.inputFile = templateFile;
		this.verifyID = verifyID;
	}

	public void createHtml() throws IOException {
		
		File file = new File(inputFile);
		doc = Jsoup.parse(file, "UTF-8");
		int prefix = 0;

		Element bodyElement = doc.getElementsByTag("body").first();

		// Header (A)
		changeHeaderinformation(bodyElement); // Benedikt
		
		// Massnahme (B)
		konfiguriereMassnahmenBox();
		
		// Referenten (C, D, E, ... )
		int anzahlReferenten = refListe.size();
		bodyElement.attr("anzahlreferenten", Integer.toString(anzahlReferenten));
		bodyElement.attr("verifyID", String.valueOf(verifyID));
	
		ArrayList<Element> elementListe = new ArrayList<>();
		for (String ref : refListe) {
			bodyElement.attr("referent" + prefix, ref);
			elementListe.add(makeReferentenBox(ref, prefix));
			prefix++;
		}
		addElementsToHtml(elementListe);
		
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
	
	public void changeHeaderinformation(Element e) {
		
		String startdatum = "31.1.2008";
		String enddatum = "31.12.2009";
		String auftragsnummer = "F-27-99";
		String seminarleitername = "Frau Moll";
		String datum = "23.7.2020 a.d.";
		
		Element e_startdatum = e.getElementById("startdatum");
		e_startdatum.text(startdatum);
		Element e_enddatum = e.getElementById("enddatum");
		e_enddatum.text(enddatum);
//		Element e_auftragsnummer = e.getElementById("auftragsnummer");   Feld existiert noch nicht im template
//		e_auftragsnummer.text(auftragsnummer);
		Element e_seminarleitername = e.getElementById("seminarleitername");
		e_seminarleitername.text(seminarleitername);
		Element e_datum = e.getElementById("datum");
		e_datum.text(datum);
		
	}

	public Element makeReferentenBox(String referentName, int radioButtonNamePrefix) throws IOException {

		// hole das Fieldset von den Refereten
		Element elementFieldset = doc.getElementsByAttributeValue("id", "referent").first().clone();
		// Ändere Name
		Element refName = elementFieldset.getElementById("name");
		refName.text("Name: " + referentName);

		// Passe die RadioButtons an
		
		for (int dozentenFragenIndex = 0; dozentenFragenIndex <= 4; dozentenFragenIndex++) {
			String buttonSelector = "[name=r"+ Integer.toString(dozentenFragenIndex) + "]";
			String radioButtonName = Integer.toString(radioButtonNamePrefix) + "_r" + Integer.toString(dozentenFragenIndex);
			appendWarningIntoTableRow(elementFieldset, buttonSelector, radioButtonName);

			for (int radioButtonIndex = 0; radioButtonIndex <= 4; radioButtonIndex++) {
				Element buttonId = elementFieldset.select(buttonSelector).first();
				buttonId.attr("name", radioButtonName);
			}

			Element textarea = elementFieldset.select("textarea").first();
			textarea.attr("id", Integer.toString(radioButtonNamePrefix) + "_text");

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

	/** Fügt die fertig konfigurierten HTML-Blöcke für die Referenten hinzu. 
	 * @param elementListe
	 */
	public void addElementsToHtml(List<Element> elementListe) {
		
		// Entfernt das nicht mehr benötigte Original aus dem Dokument
		Element element = doc.getElementsByAttributeValue("id", "referent").first();
		element.remove();
		
		Element elementAtBottomOfPage = doc.getElementById("OKButton");
		
		for (Element e : elementListe) {
			elementAtBottomOfPage.before(e);
		}
	}

	public void saveHtml(String fileName) throws IOException {
		File file = new File(fileName);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			bw.write(doc.outerHtml());
		}
	}

}
