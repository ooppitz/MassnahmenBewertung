package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
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
			Logger logger = Logger.getLogger();
			logger.logError(e2);
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
				AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Eine Datei mit diesem Namen existiert leider schon!", "Eine Datei mit diesem Namen existiert leider schon!");
				Logger log = Logger.getLogger();
				log.logError("Eine Datei mit diesem Namen existiert leider schon!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
				return false;
			}
			else
			{
				boolean success = test_file.mkdir();
				if (!success)
				{
					System.out.println("Directory could not be created!!!");
					Logger log = Logger.getLogger();
					log.logError("Dieser Benutzername kann nicht verwendet werden!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Dieser Benutzername kann nicht verwendet werden!", "Dieser Benutzername kann nicht verwendet werden!");
					return false;
				}
				else
				{
					Logger log = Logger.getLogger();
					log.logInfo("Ordner erstellt!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
					return true;
				}
			}

		}
		return true;
	}
}
