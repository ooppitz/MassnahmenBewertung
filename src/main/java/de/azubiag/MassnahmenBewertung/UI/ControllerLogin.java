package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.tools.Tools;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;


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

				String clean_username = Tools.normalisiereString(username.getText());
				// schauen, ob das Feld nicht leer ist
				// Auswahlliste von Namen davor anzeigen
				boolean alles_ok = erstelleNeuenOrdnerFallsNotwendig(clean_username);

				if(alles_ok)
				{
					MainApp.setUserName(username.getText());
					mainapp.showTabPane();
				}
			}

		});
	}

	private boolean erstelleNeuenOrdnerFallsNotwendig(String clean_username) {

		String path;
		try {
			path = Upload.getInstance().getSeminarleiterDirectory(clean_username);
		} catch (Exception e2) {
			return false; // besser handlen?
		}
		System.out.println(path);

		File test_file = new File(path);
		System.out.println("Exists:\t"+test_file.exists());
		System.out.println("Is Directory:\t"+test_file.isDirectory());

		if(!test_file.isDirectory() )
		{
			if(test_file.exists())
			{
				Alert error = new Alert(AlertType.ERROR);
				Logger log = Logger.getLogger();
				log.log("Eine Datei mit diesem Namen existiert leider schon!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
				error.setTitle("Eine Datei mit diesem Namen existiert leider schon!");
				error.setHeaderText("Eine Datei mit diesem Namen existiert leider schon!");
				ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
				error.getButtonTypes().setAll(end);
				error.show();
				return false;
			}
			else
			{
				boolean success = test_file.mkdir();
				if (!success)
				{
					System.out.println("Directory could not be created!!!");
					Logger log = Logger.getLogger();
					log.log("Dieser Benutzername kann nicht verwendet werden!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
					// Alert
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Dieser Benutzername kann nicht verwendet werden!");
					error.setHeaderText("Dieser Benutzername kann nicht verwendet werden!");
					ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
					error.getButtonTypes().setAll(end);
					error.show();
					return false;
				}
				else
				{
					System.out.println("Directory created!");
					Logger log = Logger.getLogger();
					log.log("Ordner erstellt!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
					return true;
				}
			}

		}
		return true;
	}
}
