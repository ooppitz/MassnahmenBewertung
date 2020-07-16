package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import de.azubiag.MassnahmenBewertung.htmlcreator.HtmlCreator;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/* Erstellen des Fragebogens */

public class ControllerFragebogenErstellen {

	int anzahl_referenten;

	@FXML
	GridPane gridpane;

	@FXML
	TextField name;

	@FXML
	Label referent_label;

	@FXML
	TextField referent_name;

	@FXML
	public Button preview;

	@FXML
	public Button delete;

	private MainApp mainapp;

	public void setMainApp(MainApp app) {
		mainapp = app;
	}

	public String getName() {
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void addneuerReferent() {
		referent_name.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (oldValue == false && newValue == true) {
					if (anzahl_referenten > 6) {
						gridpane.setPrefHeight(gridpane.getPrefHeight() + 49);
						gridpane.addRow(anzahl_referenten + 3);
						// Eigenschaften der neuen Row ändern, sodass sie genau so wie die vorherigen
						// aussieht
					}

					Label temp = new Label();
					temp.setText("   Name von Referent ");
					temp.setText(temp.getText() + (anzahl_referenten + 3) + ":");
					temp.setFont(referent_label.getFont());

					TextField temp2 = new TextField();
					temp2.setPromptText("Klicken, um einen weiteren Referenten hinzuzufügen");
					temp2.setFont(referent_name.getFont());

					gridpane.getChildren().remove(referent_name);
					gridpane.add(referent_name, 2, anzahl_referenten + 3, 3, 1);

					gridpane.add(temp, 0, anzahl_referenten + 3, 2, 1);
					gridpane.add(temp2, 2, anzahl_referenten + 2, 3, 1);
					anzahl_referenten++;

					temp2.requestFocus();
				}
			}
		});
	}

	ArrayList<String> getReferentenNamen() {

		ArrayList<String> referentenNamen = new ArrayList<String>(
				List.of("Pfaffelhuber", "Werner", "Meier", "Dosterschill"));
		return referentenNamen;

	}

	public void addVorschauButtonHandler(int index) {
		preview.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				try {

					String pathFragebogenFile;
					try {
						pathFragebogenFile = Upload.getInstance().getRepositoryPfad() + "fragebogen.html";
						
					} catch (Exception exc) {
						
						// Hochladen hat nicht geklappt
						Alert error = new Alert(AlertType.ERROR);
						error.setTitle("Probleme beim Hochladen");
						error.setHeaderText("Das Hochladen des Fragebogens hat nicht geklappt. Probieren Sie es später nochmal.");
						ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
						error.getButtonTypes().setAll(end);
						error.showAndWait();
						return;	
					}
					
					HtmlCreator creator = new HtmlCreator(getReferentenNamen(),
							"C:\\Users\\oliveroppitz\\git\\MassnahmenBewertung\\src\\main\\resources\\de\\azubiag\\MassnahmenBewertung\\template.html",
							pathFragebogenFile);
					creator.createHtml();

					Desktop.getDesktop().browse(new URL("file://" + pathFragebogenFile).toURI());
					// echter Fragebogen muss noch generiert werden !!!
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Fragebogen veröffentlichen?");
					alert.setHeaderText("Fragebogen veröffentlichen?");

					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeCancel = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

					Optional<ButtonType> result = alert.showAndWait();
					
					if (result.get() == buttonTypeYes) { 	// Nutzer drückt "ja"
					
						try {
							
							Upload repoHandle = Upload.getInstance();  // JGit lädt Datei hoch
							repoHandle.hochladen();
							
						} catch (Exception exc) {
							
							// Hochladen hat nicht geklappt
							Alert error = new Alert(AlertType.ERROR);
							error.setTitle("Probleme beim Hochladen");
							error.setHeaderText("Das Hochladen des Fragebogens hat nicht geklappt. Probieren Sie es später nochmal.");
							ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
							error.getButtonTypes().setAll(end);
							error.showAndWait();
							return;
							
						}

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
						System.out.println(result2);
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
						mainapp.showAntwortenErfassen(getName(), index);

						if (result3.get() == buttonTypeYes3) {
							// Fragebogen klonen
							setName("Kopie von " + getName());
						} else {
							mainapp.rootLayout.getTabs().remove(index);
						}

					} else {
						// nichts tun
					}

				} catch (MalformedURLException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText(
							"Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n MalformedURLException beim Preview-Alert");
					alert.showAndWait();

				} catch (IOException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText(
							"Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n IOException beim Preview-Alert");
					alert.showAndWait();

				} catch (URISyntaxException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText(
							"Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n URISyntaxException beim Preview-Alert");
					alert.showAndWait();

				}

			}
		});
	}
}
