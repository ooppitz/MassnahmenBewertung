package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Zustand3Controller {
	
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

	public String getName() {
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText("Ergebnisse für den Fragebogen "+name);
	}
}
