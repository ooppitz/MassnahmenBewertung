package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/* Status:	- Contextmenu muss zur Textbox in Zustand0 hinzugef�gt werden -> autocomplete für Nutzernamen
 * 			- Button in Zustand0 wird nicht gesperrt, wenn all der Text aus dem TextField entfernt wird
 *			- Button in Zustand1 wird nicht gesperrt, wenn all der Text aus dem TextField entfernt wird
 *			- Upload wird noch nicht überprüft
 * 			- Buttons in upload.fxml machen noch beide dasselbe -> darf man den Upload überhaupt abbrechen ???
 * 			- Antwort hinzufügen in Zustand2 muss implementiert werden --> Dekodierung 
 * 			- Eigenschaften der neuen Row in Zustand2 ändern, sodass sie genau so wie die vorherigen aussieht
 *      	- Anwendung muss an den Rest angebunden werden (Dekodierung von Strings, Weitergabe danach     +   Auswertung in Zustand 3)
 */

public class MainApp extends Application {

	Stage primaryStage;
	TabPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SeminarLeiterApp");

		showLogin();

		// showcreate();
	}

	/**
	 * The login-window appears.<br>
	 * Related:
	 * {@link de.azubiag.MassnahmenBewertung.UI.MainApp#addUsernameNextToButton(Button, TextField)
	 * addUsernameNextToButton(Button, TextField)} <br>
	 * Related: {@link de.azubiag.MassnahmenBewertung.UI.ControllerLogin
	 * Zustand0Controller}
	 */
	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand0.fxml"));
			GridPane login_grid = (GridPane) loader.load();
			login_grid.setPrefSize(600, 200);

			Scene scene = new Scene(login_grid);
			primaryStage.setScene(scene);

			ControllerLogin controller = loader.getController();
			// System.out.println(controller);
			controller.setMainapp(this);
			controller.addUsernameNextToButton();
			controller.username.textProperty().addListener((observable, oldValue, newValue) -> { // für eine "normale"
																									// Methode müssten
																									// all diese Buttons
																									// gleich heißen
				controller.next.setDisable((newValue == "") ? true : false);
				System.out.println("old: " + oldValue + " ---> new: " + newValue);
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
			rootLayout.setPrefSize(800, 600);
			rootLayout.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.hide();
			primaryStage.setMaxHeight(600);
			primaryStage.setMaxWidth(800);

			// Berechnen, welche Tabs offen sein müssen

			showFragebogenErstellen();

			// am Ende Plus Tab anzeigen
			showPlus();

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showFragebogenErstellen() { // Tab Text muss sich ändern + Anzahl der Referentenfelder müssen sich ändern +
								// Button sperren, wenn Name leer ist
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand1.fxml"));
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
			addDeleteToButton(controller.delete, rootLayout, tab_z1);
			controller.addPreviewToButton(rootLayout.getTabs().indexOf(tab_z1));
			addneuerReferent(controller.referent_name, controller);
			controller.name.textProperty().addListener((observable, oldValue, newValue) -> { // für eine "normale"
																								// Methode müssten all
																								// diese Buttons gleich
																								// heißen
				controller.preview.setDisable((newValue == "") ? true : false);
				System.out.println("old: " + oldValue + " ---> new: " + newValue);
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAntwortenErfassen(String name, int index) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand2.fxml"));
			BorderPane z2 = (BorderPane) loader.load(); // !!
			Tab tab_z2 = new Tab();
			tab_z2.setContent(z2);
			tab_z2.setClosable(true);
			// tab_z2.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z2.setText(name);
			rootLayout.getTabs().add(index + 1, tab_z2);
			ControllerAntwortenErfassen controller = loader.getController();
			// System.out.println(controller);
			controller.setMainApp(this);
			controller.setName(name);
			controller.setMaintext(name);
			addDeleteToButton(controller.delete, rootLayout, tab_z2);
			controller.addAnswerToButton();
			addNext2ToButton(controller.next, controller.getName(), rootLayout.getTabs().indexOf(tab_z2));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAuswertungAnzeigen(String name, int index) { // incomplete
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand3.fxml"));
			BorderPane z3 = (BorderPane) loader.load(); // !!
			Tab tab_z3 = new Tab();
			tab_z3.setContent(z3);
			tab_z3.setClosable(true);
			// tab_z3.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z3.setText(name);
			System.out.println(index);
			rootLayout.getTabs().add(index, tab_z3);
			rootLayout.getTabs().remove(index - 1);
			ControllerAuswertungAnzeigen controller = loader.getController();
			// System.out.println(controller);
			controller.setMainApp(this);
			controller.setName(name);
			addDeleteToButton(controller.delete, rootLayout, tab_z3);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addDeleteToButton(Button button, TabPane pane, Tab thistab) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				pane.getTabs().remove(thistab);
			}
		});
	}

	public void addneuerReferent(TextField referent_name, ControllerFragebogenErstellen controller) {
		referent_name.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (oldValue == false && newValue == true) {
					if (controller.anzahl_referenten > 6) {
						controller.gridpane.setPrefHeight(controller.gridpane.getPrefHeight() + 49);
						controller.gridpane.addRow(controller.anzahl_referenten + 3);
						// Eigenschaften der neuen Row ändern, sodass sie genau so wie die vorherigen
						// aussieht
					}

					Label temp = new Label();
					temp.setText("   Name von Referent ");
					temp.setText(temp.getText() + (controller.anzahl_referenten + 3) + ":");
					temp.setFont(controller.referent_label.getFont());

					TextField temp2 = new TextField();
					temp2.setPromptText("Klicken, um einen weiteren Referenten hinzuzufügen");
					temp2.setFont(controller.referent_name.getFont());

					controller.gridpane.getChildren().remove(controller.referent_name);
					controller.gridpane.add(controller.referent_name, 2, controller.anzahl_referenten + 3, 3, 1);

					controller.gridpane.add(temp, 0, controller.anzahl_referenten + 3, 2, 1);
					controller.gridpane.add(temp2, 2, controller.anzahl_referenten + 2, 3, 1);
					controller.anzahl_referenten++;

					temp2.requestFocus();
				}
			}
		});
	}

	public void addNext2ToButton(Button button, String nameFragebogen, int indexOfTab) { // Auswertung
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Next
				// controller.antwortListe an die Auswertung schicken

				
				// Auswertung zurückbekommen
				showAuswertungAnzeigen(nameFragebogen, indexOfTab);
			}
		});
	}

	public void showPlus() {
		Tab tab_plus = new Tab();
		tab_plus.setText("+");
		tab_plus.setStyle("-fx-background-color:#DDD; -fx-border-color:#DDD; -fx-font-size:20px;");
		rootLayout.getTabs().add(tab_plus);

		tab_plus.setOnSelectionChanged(new EventHandler<Event>() {
			@Override
			public void handle(Event t) {
				if (tab_plus.isSelected()) {
					int size = rootLayout.getTabs().size(); // amount of tabs
					if (size != 1) {
						rootLayout.getTabs().remove(size - 1);
						showFragebogenErstellen();
						rootLayout.getTabs().add(tab_plus);
					} else {
						showFragebogenErstellen();
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
		launch(args);
	}
}