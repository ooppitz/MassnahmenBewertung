package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

/* Ausgabe der Auswertung */

public class ControllerAuswertungAnzeigen {
	
	Tab tab;
	
	@FXML
	public Label name;
	
	@FXML
	public Button save;
	
	@FXML
	public Button delete;
	
	private MainApp mainapp;
	
	public void setMainApp (MainApp app){
		mainapp = app;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getName() {
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText("Ergebnisse für den Fragebogen "+name);
	}
}
