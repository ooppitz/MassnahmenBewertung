package de.azubiag.MassnahmenBewertung.UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	
	public void addUsernameNextToButton() {
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// schauen, ob das Feld nicht leer ist
				// Auswahlliste von Namen davor anzeigen
				mainapp.showTabPane();
				
				MainApp.setUserName(username.getText());
			}
		});
	}
}
