package de.azubiag.MassnahmenBewertung.UI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.azubiag.MassnahmenBewertung.UI.test.ClipboardLoeschenTest;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import de.azubiag.MassnahmenBewertung.upload.Upload;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/* Status:	- Contextmenu muss zur Textbox in Zustand0 hinzugef�gt werden -> autocomplete für Nutzernamen
 * 			- Button in Zustand0 wird nicht gesperrt, wenn all der Text aus dem TextField entfernt wird
 *			- Button in Zustand1 wird nicht gesperrt, wenn all der Text aus dem TextField entfernt wird
 *			- Upload wird noch nicht überprüft
 * 			- Buttons in upload.fxml machen noch beide dasselbe -> darf man den Upload überhaupt abbrechen ???
 * 			- Antwort hinzufügen in Zustand2 muss implementiert werden --> Dekodierung 
 * 			- Eigenschaften der neuen Row in Zustand2 ändern, sodass sie genau so wie die vorherigen aussieht
 *      	- Anwendung muss an den Rest angebunden werden (Dekodierung von Strings, Weitergabe danach     +   Auswertung in Zustand 3)
 */

/**
 *The User Interface
 * @author Filip Golanski
 */
public class MainApp extends Application {

	static ArrayList<ControllerAntwortenErfassen> listeControllerAntwortenErfassen = new ArrayList<ControllerAntwortenErfassen>();
	static String userName = "";

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		MainApp.userName = userName;
	}

	/* Zeigt an, ob die App im Testmodus läuft. Kann über KOmmandozeilenparameter gesteuert werden, */
	static boolean testmodusAktiv = true; 

	public static boolean isTestmodusAktiv() {
		return testmodusAktiv;
	}

	public static void setTestmodusAktiv(boolean _testmodusAktiv) {
		testmodusAktiv = _testmodusAktiv;
		Logger.getLogger().logInfo(testmodusAktiv? "Testmodus aktiviert" : "Testmodus deaktiviert");
	}

	protected Stage primaryStage;
	protected TabPane rootLayout;


	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SeminarLeiterApp");


		showLogin();

		// showcreate();
	}


	/**
	 *
	 * The login-window appears.<br>
	 * Related: {@link de.azubiag.MassnahmenBewertung.UI.ControllerLogin ControllerLogin}
	 *  @author Filip Golanski <br>
	 */
	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ControllerLogin.fxml"));
			GridPane login_grid = (GridPane) loader.load();
			login_grid.setPrefSize(800, 200);

			Scene scene = new Scene(login_grid);
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(200);
			primaryStage.setMinWidth(600);

			ControllerLogin controller = loader.getController();
			// System.out.println(controller);
			controller.setMainapp(this);
			controller.addUsernameNextToButton();
			controller.init();
			controller.addListener_TextFieldSuggestion();
			controller.username.textProperty().addListener((observable, oldValue, newValue) -> { 

				// Knopf wird aktiviert, wenn non-whitespace Zeichen vorhanden sind
				controller.next.setDisable(!(newValue.matches(".*\\S+.*")) ? true : false); 
				
			});

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTabPane() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("tabpane.fxml"));
			rootLayout = (TabPane) loader.load();
			rootLayout.setPrefSize(1280, 720);
			rootLayout.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.hide();
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(800);
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					warnfenster(event);
				}
			});
			
			// TODO: Alle weiteren Tabs für Fragebögen öffnen, deren Antworten eingegeben werden sollen 
			// Aufrufen von showAntwortenErfassen()
			if (existieren_speicherdaten())
				Logger.getLogger().logInfo("Speicherdatei existiert!");
			else
				Logger.getLogger().logInfo("Speicherdatei existiert nicht!");

			if (existieren_speicherdaten()) {
				speicherdaten_laden();
				boolean erfolg = speicherdaten_löschen();
				if (erfolg)
				{
				Logger.getLogger().logInfo("Alte Speicherdatei konnte gelöscht werden!");
				}
				else
				{
					Logger.getLogger().logWarning("Alte Speicherdatei konnte  NICHT  gelöscht werden!");
					// TODO: Fehlermeldung, dann schließen des Programms?
				}
			}
			else
			{
				showTabFragebogenErstellen();
			}

			// am Ende Plus Tab anzeigen
			showTabPlus();

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTabFragebogenErstellen() { // Tab Text muss sich ändern + Anzahl der Referentenfelder müssen sich ändern +
		// Button sperren, wenn Name leer ist
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ControllerFragebogenErstellen.fxml"));
			BorderPane z1 = (BorderPane) loader.load(); // !!
			Tab tab_z1 = new Tab();
			tab_z1.setContent(z1);
			tab_z1.setClosable(true);
			// tab_z1.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z1.setText("Unbenannter Fragebogen");
			rootLayout.getTabs().add(tab_z1);
			ControllerFragebogenErstellen controller = loader.getController();	
			// System.out.println(controller);
			controller.setMainApp(this);
			controller.setTab(tab_z1);
			controller.init();
			addHandlerToDeleteButton(controller.delete, tab_z1, controller);
			controller.addVorschauButtonHandler();
			controller.addneuerReferent();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param verifyID Id des Fragebogens
	 */
	public void showTabAntwortenErfassen(FragebogenEigenschaften eigenschaft, int indexInTabPane, int verifyID) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ControllerAntwortenErfassen.fxml"));
			BorderPane z2 = (BorderPane) loader.load(); // !!
		
			ControllerAntwortenErfassen controller = loader.getController();
			
			Tab tab_z2 = erzeugeTab(z2, eigenschaft.fragebogen_name, controller);
			rootLayout.getTabs().add(indexInTabPane + 1, tab_z2);
			
			controller.setMainApp(this);
			controller.setTab(tab_z2);
			
			controller.setEigenschaft(eigenschaft);
			
			
			controller.setName(eigenschaft.fragebogen_name);
			controller.setMaintext(eigenschaft.fragebogen_name);
			controller.setUmfrageID(verifyID);
			controller.init();
			addHandlerToDeleteButton(controller.delete, tab_z2, controller);
			controller.setHandlerAnswerButton();
			controller.addNext2ToButton(controller);
			SingleSelectionModel<Tab> single_model = rootLayout.getSelectionModel();
			single_model.select(tab_z2);
			listeControllerAntwortenErfassen.add(controller);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTabAuswertungAnzeigen(FragebogenEigenschaften eigenschaft, int index,List<AzubiAntwort> antwortListe) { // incomplete
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ControllerAuswertungAnzeigen.fxml"));
			BorderPane z3 = (BorderPane) loader.load(); // !!
			Tab tab_z3 = new Tab();
			tab_z3.setContent(z3);
			tab_z3.setClosable(true);
			// tab_z3.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z3.setText(eigenschaft.fragebogen_name);
			System.out.println(index);
			rootLayout.getTabs().add(index +1, tab_z3);
			rootLayout.getTabs().remove(index);
			ControllerAuswertungAnzeigen controller = loader.getController();
			// System.out.println(controller);
			controller.setEigenschaft(eigenschaft);
			controller.init(this, eigenschaft, antwortListe);
			controller.erzeugeDarstellung();
			SingleSelectionModel<Tab> single_model = rootLayout.getSelectionModel();
			single_model.select(tab_z3);

			addHandlerToDeleteButton(controller.delete, tab_z3, controller);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void speicherdaten_laden() {

		listeControllerAntwortenErfassen = ControllerAntwortenErfassen.laden();
		for (int i = 0; i < listeControllerAntwortenErfassen.size(); i++) {
			wiederherstellenTabAntwortenErfassen(i, listeControllerAntwortenErfassen.get(i));
		}
	}
	
	public void wiederherstellenTabAntwortenErfassen(int indexInTabPane, ControllerAntwortenErfassen deserialisierterController) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(MainApp.class.getResource("ControllerAntwortenErfassen.fxml"));
			BorderPane z2 = (BorderPane) loader.load(); // !!
			
			String tabName = deserialisierterController.eigenschaften.fragebogen_name;
			
			ControllerAntwortenErfassen neuer_controller = loader.getController();
		
			Tab tab_z2 = erzeugeTab(z2, tabName, neuer_controller);
			rootLayout.getTabs().add(indexInTabPane, tab_z2);
			
			neuer_controller.setMainApp(this);
			neuer_controller.setTab(tab_z2);
			neuer_controller.tab_wiederherstellen(deserialisierterController);
			
			
			addHandlerToDeleteButton(neuer_controller.delete, tab_z2, neuer_controller);
			neuer_controller.setHandlerAnswerButton();
			neuer_controller.addNext2ToButton(neuer_controller);
	
			listeControllerAntwortenErfassen.remove(indexInTabPane);
			listeControllerAntwortenErfassen.add(indexInTabPane, neuer_controller);
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private Tab erzeugeTab(BorderPane borderPane, String tabName, Controller controller) {
	
		Tab thisTab = new Tab();
		thisTab.setContent(borderPane);
		thisTab.setClosable(true);
		thisTab.setText(tabName);
		
		thisTab.setOnClosed(new EventHandler<Event>() {		// beim schließen des Tabs wird der Controller aus der Liste entfernt
			@Override
			public void handle(Event event) {
				handleUmfrageSchliessen( thisTab, controller, event);
			}
		});
		
		return thisTab;
	}
	
	public boolean speicherdaten_löschen() {
		
		try {
			Upload upload = Upload.getInstance();
			String ordner = upload.getSeminarleiterDirectory(userName);
			File speicherdatei = new File(ordner + ControllerAntwortenErfassen.saveFileName);
			return speicherdatei.delete();
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public void addHandlerToDeleteButton(Button button, Tab thistab, Controller controller) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				handleUmfrageSchliessen( thistab, controller, e);
			}
		});
	}

	
	public void handleUmfrageSchliessen( Tab thistab, Controller controller, Event event) {
		boolean loeschen = (AlertMethoden.zeigeAlertJaNeinAbbrechen(AlertType.WARNING, "Umfrage schließen", "Wenn Sie fortfahren, werden alle Daten der Umfrage gelöscht. Trotzdem fortfahren ? ")==1)? true:false;
		
		if (loeschen) {
			deleteActions( thistab, controller);
		}else {
			event.consume(); //bei Tab: setOnCloseRequest Schließen stoppen
		}
	}

	public void deleteActions( Tab thistab, Controller controller) {
		rootLayout.getTabs().remove(thistab);
		if (controller.getClass()== ControllerAntwortenErfassen.class) {
			MainApp.listeControllerAntwortenErfassen.remove(controller);
		}
		String seminarleiter = MainApp.getUserName();

		try {
			File f = new File(Upload.getInstance().getFragebogenPfad(seminarleiter, thistab.getText()));
			
			if (f!=null) {
				if (f.delete()) // returns Boolean value
				{
					Upload.getInstance().hochladen();
					System.out.println(f.getName() + " deleted"); // getting and printing the file name
				} else {
					System.out.println("failed");
				} 
			}
		} catch (GitAPIException | IOException exc) {
			exc.printStackTrace();
		}
	}
	public void showTabPlus() {
		Tab tab_plus = new Tab();
		tab_plus.setText("+");
		tab_plus.setStyle("-fx-background-color:#DDD; -fx-border-color:#DDD; -fx-font-size:20px;");
		rootLayout.getTabs().add(tab_plus);

		tab_plus.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event t) {
				if (tab_plus.isSelected()) {
					Logger logger = Logger.getLogger();
					logger.logInfo("Neuer-Tab-Reiter geklickt");
					int size = rootLayout.getTabs().size(); // amount of tabs
					if (size != 1) {
						rootLayout.getTabs().remove(size - 1);
						showTabFragebogenErstellen();
						rootLayout.getTabs().add(tab_plus);
					} else {
						showTabFragebogenErstellen();
						rootLayout.getTabs().remove(0);
						rootLayout.getTabs().add(tab_plus);
					}
					SingleSelectionModel<Tab> selectionModel = rootLayout.getSelectionModel();
					selectionModel.select(size - 1);
				}

			}
		});
	}


	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {

		for (int i=0; i<args.length; i++) {
			if (args[i].equals("--test")) setTestmodusAktiv(true);
		}

		launch(args);
	}

	
	public void warnfenster(WindowEvent event) {
		if (!listeControllerAntwortenErfassen.isEmpty())
		{
			int resultFortschrittSpeichern = AlertMethoden.zeigeAlertJaNeinAbbrechen(AlertType.WARNING, "Warnung", "Wollen Sie den Fortschritt speichern?");
	
			if(resultFortschrittSpeichern == 1) {
				ControllerAntwortenErfassen.speichern();
				Platform.exit();
			} else if(resultFortschrittSpeichern == 0) {
				System.out.println("Fortschritt wird verworfen!");
				Platform.exit();
			} else {
				System.out.println("Schließen wird abgebrochen");
				event.consume();
			}
			System.out.println("Der Rest der Methode wird noch durchgeführt!");
		}
	}

	public boolean existieren_speicherdaten() {

		try {
			Upload upload = Upload.getInstance();
			String ordner = upload.getSeminarleiterDirectory(userName);
			File speicherdatei = new File(ordner+ControllerAntwortenErfassen.saveFileName);
			if (speicherdatei.isFile())
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}