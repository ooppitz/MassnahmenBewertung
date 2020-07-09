package de.azubiag.MassnahmenBewertung.UI;

import de.muc.gfi.referentenbewertung.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Zustand0Controller {
	
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
