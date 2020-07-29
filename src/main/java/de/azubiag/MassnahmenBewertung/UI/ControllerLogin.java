package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.tools.Tools;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


/* Fenster zum Einloggen 
 */
public class ControllerLogin {

	Tab tab;

	@FXML
	public Button next;

	@FXML
	public TextField username;

	private MainApp mainapp;

	ArrayList<String> alle_Nutzer;
	ArrayList<String> zutreffende_Nutzer;

	public ArrayList<String> getZutreffende_Nutzer() {
		if (zutreffende_Nutzer == null)
		{
			zutreffende_Nutzer = new ArrayList<String>();
		}
		return zutreffende_Nutzer;
	}

	public ArrayList<String> getAlle_Nutzer() {
		if (alle_Nutzer == null)
		{
			try {
				String pfad = Upload.getInstance().getRepositoryPfad();
				pfad += "//Fragebogen";
				File fragebogen_ordner = new File(pfad);
				String[] array = fragebogen_ordner.list();
				alle_Nutzer = new ArrayList<String>();
				for (String string : array) {
					alle_Nutzer.add(string);
				}
			} catch (GitAPIException | IOException e) {
				e.printStackTrace();
			}
		}
		return alle_Nutzer;
	}

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

				boolean nutzer_vorhanden = isNutzerVorhanden(clean_username);

				if(nutzer_vorhanden)
				{
					MainApp.setUserName(username.getText());
					mainapp.showTabPane();
				}
				else
				{
					boolean account_erstellt = neuen_Account_erstellen(clean_username);
					if (account_erstellt)
					{
						MainApp.setUserName(username.getText());
						mainapp.showTabPane();
					}
				}
			}

		});
	}

	public void addListener_TextFieldSuggestion() {
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isBlank())
			{
				String spaces_vorne_entfernt = newValue.replaceFirst("^\\s*", "");
				String clean_username = Tools.normalisiereString(spaces_vorne_entfernt).toLowerCase();
				zutreffende_Nutzer.clear();
				for (String string : getAlle_Nutzer()) {
					if(string.matches(clean_username+"\\S*")  || string.matches("\\S*_"+clean_username+"\\S*"))
					{
						zutreffende_Nutzer.add(string);
						System.out.println(string);
					}
				}
				System.out.println();
			}
		});
	}

	public File getPath(String clean_username)
	{
		try {
			String path_string = Upload.getInstance().getSeminarleiterDirectory(clean_username);
			File test_file = new File(path_string);
			return test_file;
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPath_String(String clean_username)
	{
		try {
			String path_string = Upload.getInstance().getSeminarleiterDirectory(clean_username);
			return path_string;
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isNutzerVorhanden(String clean_username) {

		String path = getPath_String(clean_username);
		File test_file = getPath(clean_username);
		System.out.println("Exists:\t"+test_file.exists());
		System.out.println("Is Directory:\t"+test_file.isDirectory());

		if(!test_file.isDirectory())
		{
			if(test_file.exists())
			{
				AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Eine Datei mit diesem Namen existiert leider schon!", "Eine Datei mit diesem Namen existiert leider schon!");
				Logger log = Logger.getLogger();
				log.logError("Eine Datei mit diesem Namen existiert leider schon!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
			}
			return false;
		}
		return true;	// alles OK!
	}

	public boolean neuen_Account_erstellen(String clean_username) {

		boolean neuer_Account = AlertMethoden.entscheidungViaDialogAbfragen("Neuen Account erstellen?", "Es wurde ein neuer Nutzername eingegeben.\nWollen Sie einen neuen Account erstellen?");
		if (neuer_Account)
		{
			String path_string = getPath_String(clean_username);
			File test_file = getPath(clean_username);
			boolean success = test_file.mkdir();
			if (!success)
			{
				System.out.println("Directory could not be created!!!");
				Logger log = Logger.getLogger();
				log.logError("Dieser Benutzername kann nicht verwendet werden!"+"\tPath= "+path_string+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
				AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Dieser Benutzername kann nicht verwendet werden!", "Dieser Benutzername kann nicht verwendet werden!");
				return false;
			}
			else
			{
				Logger log = Logger.getLogger();
				log.logInfo("Ordner erstellt!"+"\tPath= "+path_string+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory());
				return true;
			}
		}
		return false;
	}
}
