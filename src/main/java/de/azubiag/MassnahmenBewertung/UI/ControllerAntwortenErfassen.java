package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

/* Eingeben der Antworten */

public class ControllerAntwortenErfassen {
	
	
	public List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
	
	int anzahl_antworten;
	
	@FXML
	Label antwort_name;
	
	@FXML
	Label antwort_text;
	
	@FXML
	GridPane gridpane;
	
	@FXML
	private Label name;
	
	@FXML
	private Label maintext;
	
	@FXML
	public Button add;
	
	@FXML
	public Button next;
	
	@FXML
	public Button delete;
	
	private MainApp mainapp;
	
	public void setMainApp (MainApp app){
		mainapp = app;
	}

	public String getName() {
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText(name);
	}
	
	public String getMaintext() {
		return maintext.getText();
	}

	public void setMaintext(String maintext) {
		this.maintext.setText("Eingeben der Ergebnisse für Fragebogen "+maintext);
	}
}
