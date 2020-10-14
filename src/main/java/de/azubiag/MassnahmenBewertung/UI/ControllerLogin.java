package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.tools.Tools;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;


/* Fenster zum Einloggen 
 */
public class ControllerLogin {

	Tab tab;

	@FXML
	public Button next;

	@FXML
	public GridPane grid;

	public TextField_mitVorschlägen username;

	private MainApp mainapp;

	ArrayList<String> alle_Nutzer;
	ArrayList<String> zutreffende_Nutzer;
	private final int LIMIT = 100;
	private Dictionary<String,String> nutzernamenUndOrdner;
	static String nutzerfilename = "nutzer.ser";

	public ArrayList<String> getZutreffende_Nutzer() {
		if (zutreffende_Nutzer == null)
		{
			zutreffende_Nutzer = new ArrayList<String>();
		}
		return zutreffende_Nutzer;
	}

	public ArrayList<String> getAlle_Nutzer() {
		ArrayList<String>  tempArrayList= new ArrayList<>();
		if (alle_Nutzer == null)
		{
			alle_Nutzer = Collections.list(nutzernamenUndOrdner.keys());		
		}
		for (String s: alle_Nutzer) {
			if (s.charAt(0) > 31 && s.charAt(0) <123 ) {
				tempArrayList.add(s);
				}
		}
		alle_Nutzer = tempArrayList;
		return alle_Nutzer;
	}

	public MainApp getMainapp() {
		return mainapp;
	}

	public void setMainapp(MainApp mainapp) {
		this.mainapp = mainapp;
	}

	public void init() {
		username = new TextField_mitVorschlägen();
		grid.add(username, 0, 2);
		username.setFont(next.getFont());
		GridPane.setMargin((Node)username, new Insets(5, 5, 5, 5));
		nutzernamenUndOrdner = laden();
		System.out.println("nutzernamenUndOrdner-> "+nutzernamenUndOrdner);	// DEBUG
		
		getAlle_Nutzer();
		getZutreffende_Nutzer();
	}

	public void speichern() {

		Logger log = Logger.getLogger();
		log.logInfo("nutzer.ser wird gespeichert");

		try {
			String ordner = Upload.getInstance().getRepositoryPfad();
			FileOutputStream fos = new FileOutputStream(ordner + nutzerfilename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(nutzernamenUndOrdner);
			oos.close();
			fos.close();

		} catch (IOException | GitAPIException e) {
			Logger l = new Logger();
			l.logError(e);
		}
	}

	public Hashtable<String,String> laden() {

		if(existiert_datei())
		{
			Logger log = Logger.getLogger();
			log.logInfo("nutzer.ser wird geladen");
			Hashtable<String,String> hashtable = null;
			try {
				String ordner = Upload.getInstance().getRepositoryPfad();
				FileInputStream fis = new FileInputStream(ordner + nutzerfilename);
				ObjectInputStream ois = new ObjectInputStream(fis);
				hashtable = (Hashtable<String,String>) ois.readObject(); // unchecked cast
				ois.close();
				fis.close();
			} catch (IOException | ClassNotFoundException | GitAPIException e) {
				Logger l = new Logger();
				l.logError(e);
			}
			return hashtable;
		}
		else
		{
			return new Hashtable<String,String>();
		}
	}

	public static boolean existiert_datei() {

		try {
			String ordner = Upload.getInstance().getRepositoryPfad();
			File datei = new File(ordner+nutzerfilename);
//			System.out.println(datei+"-->" +datei.isFile());
			return datei.isFile();
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void addUsernameNextToButton() {
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Eingegebener Username ohne äußeren Whitespace
				String theUsername = username.getText().strip();
				// Normalisierter Username für Addressierung
				String clean_username = Tools.normalisiereString(theUsername);
				mainapp.primaryStage.setTitle("SeminarLeiterApp " + theUsername);

				// schauen, ob das Feld nicht leer ist
				// Auswahlliste von Namen davor anzeigen
				boolean nutzer_vorhanden = isNutzerVorhanden(clean_username);

				if(nutzer_vorhanden)
				{
					MainApp.setUserName(theUsername);
					mainapp.showTabPane();
				}
				else
				{
					boolean account_erstellt = neuen_Account_erstellen(clean_username);
					if (account_erstellt)
					{
						nutzernamenUndOrdner.put(theUsername, clean_username);
						System.out.println("Neuer Account erstellt: Key-> "+ theUsername +"\tValue-> "+clean_username);
						speichern();
						MainApp.setUserName(theUsername);
						mainapp.showTabPane();
					}
				}
			}

		});
	}

	public void addListener_TextFieldSuggestion() {
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			zutreffende_Nutzer.clear();
			if (!newValue.isBlank())
			{
				Pattern pattern = Pattern.compile(".*"+newValue+".*", Pattern.CASE_INSENSITIVE);

				
				for (String string : getAlle_Nutzer()) {
					Matcher matcher = pattern.matcher(string);
					if(matcher.matches())
					{
						zutreffende_Nutzer.add(string);
					}
				if (zutreffende_Nutzer.size()>LIMIT) {
					return;			
					}
				}
			}
			username.fill_context_menu(zutreffende_Nutzer);
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
				log.logError(new RuntimeException("Eine Datei mit diesem Namen existiert leider schon!"+"\tPath= "+path+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory()));
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
				log.logError(new RuntimeException("Dieser Benutzername kann nicht verwendet werden!"+"\tPath= "+path_string+"\tExists: "+test_file.exists()+"\tIs Directory: "+test_file.isDirectory()));
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
