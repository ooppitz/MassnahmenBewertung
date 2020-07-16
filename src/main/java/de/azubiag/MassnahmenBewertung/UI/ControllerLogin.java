package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;


/* Fenster zum Einloggen 
 */
public class ControllerLogin {
	
	Tab tab;
	
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

				String clean_username = username.getText();
				// schauen, ob das Feld nicht leer ist
				// Auswahlliste von Namen davor anzeigen
				System.out.println(System.getProperty("java.io.tmpdir")+"gfigithubaccess.github.io\\fragebogen\\"+clean_username);
				String path = System.getProperty("java.io.tmpdir")+"gfigithubaccess.github.io\\fragebogen\\"+clean_username;
//				Path weg = Paths.get();
				File test_file = new File(path);
				System.out.println("Exists:\t"+test_file.exists());
				System.out.println("Is Directory:\t"+test_file.isDirectory());
				
				if(!test_file.isDirectory() )
				{
					boolean success = test_file.mkdir();
					if (!success)
					{
						System.out.println("Directory could not be created!!!");
						// Alert
						return;
					}
					else
					{
						System.out.println("Directory created!");
					}
				}
				
				mainapp.showTabPane();
				
				MainApp.setUserName(username.getText());
			}
		});
	}
}
