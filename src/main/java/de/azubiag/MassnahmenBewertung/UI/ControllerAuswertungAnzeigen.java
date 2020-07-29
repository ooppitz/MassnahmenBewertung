package de.azubiag.MassnahmenBewertung.UI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/* Ausgabe der Auswertung */

/**
 * @author Louisa
 *
 */
/**
 * @author Louisa
 *
 */
/**
 * @author Louisa
 *
 */
/**
 * @author Louisa
 *
 */
public class ControllerAuswertungAnzeigen { // was fehlt: Pane muss möglicherweise bei zeile>?? bei der Höhe +49
					    // addieren

    List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
    List<AuswertungReferent> auswertungenReferenten;
    AuswertungMassnahme auswertungMassnahme;
    FragebogenEigenschaften eigenschaft;
    private static DecimalFormat zweiStellenNachKomma = new DecimalFormat("#.##");

    int zeile;

    Tab tab;

    @FXML
    public Label ueberschrift;

    @FXML
    public Button save;

    @FXML
    public Button delete;

    @FXML
    GridPane grid;

    private MainApp mainapp;

    public void setEigenschaft(FragebogenEigenschaften eigenschaft) {
	this.eigenschaft = eigenschaft;
    }

    public void setMainApp(MainApp app) {
	mainapp = app;
    }

    public void setTab(Tab tab) {
	this.tab = tab;
    }

    public String getName() {
	return ueberschrift.getText();
    }

    public void setName(String name) {
	this.ueberschrift.setText("Ergebnisse für den Fragebogen " + name);
    }

    public void init(MainApp app, FragebogenEigenschaften eigenschaft, List<AzubiAntwort> antwortListe) {

	this.setMainApp(app);
	setName(eigenschaft.fragebogen_name);
	System.out.println("AuswertungAnzeigen: " + antwortListe.size());
	for (AzubiAntwort azubiAntwort : antwortListe) {
	    System.out.println("AuswertungAnzeigen->AntwortListe>>> " + azubiAntwort); // <-- DEBUG
	}

	if (antwortListe.size() == 0) {
	    Logger log = Logger.getLogger();
	    log.logError("In ControllerAuswertungAnzeigen hat AntwortListe die Länge 0 !!!");
	    Label warning = new Label("     In ControllerAuswertungAnzeigen hat AntwortListe die Länge 0 !!!");
	    grid.add(warning, 0, zeile);
	    zeile = 2;
	    return;
	}

	final List<BewertungMassnahme> bewertungListe = new ArrayList<BewertungMassnahme>();

	for (AzubiAntwort azubiAntwort : antwortListe) {
	    bewertungListe.add(azubiAntwort.massnahme);
	}

	auswertungMassnahme = new AuswertungMassnahme(bewertungListe);
	auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antwortListe);

	auswertungMassnahme.alleBemerkBetrng = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkBetrng);
	auswertungMassnahme.alleBemerkRefAllg = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkRefAllg);
	auswertungMassnahme.alleBemerkVerl = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkVerl);

	setName(eigenschaft.fragebogen_name);

	zeile = 1;

    }

    public ArrayList<String> filtereUndMischeArrayList(ArrayList<String> liste) {

	for (int i = 0; i < liste.size(); i++) { // entfernen von leeren Einträgen
	    if (liste.get(i) == null || liste.get(i).isBlank()) {
		liste.remove(i);
	    }
	}
	Collections.shuffle(liste); // zufällige Reihenfolge
	return liste;
    }

    public ArrayList<String> filtereUndMischeList(List<String> eingabe) {

	ArrayList<String> liste = (ArrayList<String>) eingabe;
	for (int i = 0; i < liste.size(); i++) { // entfernen von leeren Einträgen
	    if (liste.get(i) == null || liste.get(i).isBlank()) {
		liste.remove(i);
	    }
	}
	Collections.shuffle(liste); // zufällige Reihenfolge
	return liste;
    }

    public void erzeugeDarstellung() {

	addHeaderTo();
	addBewertungVerlaufTo();
	addBetreuungTo();
	addBemerkungenReferentenAllg();
	addReferentenbewertungenTo();
	setze_alle_Fonts();

    }

    public void addHeaderTo() {

	addTextTo( "Maßnahme von " + eigenschaft.von_datum + " bis " + eigenschaft.bis_datum, 0, true);
	addTextTo( "Auftragsnummer: " + eigenschaft.auftrags_nummer, 0, true);
	addTextTo( "Seminarleitung: " + eigenschaft.seminarleiter_name, 0, true);
	addTextTo( "Datum: " + eigenschaft.ausstellungs_datum, 0, true);
	addTextTo( "  ", 0, true);
    }

    public void addBewertungVerlaufTo() {
	addPunkteVerlaufTo();
	addBemerkungenTo("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkVerl);
    }

    private void addPunkteVerlaufTo() {
	addTextTo( "1. Maßnahmeverlauf:", 0, false);
	addWertungsheaderTo();
	addPunkteauswertungTo("Wie empfinden die Teilnehmer die Organisation des Seminars?",
		auswertungMassnahme.pktvertOrg, auswertungMassnahme.durchschnOrg);
	addPunkteauswertungTo( "Wie empfinden die Teilnehmer den Verlauf des Seminars?",
		auswertungMassnahme.pktvertVerl, auswertungMassnahme.durchschnVerl);
    }

    private void addPunkteauswertungTo( String fragestellung, int[] punkteverteilung,
	    double durchschnitt) {
	addTextTo( fragestellung, 0, false);
	addTextTo( Integer.toString(punkteverteilung[0]), 1, false);
	addTextTo( Integer.toString(punkteverteilung[1]), 2, false);
	addTextTo( Integer.toString(punkteverteilung[2]), 3, false);
	addTextTo( Integer.toString(punkteverteilung[3]), 4, false);
	addTextTo( Integer.toString(punkteverteilung[4]), 5, false);
	addTextTo( zweiStellenNachKomma.format(durchschnitt), 6, false);
	zeile++;
    }

    private void addWertungsheaderTo() {
	addTextTo( "-2", 1, false);
	addTextTo( "-1", 2, false);
	addTextTo( " 0", 3, false);
	addTextTo( "+1", 4, false);
	addTextTo( "+2", 5, false);
	addTextTo( "Ø", 6, false);
	zeile++;
    }

    private void addBemerkungenTo(String ueberschrift, ArrayList<String> bemerkungenKategorie) {
	addTextTo( ueberschrift, 0, true);

	if (bemerkungenKategorie.isEmpty()) {
	    addTextTo( "(Es gibt keine Bemerkungen.)", 0, true);
	} else {
	    int anzBemerkungen = bemerkungenKategorie.size();

	    for (int i = 0; i < anzBemerkungen; i++) {
		String bemerkung = bemerkungenKategorie.get(i);
		addTextTo(bemerkung, 0, true);
		if (i < anzBemerkungen - 1) { // Bemerkungen verschiedener Teilnehmer voneinander trennen
		    addTextTo( "-------", 0, true);
		}
	    }
	}
	addTextTo( "  ", 0, true);
    }

    private void addTextTo(String textContent, int col, boolean linefeed) {
	Text text = new Text(textContent);
	grid.add(text, col, zeile);
	if (linefeed) {
	    zeile++;
	}
    }

    public void addBetreuungTo() {
	addTextTo( "2. Maßnahmenbetreuung", 0, false);

	addWertungsheaderTo();
	addPunkteauswertungTo( "Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?",
		auswertungMassnahme.pktvertBetrng, auswertungMassnahme.durchschnBetrng);
	addBemerkungenTo("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkBetrng);
    }

    public void addBemerkungenReferentenAllg() {
	addBemerkungenTo("3.Bewertung der Referenten bzw. Referentinnen:", auswertungMassnahme.alleBemerkRefAllg);
    }

    public void addReferentenbewertungenTo() {
	addTextTo( "4.Auswertung der Referenten:", 0, true);
	addTextTo("  ", 0, true);

	for (AuswertungReferent auswertungReferent : auswertungenReferenten) {
	    addTextTo(auswertungReferent.getName(), 0, false);
	    addWertungsheaderTo();

	    addPunkteauswertungTo( "Wie war ihr/sein Unterricht vorbereitet ?",
		    auswertungReferent.stimmenProRadioBtnVorbereitung, auswertungReferent.durchschnittVorbereitung);
	    addPunkteauswertungTo( "Wie umfangreich war ihr/sein Fachwissen ? ",
		    auswertungReferent.stimmenProRadioBtnFachwissen, auswertungReferent.durchschnittFachwissen);
	    addPunkteauswertungTo( "Wie ging sie/er auf spezielle thematische Probleme ein ? ",
		    auswertungReferent.stimmenProRadioBtnEingehenAufProbleme,
		    auswertungReferent.durchschnittEingehenAufProbleme);
	    addPunkteauswertungTo( "Wie verständlich sie/er die Inhalte vermitteln ? ",
		    auswertungReferent.stimmenProRadioBtnInhaltsvermittlung,
		    auswertungReferent.durchschnittInhaltsvermittlung);
	    addPunkteauswertungTo( "Wie sagte Ihnen ihr/sein Verhalten gegenüber den Seminarteilnehmern zu ? ",
		    auswertungReferent.stimmenProRadioBtnVerhalten, auswertungReferent.durchschnittVerhalten);

	    ArrayList<String> bemerkungen = filtereUndMischeList(auswertungReferent.getBemerkungen());

	    addBemerkungenTo("Bemerkungen zu: " + auswertungReferent.getName(), bemerkungen);
	}
    }

    public void setze_alle_Fonts() {

	for (Node node : grid.getChildrenUnmodifiable()) {
	    Text text = (Text) node;
	    text.setFont(new Font(20));
	}
    }
}
