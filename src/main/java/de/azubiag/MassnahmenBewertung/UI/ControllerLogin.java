package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


/* Fenster zum Einloggen 
 */
public class ControllerLogin {
	
	@FXML
	public Button next;
	
	@FXML
	public TextField username;
	
	private MainApp mainapp;

	public MainApp getMainapp() {
		return mainapp;
	}

	public void setMainapp(MainApp mainapp) {
		this.mainapp = mainapp;
	}
	
	
}
