package de.azubiag.MassnahmenBewertung.UI;

import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

/* Ausgabe der Auswertung */

public class ControllerAuswertungAnzeigen {
	
	public List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
	
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
	
	public void init() {
		
		zeile = 0;
		verlauf();
		betreuung();
		bewertung();
		bemerkungen();
		
	}
	
	public void verlauf() {
		
		Label ueberschrift_verlauf = new Label("1. Maßnahmeverlauf:");
		Label u__2 = new Label("-2");
		Label u__1 = new Label("-1");
		Label u_0 = new Label(" 0");
		Label u_1 = new Label("+1");
		Label u_2 = new Label("+2");
		Label u_d = new Label("Ø");
		
		grid.add(ueberschrift_verlauf, 0, zeile);
		grid.add(u__2, 1, zeile);
		grid.add(u__1, 2, zeile);
		grid.add(u_0, 3, zeile);
		grid.add(u_1, 4, zeile);
		grid.add(u_2, 5, zeile);
		grid.add(u_d, 6, zeile);
		zeile ++;
		
		Label organisation_frage = new Label("Wie empfinden die Teilnehmer die Organisation des Seminars?");
		Label o__2 = new Label("");
		Label o__1 = new Label("");
		Label o_0 = new Label("");
		Label o_1 = new Label("");
		Label o_2 = new Label("");
		Label o_d = new Label("");
		
		grid.add(organisation_frage, 0, zeile);
		grid.add(o__2, 1, zeile);
		grid.add(o__1, 2, zeile);
		grid.add(o_0, 3, zeile);
		grid.add(o_1, 4, zeile);
		grid.add(o_2, 5, zeile);
		grid.add(o_d, 6, zeile);
		zeile ++;
		
		Label verlauf_frage = new Label("Wie empfinden die Teilnehmer die Organisation des Seminars?");
		Label v__2 = new Label("");
		Label v__1 = new Label("");
		Label v_0 = new Label("");
		Label v_1 = new Label("");
		Label v_2 = new Label("");
		Label v_d = new Label("");
		
		grid.add(verlauf_frage, 0, zeile);
		grid.add(v__2, 1, zeile);
		grid.add(v__1, 2, zeile);
		grid.add(v_0, 3, zeile);
		grid.add(v_1, 4, zeile);
		grid.add(v_2, 5, zeile);
		grid.add(v_d, 6, zeile);
		zeile ++;
		
	}
	
	public void betreuung() {
		
		Label ueberschrift_betreuung = new Label("2. Maßnahmebetreuung:");
		Label u__2 = new Label("-2");
		Label u__1 = new Label("-1");
		Label u_0 = new Label(" 0");
		Label u_1 = new Label("+1");
		Label u_2 = new Label("+2");
		Label u_d = new Label("Ø");
		
		grid.add(ueberschrift_betreuung, 0, zeile);
		grid.add(u__2, 1, zeile);
		grid.add(u__1, 2, zeile);
		grid.add(u_0, 3, zeile);
		grid.add(u_1, 4, zeile);
		grid.add(u_2, 5, zeile);
		grid.add(u_d, 6, zeile);
		zeile ++;
		
		Label betreuung_frage = new Label("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?");
		Label b__2 = new Label("");
		Label b__1 = new Label("");
		Label b_0 = new Label("");
		Label b_1 = new Label("");
		Label b_2 = new Label("");
		Label b_d = new Label("");
		
		grid.add(betreuung_frage, 0, zeile);
		grid.add(b__2, 1, zeile);
		grid.add(b__1, 2, zeile);
		grid.add(b_0, 3, zeile);
		grid.add(b_1, 4, zeile);
		grid.add(b_2, 5, zeile);
		grid.add(b_d, 6, zeile);
		zeile ++;
	}
	
	public void bewertung() {		// was fließt hier ein? möglicherweise gar nicht anzeigen
		
	}
	
	public void bemerkungen() {		// wird wohl nicht gebraucht
		
	}
}
