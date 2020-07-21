package de.azubiag.MassnahmenBewertung.UI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.tools.Logger;

/* Eingeben der Antworten */

public class ControllerAntwortenErfassen {


	public List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
	Tab tab;

	int anzahl_antworten;

	@FXML
	Label desc;
	
	@FXML
	Label antwort_name;

	@FXML
	Label antwort_text;

	@FXML
	GridPane gridpane;

	@FXML
	private Label name;

	@FXML
	private Label maintext;
	
	@FXML
	Button answ_del;

	@FXML
	public Button add;

	@FXML
	public Button next;

	@FXML
	public Button delete;

	private MainApp mainapp;
	
	public void init() {
		removeAnswer(answ_del);
		readdNode(desc, 1, 0);
		readdNode(name, 3, 0);
		readdNode(answ_del, 0, 1);
		readdNode(antwort_name, 1, 1);
		readdNode(antwort_text, 3, 1);
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
		return name.getText();
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public String getMaintext() {
		return maintext.getText();
	}

	public void setMaintext(String maintext) {
		this.maintext.setText("Eingeben der Ergebnisse für Fragebogen "+maintext);
	}

	public void addAnswerToButton() {
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// Ergebnis von der Zwischenablage kopieren
				Clipboard clipboard = Clipboard.getSystemClipboard();

				// String muss dekodiert und überprüft werden

				String verschluesselteAntwort = clipboard.getString();

				if (verschluesselteAntwort == null)
				{
					Logger logger = Logger.getLogger();
					logger.logWarning("Zwischenablage leer beim Einkopieren der Antwortstrings");
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Ihre Zwischenablage ist leer!");
					error.setHeaderText("Ihre Zwischenablage ist leer!");
					ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
					error.getButtonTypes().setAll(end);
					error.show();
					return;
				}
				
				String entschluesselteAntwort = Decrypt.decrypt_any_type(verschluesselteAntwort);

				if (entschluesselteAntwort == null) {
					Logger logger = Logger.getLogger();
					logger.logError("Beim Eingaben eines Antwortstrings: Fehlerhafter String eingegeben!");
					Alert error = new Alert(AlertType.ERROR);
					error.setTitle("Die eingefügten Daten waren fehlerhaft!");
					error.setHeaderText("Die eingefügten Daten waren fehlerhaft!");
					ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
					error.getButtonTypes().setAll(end);
					error.show();
					return;

				} else {

					System.out.println(
							"Verschlüsselt: " + verschluesselteAntwort + " Entschlüsselt: " + entschluesselteAntwort);

					AzubiAntwort antwort = new AzubiAntwort(entschluesselteAntwort);

					antwortListe.add(antwort);
				}

				// TODO: In den else-Branch verschieben...

				// wenn richtiger String, dann hier weiter
				if (anzahl_antworten == 0) {
					antwort_text.setText(clipboard.getString());
					anzahl_antworten++;
				} else if (anzahl_antworten > 0) {
					if (anzahl_antworten > 9) {
						gridpane.setPrefHeight(gridpane.getPrefHeight() + 49);
						gridpane.addRow(anzahl_antworten + 1);
						// Eigenschaften der neuen Row ändern, sodass sie genau so wie die vorherigen
						// aussieht
					}
					Button del = new Button();
					del.setText("x");
					
					Label temp = new Label();
					temp.setText("  Verschlüsselte Antwort ");
					temp.setText(temp.getText() + (anzahl_antworten + 1) + ":");
					temp.setFont(antwort_name.getFont());

					Label temp2 = new Label(clipboard.getString());
					temp2.setFont(antwort_text.getFont());
					gridpane.add(temp, 0, anzahl_antworten + 1, 0, 1);
					gridpane.add(temp, 1, anzahl_antworten + 1, 2, 1);
					gridpane.add(temp2, 3, anzahl_antworten + 1, 3, 1);
					anzahl_antworten++;
				}
			}
		});
	}

	public void removeAnswer(Button button) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Logger logger = Logger.getLogger();
				logger.logInfo("Es soll eine Antwort entfernt werden.");
				/*	Ablauf:
				 * - letzter Button wird entfernt
				 * - letzter Label wird entfernt
				 * - TextField neben diesem Button wird entfernt
				 * - alle Textfields darunter werden nach oben verschoben
				 * - Antwort wird aus der Antwortliste gestrichen
				 * - möglicherweise wird das Gridpane um 49 Höhe kleiner		
				 * - anzahlAntworten wird dekrementiert
				 */
				
				int letzteRow = anzahl_antworten+1;
				System.out.println("letzte Reihe:\t"+letzteRow);
				Button letzterButton = (Button) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
				Label letzterLabel = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 1);
				Label labelnebendiesembutton = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane, GridPane.getRowIndex(button), 3);

				gridpane.getChildren().removeAll(letzterButton, letzterLabel, labelnebendiesembutton);

				for (int i = GridPane.getRowIndex(button)+1; i <= letzteRow; i++) {

					Node temp = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 3);
					logger.logInfo("temp node: "+temp);
					if (temp!=null)
					{
						GridPaneCustom.moveElemByRowAndColumn(temp, gridpane, -1, 0);
					}
				}

				anzahl_antworten--;
			}
		});
	}

	public void addNext2ToButton() { // Auswertung
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Next
				// controller.antwortListe an die Auswertung schicken
				for (AzubiAntwort azubiAntwort : antwortListe) {
					// TODO entfernen
					System.out.println(azubiAntwort.toString());
				}

				// Auswertung zurückbekommen
				mainapp.showAuswertungAnzeigen(name.getText(), tab.getTabPane().getTabs().indexOf(tab));
			}
		});
	}
}
