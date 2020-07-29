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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/* Ausgabe der Auswertung */

public class ControllerAuswertungAnzeigen {		// was fehlt:  GridPane muss möglicherweise bei zeile>?? bei der Höhe +49 addieren

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

	public void setMainApp (MainApp app){
		mainapp = app;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getName() {
		return ueberschrift.getText();
	}

	public void setName(String name) {
		this.ueberschrift.setText("Ergebnisse für den Fragebogen "+name);
	}

	public void init(MainApp app, FragebogenEigenschaften eigenschaft, List<AzubiAntwort> antwortListe) {

		this.setMainApp(app);
		setName(eigenschaft.fragebogen_name);
		System.out.println("AuswertungAnzeigen: "+antwortListe.size());
		for (AzubiAntwort azubiAntwort : antwortListe) {
			System.out.println("AuswertungAnzeigen->AntwortListe>>> "+azubiAntwort);			// <-- DEBUG
		}

		if (antwortListe.size()==0)
		{
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

		for (int i=0; i<liste.size(); i++) {	// entfernen von leeren Einträgen
			if (liste.get(i) == null || liste.get(i).isBlank())
			{
				liste.remove(i);
			}
		}
		Collections.shuffle(liste);				// zufällige Reihenfolge
		return liste;
	}

	public ArrayList<String> filtereUndMischeList(List<String> eingabe) {

		ArrayList<String> liste = (ArrayList<String>) eingabe;
		for (int i=0; i<liste.size(); i++) {	// entfernen von leeren Einträgen
			if (liste.get(i) == null || liste.get(i).isBlank())
			{
				liste.remove(i);
			}
		}
		Collections.shuffle(liste);				// zufällige Reihenfolge
		return liste;
	}


	public void erzeugeDarstellung() {

		addHeaderToGrid();
		addBewertungVerlaufToGrid();
		addBetreuungToGrid();
		addBemerkungenReferentenAllg();
		addReferentenbewertungenToGrid();
		setze_alle_Fonts();

	}

	public void addHeaderToGrid() {

	    addTextToGrid(grid, "Maßnahme von "+eigenschaft.von_datum+" bis "+eigenschaft.bis_datum, 0, zeile);
	    zeile++;

	    addTextToGrid(grid,"Auftragsnummer: "+eigenschaft.auftrags_nummer,0, zeile );
	    zeile++;

	    addTextToGrid(grid,"Seminarleitung: "+eigenschaft.seminarleiter_name,  0, zeile);
	    zeile++;

	    addTextToGrid(grid, "Datum: "+eigenschaft.ausstellungs_datum, 0, zeile);
	    zeile++;

	    addTextToGrid(grid, "  ", 0, zeile );
	    zeile++;

	}

	public void addBewertungVerlaufToGrid() {
	    addPunkteVerlaufToGrid();
	    addBemerkungenToGrid("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkVerl);
	}

	private void addPunkteVerlaufToGrid() {
	    addTextToGrid(grid,"1. Maßnahmeverlauf:", 0, zeile); 
	    addWertungsheaderToGrid();
	    addPunkteauswertungToGrid(grid , "Wie empfinden die Teilnehmer die Organisation des Seminars?", auswertungMassnahme.pktvertOrg, auswertungMassnahme.durchschnOrg);
	    addPunkteauswertungToGrid(grid, "Wie empfinden die Teilnehmer den Verlauf des Seminars?", auswertungMassnahme.pktvertVerl, auswertungMassnahme.durchschnVerl );
	}

	private void addPunkteauswertungToGrid(GridPane grid, String fragestellung, int [] punkteverteilung, double durchschnitt) {
	    addTextToGrid(grid,fragestellung, 0, zeile); 
	    addTextToGrid(grid,Integer.toString(punkteverteilung[0]), 1, zeile); 
	    addTextToGrid(grid,Integer.toString(punkteverteilung[1]), 2, zeile); 
	    addTextToGrid(grid,Integer.toString(punkteverteilung[2]), 3, zeile); 
	    addTextToGrid(grid,Integer.toString(punkteverteilung[3]), 4, zeile); 
	    addTextToGrid(grid,Integer.toString(punkteverteilung[4]), 5, zeile); 
	    addTextToGrid(grid,zweiStellenNachKomma.format(durchschnitt), 6, zeile); 
	    zeile ++;
	}

	private void addWertungsheaderToGrid() {
	    addTextToGrid(grid,"-2", 1, zeile); 
	    addTextToGrid(grid,"-1", 2, zeile); 
	    addTextToGrid(grid," 0", 3, zeile); 
	    addTextToGrid(grid,"+1", 4, zeile); 
	    addTextToGrid(grid,"+2", 5, zeile); 
	    addTextToGrid(grid,"Ø", 6, zeile); 
	    zeile ++;
	}

	private void addBemerkungenToGrid(String ueberschrift, ArrayList<String> bemerkungenKategorie
		) {
	    Label ueberschrift_bemerkungen = new Label(ueberschrift);
	    grid.add(ueberschrift_bemerkungen, 0, zeile);
	    zeile++;

	    if (bemerkungenKategorie.isEmpty()) {
		Label leer_hinweiß = new Label("(Es gibt keine Bemerkungen.)");
		grid.add(leer_hinweiß, 0, zeile);
		zeile++;
	    } else { 
		int anzBemerkungen = bemerkungenKategorie.size(); 

		for (int i = 0; i < anzBemerkungen; i++) {
		    String bemerkung = bemerkungenKategorie.get(i);
		    addTextToGrid(grid, bemerkung, 0, zeile );
		    zeile++;
		    if (i<anzBemerkungen-1) { //Bemerkungen verschiedener Teilnehmer voneinander trennen
			addTextToGrid(grid, "-------", 0, zeile);
		    }
		}
	    }
	    addTextToGrid(grid,"  ", 0, zeile );
	    zeile ++;
	}

	private void addTextToGrid(GridPane gridPane, String textContent, int col, int row) {	    
	    Text text = new Text(textContent);
	    gridPane.add(text, col, row);
	}

	public void addBetreuungToGrid() {
	    addWertungsheaderToGrid();
	    addPunkteauswertungToGrid(grid, "Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?", auswertungMassnahme.pktvertBetrng, auswertungMassnahme.durchschnBetrng);
	    addBemerkungenToGrid("Bemerkungen dazu: ", auswertungMassnahme.alleBemerkBetrng);
	}

	public void addBemerkungenReferentenAllg() {
		addBemerkungenToGrid("3.Bewertung der Referenten bzw. Referentinnen:", auswertungMassnahme.alleBemerkRefAllg);
	}

	public void addReferentenbewertungenToGrid() {
		addTextToGrid(grid, "4.Auswertung der Referenten:",  0, zeile);
		addTextToGrid(grid, "  ",  0, zeile);
		
		for (AuswertungReferent auswertungReferent : auswertungenReferenten) {
		    addTextToGrid(grid, auswertungReferent.getName(),  0, zeile);
		    addWertungsheaderToGrid();

		    addPunkteauswertungToGrid(grid, "Wie war ihr/sein Unterricht vorbereitet ?", auswertungReferent.stimmenProRadioBtnVorbereitung, auswertungReferent.durchschnittVorbereitung) ;
		    addPunkteauswertungToGrid(grid,  "Wie umfangreich war ihr/sein Fachwissen ? ", auswertungReferent.stimmenProRadioBtnFachwissen, auswertungReferent.durchschnittFachwissen);
		    addPunkteauswertungToGrid(grid, "Wie ging sie/er auf spezielle thematische Probleme ein ? ", auswertungReferent.stimmenProRadioBtnEingehenAufProbleme, auswertungReferent.durchschnittEingehenAufProbleme );
		    addPunkteauswertungToGrid(grid, "Wie verständlich sie/er die Inhalte vermitteln ? ", auswertungReferent.stimmenProRadioBtnInhaltsvermittlung, auswertungReferent.durchschnittInhaltsvermittlung);
		    addPunkteauswertungToGrid(grid, "Wie sagte Ihnen ihr/sein Verhalten gegenüber den Seminarteilnehmern zu ? ", auswertungReferent.stimmenProRadioBtnVerhalten,auswertungReferent.durchschnittVerhalten  );

		    ArrayList<String> bemerkungen = filtereUndMischeList(auswertungReferent.getBemerkungen());

		    addBemerkungenToGrid("Bemerkungen zu: "+auswertungReferent.getName(), bemerkungen);
		}
	}

	public void setze_alle_Fonts() {

	    for (Node node : grid.getChildrenUnmodifiable() ) {
		Text text = (Text)node; 
		text.setFont(new Font(20));
	    }
	}
}
