package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import de.azubiag.MassnahmenBewertung.htmlcreator.HtmlCreator;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* Erstellen des Fragebogens */

public class ControllerFragebogenErstellen {

	Tab tab;
	int anzahl_referenten;

	@FXML
	GridPane gridpane;

	@FXML
	Label description;

	@FXML 
	TextField fragebogenname;
	
	@FXML
	Label maßnahme_von;
	
	@FXML
	DatePicker von_Datum;
	
	@FXML
	Label maßnahme_bis;
	
	@FXML 
	DatePicker bis_Datum;
	
	@FXML
	Label auftragsnummer_label;
	
	@FXML 
	TextField auftragsnummer_textfield;
	
	@FXML
	Label leiter_label1;
	
	@FXML 
	Label leiter_label2;
	
	@FXML
	DatePicker heute_datum;

	@FXML
	Label referent_label;

	@FXML
	TextField referent_name;

	@FXML
	Label referent_label_first;

	@FXML
	TextField referent_name_first;

	@FXML
	Button ref1_x;

	@FXML
	Button ref2_x;

	@FXML
	public Button preview;

	@FXML
	public Button delete;

	private MainApp mainapp;

	public void init() {
		entferneReferent(ref1_x);
		entferneReferent(ref2_x);
		readdNode(description, 1, 0);
		readdNode(fragebogenname, 3, 0);
		
		readdNode(maßnahme_von, 1, 1);
		readdNode(von_Datum, 3, 1);
//		((TextField) von_Datum.getChildrenUnmodifiable().get(0)).setFont(referent_label.getFont()); 	// Ansatz geht nicht
		readdNode(maßnahme_bis, 4, 1);
		readdNode(bis_Datum, 5, 1);
//		((TextField) bis_Datum.getChildrenUnmodifiable().get(0)).setFont(referent_label.getFont());
		readdNode(auftragsnummer_label, 1, 2);
		readdNode(auftragsnummer_textfield, 3, 2);
		readdNode(leiter_label1, 1, 3);
		readdNode(leiter_label2, 3, 3);
		leiter_label2.setText(mainapp.getUserName());
		readdNode(heute_datum, 5, 3);
		heute_datum.setValue(LocalDate.now());
//		((TextField) heute_datum.getChildrenUnmodifiable().get(0)).setFont(referent_label.getFont());
		
		readdNode(ref1_x, 0, 4);
		readdNode(referent_label_first, 1, 4);
		readdNode(referent_name_first, 3, 4);
		readdNode(ref2_x, 0, 5);
		readdNode(referent_label, 1, 5);
		readdNode(referent_name, 3, 5);
		
		fragebogenname.textProperty().addListener((observable, oldValue, newValue) -> { 
			
//			if (oldValue != "" || newValue != "")
				tab.setText(newValue);
//			else
//				tab.setText("Unbenannter Fragebogen");
		});
	}

	public void readdNode(Node node, int col, int row)
	{
		gridpane.getChildren().remove(node);
		gridpane.add(node, col, row);
	}

	public void setMainApp (MainApp app){
		mainapp = app;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getName() {
		return fragebogenname.getText();
	}

	public void setName(String name) {
		this.fragebogenname.setText(name);
	}

	public void addneuerReferent() {
		referent_name.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (oldValue == false && newValue == true) {
					if (anzahl_referenten > 3) {
						gridpane.setPrefHeight(gridpane.getPrefHeight() + 49);
						gridpane.addRow(anzahl_referenten + 6);
						// Eigenschaften der neuen Row ändern, sodass sie genau so wie die vorherigen
						// aussieht
					}

					Label temp = new Label();
					temp.setText("     Name von Referent ");
					temp.setText(temp.getText() + (anzahl_referenten + 3) + ":");
					temp.setFont(referent_label.getFont());

					TextField temp2 = new TextField();
					temp2.setPromptText("Klicken, um einen weiteren Referenten hinzuzufügen");
					temp2.setFont(referent_name.getFont());

					Button x_button = new Button();
					x_button.setText("x");
					entferneReferent(x_button);

					gridpane.getChildren().remove(referent_name);
					gridpane.add(referent_name, 3, anzahl_referenten + 6, 3, 1);

					gridpane.add(x_button, 0, anzahl_referenten + 6, 1, 1);
					gridpane.add(temp, 1, anzahl_referenten + 6, 2, 1);
					gridpane.add(temp2, 3, anzahl_referenten + 5, 3, 1);
					anzahl_referenten++;
					
					int letzteRow = anzahl_referenten+5;
					for (int i = 5; i < letzteRow; i++) {
						Node temp3 = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 0);
						if (temp3!=null)
						{
							((Button)temp3).setDisable(false);
						}
					}
					Node temp4 = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
					((Button)temp4).setDisable(true);
					
					temp2.requestFocus();
				}
			}
		});
	}

	public void entferneReferent(Button button) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Logger logger = Logger.getLogger();
				logger.logInfo("Aktion: Referent soll gelöscht werden");
				/* Ablauf:
				 * - letzter Button wird entfernt
				 * - letzter Label wird entfernt
				 * - TextField neben diesem Button wird entfernt
				 * - alle Textfields darunter werden nach oben verschoben
				 * - möglicherweise wird das Gridpane um 49 Höhe kleiner			<--  fehlt noch
				 * - anzahlReferenten wird dekrementiert
				 */

				int letzteRow = anzahl_referenten+5;
				logger.logInfo("ControllerFragebogenErstellen.entferneReferent\nletzte Reihe: "+letzteRow);
				Button letzterButton = (Button) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
				Label letzterLabel = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 1);
				TextField textfieldnebendiesembutton = (TextField) GridPaneCustom.getElemByRowAndColumn(gridpane, GridPane.getRowIndex(button), 3);

				gridpane.getChildren().removeAll(letzterButton, letzterLabel, textfieldnebendiesembutton);

				for (int i = 5; i < letzteRow; i++) {
					Node temp = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 0);
					if (temp!=null)
					{
						((Button)temp).setDisable(false);
					}
				}
				Node temp2 = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow-1, 0);
				((Button)temp2).setDisable(true);
				
				for (int i = GridPane.getRowIndex(button)+1; i <= letzteRow; i++) {

					Node temp = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 3);
//					System.out.println("temp node:\t"+temp);
					if (temp!=null)
					{
						GridPaneCustom.moveElemByRowAndColumn(temp, gridpane, -1, 0);
					}
				}

				anzahl_referenten--;
			}
		});
	}

	ArrayList<String> getReferentenNamen() {

		int skip = 2;
		ArrayList<String> referentenNamen = new ArrayList<String>();

		for (Node node : gridpane.getChildrenUnmodifiable()) {

			try {
				TextField temp = (TextField) node;
				if (skip<=0 && !(temp.getText().equals("")) )
				{
					referentenNamen.add(temp.getText());
				}
				else
				{
					skip--;
				}
			} catch (Exception e) {
				// occurs on labels and buttons
			}
		}
		return referentenNamen;
	}


	public void addVorschauButtonHandler() {
		preview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Logger logger = Logger.getLogger();
				try {

					Random random = new Random();
					int verifyID = random.nextInt();
					
					// Erstellen des Fragebogen-Files

					String seminarleiterName = MainApp.getUserName();
					String fragebogenTemplateDirectory = Upload.getInstance().getTemplateDirectory() + "template_fragebogen.html";
					String fragebogenOutputPfad = Upload.getInstance().getFragebogenPfad(seminarleiterName, fragebogenname.getText());
					
					// Schreibt den Fragebogen in das Repository
					new HtmlCreator(getReferentenNamen(), fragebogenTemplateDirectory, fragebogenOutputPfad, verifyID).createHtml();
					
					Desktop.getDesktop().browse(new URL("file://" + fragebogenOutputPfad).toURI());

					// Entfernen von .html, weil es manchmal auf github.io zu Problemen führt
					int indexA = fragebogenOutputPfad.indexOf("gfigithubaccess");
					int indexB = fragebogenOutputPfad.indexOf(".html");
					String webpath = "https://" + fragebogenOutputPfad.substring(indexA, indexB);
					webpath = webpath.replace('\\', '/');
					Logger.getLogger().logInfo(webpath);
					
					
					
					
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Fragebogen veröffentlichen?");
					alert.setHeaderText("Fragebogen auf " + webpath + " veröffentlichen?");
					
					//Dialgofenster zum Hochladen soll über der Browservorschau angezeigt werden, um den Nutzer nicht 
					//zu verwirren:
					Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow(); 
					alertStage.setAlwaysOnTop(true);

					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeCancel = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == buttonTypeYes) { // Nutzer drückt "ja"

						// Fortschritt anzeigen? Link anzeigen?

						Dialog<ButtonType> dialog = new Dialog<>();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(MainApp.class.getResource("upload.fxml"));
						DialogPane grid = (DialogPane) loader.load();
						dialog.setDialogPane(grid);
						ButtonType cancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
						dialog.getDialogPane().getButtonTypes().add(cancel);

						dialog.initOwner(mainapp.primaryStage);
						dialog.initModality(Modality.APPLICATION_MODAL);
						dialog.setTitle("Hochladen");
						UploadController upload_controller = loader.getController();

						try {

							Upload repoHandle = Upload.getInstance(); // JGit lädt Datei hoch
							if (!MainApp.testmodusAktiv) repoHandle.hochladen(fragebogenname.getText(),seminarleiterName);					

						} catch (Exception exc) {
							
							logger.logError(exc);
							// Hochladen hat nicht geklappt
							Alert error = new Alert(AlertType.ERROR);
							error.setTitle("Probleme beim Hochladen");
							error.setHeaderText(
									"Das Hochladen des Fragebogens hat nicht geklappt. Probieren Sie es später nochmal.");
							ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
							error.getButtonTypes().setAll(end);
							error.showAndWait();
							return;

						}
						// 8.8.8.8 pingen
						// �berpr�fen, ob Datei existiert (Error Code 404 m�glicherweise nicht m�glich,
						// da Github Pages trotzdem etwas anzeigt)
						// sehen, ob das erste div-element eine bestimmte komplizierte ID hat?
						// fx-thread nicht blockieren !!!
						// Abbrechen erlauben ?

						dialog.getDialogPane().getButtonTypes().remove(cancel);
						ButtonType next = new ButtonType("Weiter", ButtonData.NEXT_FORWARD);
						dialog.getDialogPane().getButtonTypes().add(next);

						upload_controller.upload_pending.setText("Hochladen erfolgreich!");
						upload_controller.progress.setProgress(1);
						Optional<ButtonType> result2 = dialog.showAndWait(); // Buttons abfragen!!!!
						logger.logInfo("result2 = " + result2.toString());
						// w�re praktisch, den Link noch woanders anzuzeigen

						// Fenster f�r Klonen anzeigen
						Alert alert3 = new Alert(AlertType.CONFIRMATION);
						alert3.setTitle("Neuen Fragebogen mit denselben Referenten anlegen?");
						alert3.setHeaderText("Neuen Fragebogen mit denselben Referenten anlegen?");
						ButtonType buttonTypeYes3 = new ButtonType("Ja");
						ButtonType buttonTypeCancel3 = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);
						alert3.getButtonTypes().setAll(buttonTypeYes3, buttonTypeCancel3);
						Optional<ButtonType> result3 = alert3.showAndWait();

						// Zustand2-Tab erstellen
						mainapp.showAntwortenErfassen(getName(), tab.getTabPane().getTabs().indexOf(tab), verifyID);

						if (result3.get() == buttonTypeYes3) {
							// Fragebogen klonen
							setName("Kopie von " + getName());
						} else {
							mainapp.rootLayout.getTabs().remove(tab.getTabPane().getTabs().indexOf(tab));
						}

					} else {
						// nichts tun
					}

				} catch (IOException | URISyntaxException | GitAPIException e1) {
					// SO KRIEGT MAN DEN TYP DER EXCEPTION: MIT GETCLASS.GETNAME!!!!!!!!!!!!!!!!!!!!!!
					logger.logError(e1);
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText(
							"Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n"
									+ e1.getClass().getName() + " beim Preview-Alert. \n"
									+ "Interne Fehlermeldung: " + e1.getMessage());
					alert.showAndWait();

				}

			}
		});
	}
}
