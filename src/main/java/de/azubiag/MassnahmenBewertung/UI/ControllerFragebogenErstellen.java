package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/* Erstellen des Fragebogens */

public class ControllerFragebogenErstellen {
	
	int anzahl_referenten;
	
	@FXML
	GridPane gridpane;
	
	@FXML 
	TextField name;
	
	@FXML
	Label referent_label;
	
	@FXML
	TextField referent_name;

	@FXML
	public Button preview;
	
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
}