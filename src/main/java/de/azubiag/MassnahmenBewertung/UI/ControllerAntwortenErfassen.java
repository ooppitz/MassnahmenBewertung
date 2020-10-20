package de.azubiag.MassnahmenBewertung.UI;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;
import de.azubiag.MassnahmenBewertung.tools.AlertMethoden;
import de.azubiag.MassnahmenBewertung.tools.AlsPDFSpeichern;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.upload.Upload;

/* Eingeben der Antworten */

public class ControllerAntwortenErfassen implements Serializable, Controller {

	private static final long serialVersionUID = -4954713836800270562L;

	/**
	 * Die folgenden Felder werden serilalisiert
	 */

	List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>(); // Serialisieren

	FragebogenEigenschaften eigenschaften; // Serialisieren

	int umfrageID; // Serialisieren

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
	private transient Hyperlink link_hypertext;

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

	transient private double prevHeight;


	public void init() {
		setHandlerRemoveAnswer(answ_del);
		setHandlerLinkCopyButton();
		setHandlerLinkAnklicken();
		readdNode(desc, 1, 0);
		readdNode(fragebogenName, 3, 0);
		readdNode(link_label, 1, 1);
		readdNode(link_hypertext, 2, 1);
		readdNode(link_kopieren, 5, 1);
		readdNode(auftragsnummer_label, 1, 2);
		readdNode(auftragsnummer_wert, 3, 2);
		readdNode(answ_del, 0, 3);
		readdNode(antwort_name, 1, 3);
		readdNode(antwort_text, 3, 3);
		link_hypertext.setText("Der Fragebogen wird vom Server verarbeitet. Der Link erscheint anschließend.");
		link_hypertext.setDisable(true);
		link_kopieren.setDisable(true);
		auftragsnummer_wert.setText(eigenschaften.auftrags_nummer);
		prevHeight = gridpane.getPrefHeight();
		// Der Task, der den Text in der UI ändert
		Task<Void> text_andern = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				link_hypertext.setText(eigenschaften.link+".html");
				link_hypertext.setDisable(false);
				link_kopieren.setDisable(false);
				return null;
			}
			
		};
		// Der Timer, der Timertasks als Daemon startet
		Timer timer = new Timer(true);
		/*  Der Timertask, der 10 Minuten lang nach dem link fragt		 
		 *  -!!!- Server cached seine Antworten, sodass vor dem abfragen gewartet werden muss,
		 *  weil sonst immer nur dieselbe Antwort zurück kommt
		 */
		TimerTask timertask_verzoegerung = new TimerTask() {
			@Override
			public void run() {
				Upload.istFragebogenOnline(6000000, eigenschaften.link, eigenschaften.umfrageID);
				eigenschaften.hochgeladen = true;
				Platform.runLater(text_andern);
			}
		};
		// Der Timertask, der 15 Sekunden lang nach dem link mit html-Endung fragt
		TimerTask timertask_sofort = new TimerTask() {
			@Override
			public void run() {
				boolean ist_fertig = Upload.istFragebogenOnline(15000, eigenschaften.link+".html", eigenschaften.umfrageID);
				if (ist_fertig) {
					eigenschaften.hochgeladen = true;
					Platform.runLater(text_andern);
				}
				else {
					timer.schedule(timertask_verzoegerung, 110000L);
				}
			}
		};
		
		if (eigenschaften.hochgeladen)
		{
			Platform.runLater(text_andern);
		}
		else
		{
			timer.schedule(timertask_sofort, 1L);
		}
		
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
		this.maintext.setText("Eingeben der Ergebnisse für Umfrage \"" + maintext + "\"");
	}

	public void setHandlerLinkAnklicken() {
		link_hypertext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				link_hypertext.setVisited(true);
				if(Desktop.isDesktopSupported())
				{
					try {
						Desktop.getDesktop().browse(new URI(link_hypertext.getText()));
					} catch (IOException | URISyntaxException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void setHandlerLinkCopyButton() {
		link_kopieren.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				Clipboard clippy = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(eigenschaften.link);
				clippy.setContent(content);
				link_kopieren.setStyle("-fx-graphic: url('http://files.softicons.com/download/toolbar-icons/funktional-icons-by-creative-freedom/png/24x24/Tick.png');");
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

				if (MainApp.isTestmodusAktiv()) {

					// Hinzufügen mehrere Antworten über einen String ist nur im Testmodus unterstützt,
					// denn es führt zu Fehlermeldungen, wenn die Strings keine --- Marker haben
					ArrayList<String> antworten = MultiAntwortParser.parse(verschluesselteAntwort);
					
					for(String a : antworten) {
						hinzufuegenAntwort(a);
					}
				} else {
					hinzufuegenAntwort(verschluesselteAntwort);
				}
				
				
				
			}

			private void hinzufuegenAntwort(String verschluesselteAntwort) {

				String entschluesselteAntwort = Decrypt.decrypt_any_type(verschluesselteAntwort);
				if (entschluesselteAntwort == null) {	
					Logger.getLogger().logError(new RuntimeException("Beim Eingeben eines Antwortstrings: Fehlerhafter String eingegeben!"));
					AlertMethoden.zeigeOKAlert(AlertType.ERROR, "Die Daten aus der Zwischenablage konnten nicht eingefügt werden.", "Die Daten aus der Zwischenablage konnten nicht eingefügt werden. \r\n"
																		+ "Stellen Sie sicher, dass die gesamte verschlüsselte Nachricht\r\n"
																		+"mit \"---\" beginnend und endend in der Zwischenablage ist!");
					return;
				} 

				AzubiAntwort antwort = new AzubiAntwort(entschluesselteAntwort,verschluesselteAntwort.replace("Ergebnis des Fragebogens: ", ""));  

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
				update_UI();
			}

		});

	}

	public void setHandlerRemoveAnswer(Button button) {

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				 Logger.getLogger().logInfo("Es soll eine Antwort entfernt werden.");

				// entfernen der Antwort aus der Antwortliste
				int index_dieser_antwort = GridPane.getRowIndex(button)-3;
				antwortListe.remove(index_dieser_antwort);
				update_UI();
			}
		});
	}

	public void addNext2ToButton(ControllerAntwortenErfassen controller) { // Auswertung
		next.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Next
				if ( ! antwortListe.isEmpty()) {

					boolean umfrageAuswerten = AlertMethoden.zeigeAlertJaNein(AlertType.WARNING, "Auswerten",
							"Eine Umfrage kann aus Gründen der Anonymität nur einmal ausgewertet werden. Jetzt auswerten?");
					
					if(umfrageAuswerten) {
						mainapp.deleteActions(tab, controller);
						
						MainApp.vonListeEntfernen(controller);// speichern bzw löschen, nachdem die Auswertung erstellt wurde
//						mainapp.showTabAuswertungAnzeigen(eigenschaften, tab.getTabPane().getTabs().indexOf(tab), antwortListe);
			
						String pdfOutputPfad = Upload.getProgrammDatenOrdner()
								+ "\\"+ eigenschaften.fragebogen_name
								+ "_" + controller.umfrageID
								+ ".pdf";
						
						File ergebnisPDFFile = new File(pdfOutputPfad);
						List<AuswertungReferent> auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antwortListe);
						AuswertungMassnahme auswertungMassnahme = AuswertungMassnahme.getGefilterteUndGemischteAuswertungenMassnahme(antwortListe);
						
						AlsPDFSpeichern.saveAsPDF(ergebnisPDFFile, eigenschaften, auswertungMassnahme,
								auswertungenReferenten);
					
					    if(Desktop.isDesktopSupported())
					    {
					        try {
					            Desktop.getDesktop().browse(ergebnisPDFFile.toURI());
					        } catch (IOException e1) {
					            e1.printStackTrace();
					        } 
					    }
					    AlertMethoden.zeigeOKAlertTextCopyAlwaysOnTop(AlertType.INFORMATION, "PDF-Datei Ergebnisse", 
								"Die Ergebnisse der Umfrage wurden als PDF gespeichert:\n",  pdfOutputPfad );
					    
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

		setName(alter_controller.eigenschaften.fragebogen_name);
		setMaintext(alter_controller.eigenschaften.fragebogen_name);
		setUmfrageID(alter_controller.getUmfrageID());
		eigenschaften = alter_controller.eigenschaften;
		antwortListe = alter_controller.antwortListe;

		init();

		update_UI();
	}


	/*
	 * Löst die Serialisierung aus und speichert die Daten, die zum Wiederherstellen
	 * der Ansicht nötig sind.
	 */


	static String saveFileName = "_tabs.ser";
	public static void serializeTabs() {

		Logger.getLogger().logInfo("ControllerAntwortenErfassen.serializeTabs() wurde aufgerufen");

		try {
			String ordner = Upload.getInstance().getSeminarleiterDirectory(MainApp.getUserName());
			FileOutputStream fos = new FileOutputStream(ordner + saveFileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(MainApp.listeControllerAntwortenErfassen);
			oos.close();
			fos.close();

		} catch (IOException | GitAPIException e) {
			Logger.getLogger().logError(e);
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
			Logger.getLogger().logError(e);
		}
		return controllerListe;

	}

	public void setUmfrageID(int umfrageID) {
		this.umfrageID = umfrageID;
	}

	public int getUmfrageID() {
		return umfrageID;
	}
	

	public void update_UI() {

		gridpane.getChildren().clear(); 		// entfernen des UIs aus der GridPane
		readdNode(desc, 1, 0);
		readdNode(fragebogenName, 3, 0);
		readdNode(link_label, 1, 1);
		readdNode(link_hypertext, 2, 1);
		readdNode(link_kopieren, 5, 1);
		readdNode(auftragsnummer_label, 1, 2);
		readdNode(auftragsnummer_wert, 3, 2);

		gridpane.setPrefHeight(prevHeight + 49*(antwortListe.size()-8));	// Magische Zahl 8, weil nach 8 Antworten die GridPane vergrößert werden muss, damit Abstände gleich bleiben
		
		for (int i = 0; i <= antwortListe.size(); i++) {
			
			int rowIndex = i + 3;
			
			final int IDX_BUTTON_DELETE = 0;
			final int IDX_LABEL_ANTWORT = 1;
			final int IDX_LABEL_ANTWORT_TEXT = 3;
			
			Label labelAntwort = new Label();
			labelAntwort.setText("  Verschlüsselte Antwort " + (i+1) + ":");
			labelAntwort.setFont(antwort_name.getFont());
			gridpane.add(labelAntwort, IDX_LABEL_ANTWORT, rowIndex, 2, 1);
		
			if (i==antwortListe.size())		// Button in letzter Zeile
			{
				gridpane.add(add /* BUTTON*/ , IDX_LABEL_ANTWORT_TEXT, rowIndex, 3, 1);
			}
			else							// Ausgabe für alle anderen Zeilen
			{
				Button buttonDelete = new Button();
				buttonDelete.setText("x");
				setHandlerRemoveAnswer(buttonDelete);
				gridpane.add(buttonDelete, IDX_BUTTON_DELETE, rowIndex, 1, 1);

				Label labelAntwortText = new Label(antwortListe.get(i).verschlüsselterString);
				labelAntwortText.setFont(antwort_text.getFont());
				gridpane.add(labelAntwortText, IDX_LABEL_ANTWORT_TEXT, rowIndex, 3, 1);
			}
		}
	}

}
	
		
