package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import de.azubiag.MassnahmenBewertung.htmlcreator.HtmlCreator;
import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.Datum;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.upload.Upload;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

/* Erstellen des Fragebogens */

public class ControllerFragebogenErstellen implements Controller {

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
		von_Datum.getEditor().setFont(maßnahme_von.getFont());
		von_Datum.setValue(LocalDate.of(LocalDate.now().getYear(), 9, 1));
		readdNode(maßnahme_bis, 4, 1);
		readdNode(bis_Datum, 5, 1);
		bis_Datum.getEditor().setFont(maßnahme_bis.getFont());
		bis_Datum.setValue(LocalDate.of(LocalDate.now().getYear()+3, 8, 31));
		readdNode(auftragsnummer_label, 1, 2);
		readdNode(auftragsnummer_textfield, 3, 2);
		readdNode(leiter_label1, 1, 3);
		readdNode(leiter_label2, 3, 3, 3, 1);
		leiter_label2.setText(MainApp.getUserName());
		readdNode(heute_datum, 5, 3);
		heute_datum.setValue(LocalDate.now());
		heute_datum.getEditor().setFont(maßnahme_bis.getFont());
		gridpane.getChildren().remove(heute_datum);					// Das Datum ist zwar nicht in der UI zu sehen, aber immer noch vorhanden !!

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

	public void readdNode(Node node, int col, int row) {
		gridpane.getChildren().remove(node);
		gridpane.add(node, col, row);
	}
	
	public void readdNode(Node node, int col, int row, int colspan, int rowspan) {
		gridpane.getChildren().remove(node);
		gridpane.add(node, col, row, colspan, rowspan);
	}

	public void setMainApp(MainApp app) {
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

					int letzteRow = anzahl_referenten + 5;
					for (int i = 5; i < letzteRow; i++) {
						Node temp3 = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 0);
						if (temp3 != null) {
							((Button) temp3).setDisable(false);
						}
					}
					Node temp4 = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
					((Button) temp4).setDisable(true);

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
				/*
				 * Ablauf: - letzter Button wird entfernt - letzter Label wird entfernt -
				 * TextField neben diesem Button wird entfernt - alle Textfields darunter werden
				 * nach oben verschoben - möglicherweise wird das Gridpane um 49 Höhe kleiner
				 * <-- fehlt noch - anzahlReferenten wird dekrementiert
				 */

				int letzteRow = anzahl_referenten + 5;
				logger.logInfo("ControllerFragebogenErstellen.entferneReferent\nletzte Reihe: " + letzteRow);
				Button letzterButton = (Button) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
				Label letzterLabel = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 1);
				TextField textfieldnebendiesembutton = (TextField) GridPaneCustom.getElemByRowAndColumn(gridpane,
						GridPane.getRowIndex(button), 3);

				gridpane.getChildren().removeAll(letzterButton, letzterLabel, textfieldnebendiesembutton);

				for (int i = 5; i < letzteRow; i++) {
					Node temp = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 0);
					if (temp != null) {
						((Button) temp).setDisable(false);
					}
				}
				Node temp2 = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow - 1, 0);
				((Button) temp2).setDisable(true);

				for (int i = GridPane.getRowIndex(button) + 1; i <= letzteRow; i++) {

					Node temp = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 3);
//					System.out.println("temp node:\t"+temp);
					if (temp != null) {
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
				if (skip <= 0 && (temp.getText().matches(".*\\S.*"))) {
					referentenNamen.add(temp.getText());
				} else {
					skip--;
				}
			} catch (Exception e) {
				// occurs on labels and buttons
			}
		}
		return referentenNamen;
	}

String webpath;

public void addVorschauButtonHandler() {
	
		ControllerFragebogenErstellen controller = this;
		
		preview.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent e) {
				Logger logger = Logger.getLogger();

				if (allValuesEntered() && isDateCorrect()) {
					fragebogenHandling(logger);
				} else {
					// Warnung anzeigen, wenn nicht alle Felder ausgefüllt wurden, da Auftraggeber
					// diese Daten alle in der Auswertung wünschen

					if (MainApp.isTestmodusAktiv()) {
						// im Testmodus die Möglichkeit hinzufügen, die Warnung zu ignorieren
						
						boolean resultIgnorieren = AlertMethoden.entscheidungViaDialogAbfragen(" --TESTMODUS-- Eingaben unvollständig",
								"--TESTMODUS-- Eingabe nicht vollständig. Wollen Sie trotzdem fortfahren ? ");

						if (resultIgnorieren) { 
							fragebogenHandling(logger);
						}
					} else {
						if (isDateCorrect())  {
							AlertMethoden.zeigeOKAlert(AlertType.WARNING, "Bitte alles ausfüllen", "Bitte füllen Sie alle Felder aus und legen Sie mindestens einen Referenten an. ");
						} else {
							AlertMethoden.zeigeOKAlert(AlertType.WARNING, "Bitte korrekt ausfüllen", "Bitte füllen Sie die Datumsfelder korrekt aus. ");
						}
					}
				}

			}

			/* Erstellen, Hochladen usw. eines Fragebogens */
			
			private void fragebogenHandling(Logger logger) {
				try {

					int umfrageID = new Random().nextInt();
				
					// Erstellen des Fragebogen-Files
					FragebogenEigenschaften eigenschaftenX = new FragebogenEigenschaften(controller, "Ungültiger Webpath");
					String fragebogenOutputPfad = erstelleFragebogenImLokalenRepo(eigenschaftenX, umfrageID);
					zeigeVorschauFragebogen(fragebogenOutputPfad); // Zeigt den Fragebogen im Browser
					
					String webpath = getFragebogenWebPath(fragebogenOutputPfad);

					boolean veroeffentlichen = AlertMethoden.entscheidungViaDialogAbfragenOnTop("Fragebogen veröffentlichen?", 
							"Fragebogen auf " + webpath + " veröffentlichen?", true /* Fenster oben halten */);
										
					if (veroeffentlichen) {
			
						try {
							boolean hochladen = true;
							if (MainApp.testmodusAktiv) {   // ermöglicht Unterdrücken des Hochladens
								hochladen = AlertMethoden.entscheidungViaDialogAbfragen("--Testmodus--", "Fragebogen hochladen?");
							}
							if (hochladen) {
								// Alert alert = AlertMethoden.zeigeOKAlertWarten(AlertType.CONFIRMATION, "Hochladen des Fragebogens", "Der Fragebogen wird hochgeladen...", false);
								
// ===============================================================================						
								
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
										try {
											Upload.getInstance().hochladen(fragebogenname.getText(), MainApp.getUserName()); // JGit lädt Datei hoch	
										} catch (GitAPIException | IOException e) {
											e.printStackTrace();
										}
										
									}});
														
// ===============================================================================
								/*
								Task task = new Task<Void>() {
								    @Override protected Void call() throws Exception {
								    	try {
											Upload.getInstance().hochladen(fragebogenname.getText(), MainApp.getUserName()); // JGit lädt Datei hoch	
										} catch (GitAPIException | IOException e) {
											e.printStackTrace();
										}
								    	updateMessage("fertig!");
								    	return null;
								    }
								};
								new Thread(task).start();
								while (!task.getMessage().equals("fertig!")) {
									System.out.println("ControllerFragebogenErstellen) Hochladen des Fragebogens nicht fertig!");
								}
								System.out.println("fertig!");
								*/
// ===============================================================================
								
							} else {
								logger.logWarning("Der Fragebogen wurde nicht hochgeladen.");		
							}

						} catch (Exception exc) {
							
							logger.logError(exc); // Hochladen hat nicht geklappt
							AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Probleme beim Hochladen", "Das Hochladen des Fragebogens hat nicht funktioniert. Probieren Sie es später nochmal.");
							return;

						}
						// 8.8.8.8 pingen
						// Überprüfen, ob Datei existiert (Error Code 404 möglicherweise nicht möglich,
						// da Github Pages trotzdem etwas anzeigt)
						// sehen, ob das erste div-element eine bestimmte komplizierte ID hat?
						// fx-thread nicht blockieren !!!
						// Abbrechen erlauben ?
						
						Dialog<ButtonType> dialog = new Dialog<>();			
						ButtonType cancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
						
						UploadController upload_controller = initHochladenFenster(dialog, cancel);
						dialog.getDialogPane().getButtonTypes().remove(cancel);
						upload_controller.setLink(webpath);
						upload_controller.link.setOnAction(y -> {
						    if(Desktop.isDesktopSupported())
						    {
						        try {
						            Desktop.getDesktop().browse(new URI(webpath));
						        } catch (IOException e1) {
						            e1.printStackTrace();
						        } catch (URISyntaxException e1) {
						            e1.printStackTrace();
						        }
						    }
						});

						zeigeStatusHochladen(dialog, cancel, upload_controller);

						// Abfragen, ob Fragebogen kopiert werden soll
						boolean resultKlonen = AlertMethoden.entscheidungViaDialogAbfragen(
								"Fragebogen kopieren?",
								"Neuen Fragebogen mit denselben Referenten anlegen?");
						
						// Fragebogen-Eigenschaften-Objekt erstellen
						FragebogenEigenschaften eigenschaften = new FragebogenEigenschaften(controller, webpath);
						
						// Auswertung-Tab erstellen
						mainapp.showTabAntwortenErfassen(eigenschaften, tab.getTabPane().getTabs().indexOf(tab), umfrageID);

						if (resultKlonen) {
							// Fragebogen klonen
							setName("Kopie von " + getName());
						} else {
							//Tab schließen
							mainapp.rootLayout.getTabs().remove(tab.getTabPane().getTabs().indexOf(tab));
						}
					} else {
						// nichts tun
					}

				} catch (IOException | URISyntaxException | GitAPIException e1) {
					// SO KRIEGT MAN DEN TYP DER EXCEPTION: MIT
					// GETCLASS.GETNAME!!!!!!!!!!!!!!!!!!!!!!
					logger.logError(e1);
					String errorMessage = "Etwas ist fehlgeschlagen. \nGeben Sie die Nachricht an die Administratoren weiter:\n"
									+ e1.getClass().getName() + " beim Preview-Alert. \n"
									+ "Interne Fehlermeldung: " + e1.getMessage();
					
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Fehler", errorMessage);
				}
			}

			private UploadController initHochladenFenster(Dialog<ButtonType> dialog, ButtonType cancel)
					throws IOException {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainApp.class.getResource("upload.fxml"));
				DialogPane grid = (DialogPane) loader.load();
				dialog.setDialogPane(grid);

				dialog.getDialogPane().getButtonTypes().add(cancel);

				dialog.initOwner(mainapp.primaryStage);
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.setTitle("Hochladen");
				UploadController upload_controller = loader.getController();
				return upload_controller;
			}

			private void zeigeStatusHochladen(Dialog<ButtonType> dialog, ButtonType cancel,
					UploadController upload_controller) {
				
				
				// Entfernen des Cancel Buttons
				dialog.getDialogPane().getButtonTypes().remove(cancel);
				
				ButtonType next = new ButtonType("Weiter", ButtonData.NEXT_FORWARD);
				dialog.getDialogPane().getButtonTypes().add(next);

				upload_controller.upload_pending.setText("Hochladen erfolgreich!");
				upload_controller.progress.setProgress(1);
				Optional<ButtonType> result2 = dialog.showAndWait(); // Buttons abfragen!!!!
				
				Logger.getLogger().logInfo("result2 = " + result2.toString());
				// w�re praktisch, den Link noch woanders anzuzeigen
			}


			private String getFragebogenWebPath(String fragebogenOutputPfad) {
				// Entfernen von .html, weil es manchmal auf github.io zu Problemen führt
				int indexA = fragebogenOutputPfad.indexOf("gfigithubaccess");
				int indexB = fragebogenOutputPfad.indexOf(".html");
				String webpath = "https://" + fragebogenOutputPfad.substring(indexA, indexB);
				webpath = webpath.replace('\\', '/');
				Logger.getLogger().logInfo(webpath);
				return webpath;
			}

			private void zeigeVorschauFragebogen(String fragebogenOutputPfad)
					throws IOException, URISyntaxException, MalformedURLException {
					Desktop.getDesktop().browse(new URI("file",fragebogenOutputPfad.replace('\\', '/'),""));
			}

			private String erstelleFragebogenImLokalenRepo(FragebogenEigenschaften eigenschaften, int umfrageID)
					throws InvalidRemoteException, TransportException, GitAPIException, IOException {
				String seminarleiterName = MainApp.getUserName();
				String fragebogenTemplateDirectory = Upload.getInstance().getTemplateDirectory()
						+ "template_fragebogen.html";
				String fragebogenOutputPfad = Upload.getInstance().getFragebogenPfad(seminarleiterName,
						fragebogenname.getText());

				// Schreibt den Fragebogen in das Repository
				
				new HtmlCreator(getReferentenNamen(), fragebogenTemplateDirectory,
						fragebogenOutputPfad,  umfrageID, 
						eigenschaften.von_datum,
					 eigenschaften.bis_datum, eigenschaften.auftrags_nummer, 
					 eigenschaften.seminarleiter_name,
    				 eigenschaften.ausstellungs_datum).createHtml();
				
				return fragebogenOutputPfad;
			}

			private boolean allValuesEntered() {
				// das Feld mit dem Datum der Umfrage wird nicht abgefragt, da es vorausgefuellt
				// ist
				boolean fragebogennameEntered = fragebogenname.getText().matches(".*\\S+.*");
				boolean auftragsnummerEntered = auftragsnummer_textfield.getText().matches(".*\\S+.*");
				int anzahl_referenten = getReferentenNamen().size();
				
				boolean vonEntered   =   !von_Datum.getEditor().getText().isBlank();
				boolean bisEntered   =   !bis_Datum.getEditor().getText().isBlank();
				
				if (fragebogennameEntered && auftragsnummerEntered
						&& anzahl_referenten > 0 && vonEntered && bisEntered) {
					return true;
				} else {
					return false;
				}
			}
			
			private boolean isDateCorrect() {
				
				Datum vonDatum = Datum.parse(extractTextFromDatepicker(von_Datum));
				Datum bisDatum = Datum.parse(extractTextFromDatepicker(bis_Datum));
				
				boolean datumsGueltig = (vonDatum != null) && (bisDatum != null)
						&& vonDatum.compareTo(bisDatum) < Datum.GROESSER;
				
				return datumsGueltig;
			}
			
			private String extractTextFromDatepicker(DatePicker picker) {
				return picker.getEditor().getText();
			}
		});
	}

}
