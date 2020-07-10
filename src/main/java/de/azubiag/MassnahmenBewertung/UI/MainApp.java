package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

import javafx.application.Application;
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
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

	/* Status:	- Contextmenu muss zur Textbox in Zustand0 hinzugef�gt werden -> autocomplete f�r Nutzernamen
	 * 			- Buttons in upload.fxml machen noch beide dasselbe -> darf man den Upload überhaupt abbrechen ???
	 * 			- Antwort hinzuf�gen in Zustand2 muss implementiert werden
	 *      	- Erscheinende TextFields in Zustand1+2 m�ssen implementiert werden
	 *      	- Anwendung muss an den Rest angebunden werden (Dekodoerung von Strings, Weitergabe danach     +   Auswertung in Zustand 3)
	 */

public class MainApp extends Application {

	private Stage primaryStage;
	private TabPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SeminarLeiterApp");

		showLogin();

		//        showcreate();
	}


	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand0.fxml"));
			GridPane login_grid = (GridPane) loader.load();
			login_grid.setPrefSize(600, 200);

			Scene scene = new Scene(login_grid);
			primaryStage.setScene(scene);

			Zustand0Controller controller = loader.getController();
			//			System.out.println(controller);
			controller.setMainapp(this);
			addUsernameNextToButton(controller.next, controller.username);

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

			// Berechnen, welche Tabs offen sein m�ssen

			showStep1();

			// am Ende Plus Tab anzeigen
			showPlus();

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void showStep1() {	// Tab Text muss sich �ndern + Anzahl der Referentenfelder m�ssen sich �ndern + Button sperren, wenn Name leer ist
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand1.fxml"));
			BorderPane z1 = (BorderPane) loader.load();				// !!
			Tab tab_z1 = new Tab();
			tab_z1.setContent(z1);
			tab_z1.setClosable(true);
			//			tab_z1.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z1.setText("Unbenannter Fragebogen");
			rootLayout.getTabs().add(tab_z1);
			Zustand1Controller controller = loader.getController();
			//			            System.out.println(controller);
			controller.setMainApp(this);
			addDeleteToButton(controller.delete , rootLayout, tab_z1);
			addPreviewToButton(controller.preview, controller,rootLayout.getTabs().indexOf(tab_z1));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showStep2(String name, int index) {	
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand2.fxml"));
			BorderPane z2 = (BorderPane) loader.load();				// !!
			Tab tab_z2 = new Tab();
			tab_z2.setContent(z2);
			tab_z2.setClosable(true);
			//			tab_z2.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z2.setText(name);
			rootLayout.getTabs().add(index+1, tab_z2);
			Zustand2Controller controller = loader.getController();
			//			            System.out.println(controller);
			controller.setMainApp(this);
			controller.setName(name);
			controller.setMaintext(name);
			addDeleteToButton(controller.delete , rootLayout, tab_z2);
			addAnswerToButton(controller.add);
			addNext2ToButton(controller.next,controller.getName(),rootLayout.getTabs().indexOf(tab_z2));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showStep3(String name, int index) {	// incomplete
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("Zustand3.fxml"));
			BorderPane z3 = (BorderPane) loader.load();				// !!
			Tab tab_z3 = new Tab();
			tab_z3.setContent(z3);
			tab_z3.setClosable(true);
			//			tab_z3.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z3.setText(name);
			System.out.println(index);
			rootLayout.getTabs().add(index,tab_z3);
			rootLayout.getTabs().remove(index-1);
			Zustand3Controller controller = loader.getController();
			//			            System.out.println(controller);
			controller.setMainApp(this);
			controller.setName(name);
			addDeleteToButton(controller.delete , rootLayout, tab_z3);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addUsernameNextToButton(Button button, TextField field) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {

				// schauen, ob das Feld nicht leer ist
				// Auswahlliste von Namen davor anzeigen
				showTabPane();
			}
		});
	}

	public void addDeleteToButton(Button button, TabPane pane, Tab thistab) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				pane.getTabs().remove(thistab);
			}
		});
	}

	public void addPreviewToButton(Button button, Zustand1Controller controller, int index) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {

				try {
					Desktop.getDesktop().browse(new URL("https://ooppitz.github.io/prototyp.html").toURI());
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Fragebogen veröffentlichen?");
					alert.setHeaderText("Fragebogen veröffentlichen?");

					ButtonType buttonTypeYes = new ButtonType("Ja");
					ButtonType buttonTypeCancel = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);

					alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == buttonTypeYes){
						// Nutzer dr�ckt ja
						// JGit l�dt Datei hoch
						
						// Fortschritt anzeigen? Link anzeigen?
						
						Dialog<ButtonType> dialog = new Dialog<>();
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(MainApp.class.getResource("upload.fxml"));
						DialogPane grid = (DialogPane) loader.load();
						dialog.setDialogPane(grid);
						dialog.getDialogPane().getButtonTypes().addAll(ButtonType.NEXT,ButtonType.CANCEL);
						
						
						dialog.initOwner(primaryStage);
						dialog.initModality(Modality.APPLICATION_MODAL);
						UploadController upload_controller = loader.getController();
						
						// 8.8.8.8 pingen
						// �berpr�fen, ob Datei existiert (Error Code 404 m�glicherweise nicht m�glich, da Github Pages trotzdem etwas anzeigt)
						// sehen, ob das erste div-element eine bestimmte komplizierte ID hat?
						// fx-thread nicht blockieren !!!
						// Abbrechen erlauben ?
						
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
						showStep2(controller.getName(), index);
						
						if (result3.get() == buttonTypeYes3){
							// Fragebogen klonen
							controller.setName("Kopie von "+controller.getName());
						}
						else {
							rootLayout.getTabs().remove(index);
						}
						
					} else {
						// nichts tun
					}

				} catch (MalformedURLException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText("Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n MalformedURLException beim Preview-Alert");
					alert.showAndWait();
					
				} catch (IOException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText("Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n IOException beim Preview-Alert");
					alert.showAndWait();
					
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Fehler");
					alert.setHeaderText("Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n URISyntaxException beim Preview-Alert");
					alert.showAndWait();
					
				}
				
				
			}
		});
	}

	public void addAnswerToButton(Button button) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				// Ergebnis von der Zwischenablage kopieren
			}
		});
	}
	
	public void addNext2ToButton(Button button, String name, int index) {	// Auswertung
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				// Next
				// verschl�sselte Antwort an den Crypto-Teil des Programms schicken
				
				// Auswertung zur�ckbekommen
				showStep3(name, index);
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
				if(tab_plus.isSelected())
				{
					int size = rootLayout.getTabs().size(); // amount of tabs
					if(size!=1)
					{
						rootLayout.getTabs().remove(size-1);
						showStep1();
						rootLayout.getTabs().add(tab_plus);
					}
					else
					{
						showStep1();
						rootLayout.getTabs().remove(0);
						rootLayout.getTabs().add(tab_plus);
					}
					SingleSelectionModel<Tab> selectionModel = rootLayout.getSelectionModel();
					selectionModel.select(size-1);
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