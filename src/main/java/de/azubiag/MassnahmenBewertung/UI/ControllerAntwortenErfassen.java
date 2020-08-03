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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.upload.Upload;

/* Eingeben der Antworten */

public class ControllerAntwortenErfassen implements Serializable {

	private static final long serialVersionUID = -4954713836800270562L;

	/**
	 * Die folgenden Felder werden serilalisiert
	 */

	List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>(); // Serialisieren

	FragebogenEigenschaften eigenschaften; // Serialisieren

	private int umfrageID; // Serialisieren

//	int anzahl_antworten; // Serialisieren

	transient Tab tab;

	@FXML
	transient Label desc;

	@FXML
	transient Label antwort_name;

	@FXML
	transient Label antwort_text;

	@FXML
	transient GridPane gridpane;

	@FXML
	private transient Label fragebogenName;

	@FXML
	private transient Label maintext;
	
	@FXML
	private transient Label link_label;
	
	@FXML
	private transient TextField link_textfield;
	
	@FXML
	private transient Button link_kopieren;
	
	@FXML
	private transient Label auftragsnummer_label;
	
	@FXML
	private transient Label auftragsnummer_wert;

	@FXML
	transient Button answ_del;

	@FXML
	public transient Button add;

	@FXML
	public transient Button next;

	@FXML
	public transient Button delete;

	transient private MainApp mainapp;
	

	public void init() {
		setHandlerRemoveAnswer(answ_del);
		setHandlerLinkCopyButton();
		readdNode(desc, 1, 0);
		readdNode(fragebogenName, 3, 0);
		readdNode(link_label, 1, 1);
		readdNode(link_textfield, 2, 1);
		readdNode(link_kopieren, 5, 1);
		readdNode(auftragsnummer_label, 1, 2);
		readdNode(auftragsnummer_wert, 3, 2);
		readdNode(answ_del, 0, 3);
		readdNode(antwort_name, 1, 3);
		readdNode(antwort_text, 3, 3);
		link_textfield.setText(eigenschaften.link);
		auftragsnummer_wert.setText(eigenschaften.auftrags_nummer);
	}

	public void setEigenschaft(FragebogenEigenschaften eigenschaft) {
		this.eigenschaften = eigenschaft;
	}

	public void readdNode(Node node, int col, int row) {
		gridpane.getChildren().remove(node);
		gridpane.add(node, col, row);
	}

	public void setMainApp(MainApp app) {
		mainapp = app;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getName() {
		return fragebogenName.getText();
	}

	public void setName(String name) {
		this.fragebogenName.setText(name);
	}

	public String getMaintext() {
		return maintext.getText();
	}

	public void setMaintext(String maintext) {
		this.maintext.setText("Eingeben der Ergebnisse für Fragebogen " + maintext);
	}
	
	public void setHandlerLinkCopyButton() {
		link_kopieren.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				Clipboard clippy = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(eigenschaften.link);
				clippy.setContent(content);
			}
		});
	}

	public void setHandlerAnswerButton() {
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				String verschluesselteAntwort = Clipboard.getSystemClipboard().getString();
				if (verschluesselteAntwort == null) {
					Logger.getLogger().logWarning("Zwischenablage leer beim Einkopieren der Antwortstrings");
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Ihre Zwischenablage ist leer!", "Ihre Zwischenablage ist leer!");
					return;
				}

				ArrayList<String> antworten = MultiAntwortParser.parse(verschluesselteAntwort);
				
				for(String a : antworten) {
					hinzufuegenAntwort(a);
				}
			}

			private void hinzufuegenAntwort(String verschluesselteAntwort) {
				
				String entschluesselteAntwort = Decrypt.decrypt_any_type(verschluesselteAntwort);
				if (entschluesselteAntwort == null) {	
					Logger.getLogger().logError("Beim Eingeben eines Antwortstrings: Fehlerhafter String eingegeben!");
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Die eingefügten Daten waren fehlerhaft!", "Die eingefügten Daten waren fehlerhaft!");
					return;
				} 

				AzubiAntwort antwort = new AzubiAntwort(entschluesselteAntwort,verschluesselteAntwort);  

				if (antwort.umfrageID != umfrageID) {
					Logger.getLogger().logWarning("Eingefügte Antwort gehört nicht zu diesem Fragebogen");
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Eingefügte Antwort gehört nicht zu diesem Fragebogen!", "Eingefügte Antwort gehört nicht zu diesem Fragebogen!");
					return;
				}

				for (AzubiAntwort azubiAntwort : antwortListe) {
					if (azubiAntwort.antwortID == antwort.antwortID)	{	
						Logger.getLogger().logWarning("Eingefügte Antwort ist bereits vorhanden!");
						AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Eingefügte Antwort ist bereits vorhanden!", "Eingefügte Antwort ist bereits vorhanden!");
						return;			
					}
				}


				antwortListe.add(antwort); // Antwort ist gültig und wird zur Liste hinzugefügt

				// Hinzufügen der ersten Antwort (in ein bestehendes Control)
				if (antwortListe.size() == 1) {		
					
					antwort_text.setText(verschluesselteAntwort);

				} else  {

					if (antwortListe.size() > 8) {
						gridpane.setPrefHeight(gridpane.getPrefHeight() + 49);
						gridpane.addRow(antwortListe.size());
						// Eigenschaften der neuen Row ändern, sodass sie genau so wie die vorherigen
						// aussieht
					}

					Button buttonDelete = new Button();
					buttonDelete.setText("x");
					setHandlerRemoveAnswer(buttonDelete);

					Label labelAntwort = new Label();
					labelAntwort.setText("  Verschlüsselte Antwort " + (antwortListe.size()) + ":");
					labelAntwort.setFont(antwort_name.getFont());

					Label labelAntwortText = new Label(verschluesselteAntwort);
					labelAntwortText.setFont(antwort_text.getFont());

					int rowIndex = antwortListe.size()+2;
					int buttonDeleteIdx = 0;
					int labelAntwortIdx = 1;
					int labelAntwortTextIdx = 3;

					//                  node,  ColIndex         RowIndex, colSpan RowSpan
					gridpane.add(buttonDelete, buttonDeleteIdx, rowIndex, 1, 1);
					gridpane.add(labelAntwort, labelAntwortIdx, rowIndex, 2, 1);
					gridpane.add(labelAntwortText, labelAntwortTextIdx, rowIndex, 3, 1);

					int letzteRow = antwortListe.size()+2;
					for (int i = 1; i < letzteRow; i++) {
						Node deleteButtonNode  = GridPaneCustom.getElemByRowAndColumn(gridpane, i, buttonDeleteIdx);
						if (deleteButtonNode!=null)	{
							((Button)deleteButtonNode).setDisable(false);
						}
					}
					Node letzterDeleteButton = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, buttonDeleteIdx);
					((Button)letzterDeleteButton).setDisable(true);
				}
			}

		});

	}

	public void setHandlerRemoveAnswer(Button button) {

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Logger logger = Logger.getLogger();
				logger.logInfo("Es soll eine Antwort entfernt werden.");
				/*
				 * Ablauf: - letzter Button wird entfernt - letzter Label wird entfernt -
				 * TextField neben diesem Button wird entfernt - alle Textfields darunter werden
				 * nach oben verschoben - Antwort wird aus der Antwortliste gestrichen -
				 * möglicherweise wird das Gridpane um 49 Höhe kleiner - anzahlAntworten wird
				 * dekrementiert
				 */

				int letzteRow = antwortListe.size()+2;
				logger.logInfo("letzte Reihe: " + letzteRow);
				Button letzterButton = (Button) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
				Label letzterLabel = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 1);
				Label labelnebendiesembutton = (Label) GridPaneCustom.getElemByRowAndColumn(gridpane,
						GridPane.getRowIndex(button), 3);

				gridpane.getChildren().removeAll(letzterButton, letzterLabel, labelnebendiesembutton);

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
					logger.logInfo("temp node: " + temp);
					if (temp != null) {
						GridPaneCustom.moveElemByRowAndColumn(temp, gridpane, -1, 0);
					}
				}
				
				// entfernen der Antwort aus der Antwortliste
				int index_dieser_antwort = GridPane.getRowIndex(button)-3;
				antwortListe.remove(index_dieser_antwort);
			}
		});
	}

	public void addNext2ToButton(ControllerAntwortenErfassen controller) { // Auswertung
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Next
				if ( ! antwortListe.isEmpty()) {

					int userAntwort = AlertMethoden.zeigeAlertJaNeinAbbrechen(AlertType.WARNING, "Auswerten",
							"Ein Fragebogen kann nur 1 mal ausgewertet werden. Jetzt auswerten?");
					if(userAntwort == AlertMethoden.JA) {
						MainApp.vonListeEntfernen(controller);// speichern bzw löschen, nachdem die Auswertung erstellt wurde
						mainapp.showTabAuswertungAnzeigen(eigenschaften, tab.getTabPane().getTabs().indexOf(tab), antwortListe);
					}
				}
			}
		});
	}

	public final void writeObject(ObjectOutputStream os) {

		try {
			//			os.defaultWriteObject();
			os.writeObject(antwortListe);
			os.writeObject(eigenschaften);
			os.writeInt(umfrageID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void readObject(ObjectInputStream is) {

		try {
			//			is.defaultReadObject();
			antwortListe = (List<AzubiAntwort>) is.readObject(); // unchecked cast
			eigenschaften = (FragebogenEigenschaften) is.readObject();
			umfrageID = is.readInt();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}


	public void tab_wiederherstellen(ControllerAntwortenErfassen alter_controller) { // Labels wieder richtig einstellen
		// usw

//		anzahl_antworten = alter_controller.antwortListe.size(); // kann michael nach seinem refactoring entfernen
		setName(alter_controller.eigenschaften.fragebogen_name);
		setMaintext(alter_controller.eigenschaften.fragebogen_name);
		setUmfrageID(alter_controller.getUmfrageID());
		eigenschaften = alter_controller.eigenschaften;
		antwortListe = alter_controller.antwortListe;
		// wahrscheinlich noch weiteres
		init();

		// Schleife, um Antworten hinzuzufügen

		for (int bisherige_antworten = 0; bisherige_antworten < alter_controller.antwortListe
				.size(); bisherige_antworten++) {
			if (bisherige_antworten > 0) {
				Button deleteButton = new Button();
				deleteButton.setText("x");
				setHandlerRemoveAnswer(deleteButton);

				Label tempLabel = new Label();
				tempLabel.setText("  Verschlüsselte Antwort " + (bisherige_antworten + 1) + ":");
				tempLabel.setFont(antwort_name.getFont());

				gridpane.add(deleteButton, 0, bisherige_antworten + 1, 1, 1);
				gridpane.add(tempLabel, 1, bisherige_antworten + 1, 2, 1);
			}

			Label tempLabel2 = new Label(alter_controller.antwortListe.get(bisherige_antworten).verschlüsselterString);
			tempLabel2.setFont(antwort_text.getFont());

			gridpane.add(tempLabel2, 3, bisherige_antworten + 1, 3, 1);

			int letzteRow = bisherige_antworten + 1;
			for (int i = 1; i < letzteRow; i++) {
				Node temp3 = GridPaneCustom.getElemByRowAndColumn(gridpane, i, 0);
				if (temp3 != null) {
					((Button) temp3).setDisable(false);
				}
			}
			Node temp4 = GridPaneCustom.getElemByRowAndColumn(gridpane, letzteRow, 0);
			((Button) temp4).setDisable(true);
		}
	}


	/*
	 * Löst die Serialisierung aus und speichert die Daten, die zum Wiederherstellen
	 * der Ansicht nötig sind.
	 */

	
	static String saveFileName = "_save.txt";
	public static void speichern() {

		System.out.println("Speichern wurde aufgerufen!");

		try {
			String ordner = Upload.getInstance().getSeminarleiterDirectory(MainApp.getUserName());
			FileOutputStream fos = new FileOutputStream(ordner + saveFileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(MainApp.listeControllerAntwortenErfassen);
			oos.close();
			fos.close();

		} catch (IOException | GitAPIException e) {
			Logger l = new Logger();
			l.logError(e);
		}
	}

	public static ArrayList<ControllerAntwortenErfassen> laden() {

		ArrayList<ControllerAntwortenErfassen> controllerListe = null;
		try {
			String ordner = Upload.getInstance().getSeminarleiterDirectory(MainApp.getUserName());
			FileInputStream fis = new FileInputStream(ordner + saveFileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			controllerListe = (ArrayList<ControllerAntwortenErfassen>) ois.readObject(); // unchecked cast
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException | GitAPIException e) {
			Logger l = new Logger();
			l.logError(e);
		}
		return controllerListe;

	}

	public void setUmfrageID(int umfrageID) {
		this.umfrageID = umfrageID;
	}

	public int getUmfrageID() {
		return umfrageID;
	}

}
