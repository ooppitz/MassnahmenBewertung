package de.azubiag.MassnahmenBewertung.tools;

import java.util.Optional;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertMethoden {

	/**
	 * Confirmation-Alert mit einem Ja- und einem Nein-Button anzeigen
	 * 
	 * @param dialogTitel Titel des Confirmation-Alert
	 * @param frage       Frage, die dem User im Confirmation-Alert gestellt werden
	 *                    soll
	 * 
	 * @return true, wenn "ja" geklickt wurde, false, wenn "nein" geklickt wurde
	 */
	public static boolean entscheidungViaDialogAbfragen(String dialogTitel, String frageText) {

		return entscheidungViaDialogAbfragenOnTop(dialogTitel, frageText, false);
	}

	/**
	 * Zeigt einen ja / nein Dialog.
	 * 
	 * @param dialogTitel
	 * @param frage
	 * @param onTop       wenn true, dann wird der Dialog immer über allen Fenstern
	 *                    gezeigt
	 * @return
	 */
	public static boolean entscheidungViaDialogAbfragenOnTop(String dialogTitel, String frageText, boolean onTop) {

		ButtonType buttonTypeYes = new ButtonType("Ja");
		ButtonType buttonTypeCancel = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(dialogTitel);
		alert.setHeaderText(frageText);

		if (onTop) {
			setAlertAlwaysOnTop(alert);
		}
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);
		Optional<ButtonType> buttonType = alert.showAndWait();

		return buttonType.get() == buttonTypeYes;

	}

	/**
	 * Zeige einfaches Alert-Fenster mit Ok-Button um das Fenster zu schließen.
	 * Geeignet fuer AlertType.WARNING, AlertType.ERROR, AlertType.INFORMATION. Fuer
	 * einen Confirmation-Alert nutzen Sie bitte die
	 * entscheidungUeberDialogAbfragen()-Methode
	 * 
	 * @param alertType
	 * @param title     Titel des Alert-Fensters
	 * @param text      Text, der in dem Alert-Fenster angezeigt werden soll
	 */
	public static Alert zeigeOKAlert(AlertType alertType, String title, String text) {
		return zeigeOKAlertWarten(alertType, title, text, true /* Warten auf Knopfdruck */);
	}

	/**
	 * Zeigt ein Alert-Fenster und erlaubt zu steuern, ob auf den Knopfdruck
	 * gewartet wird.
	 * 
	 * @param alertType
	 * @param title
	 * @param text
	 * @param warten    entscheidet, ob auf den Knopfdruck gewartet wird.
	 */
	public static Alert zeigeOKAlertWarten(AlertType alertType, String title, String text, boolean warten) {

		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(text);
		ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(end);
		alert.initModality(Modality.APPLICATION_MODAL);
		if (warten == true) {
			alert.showAndWait();
		} else {
			alert.show();
		}

		return alert;

	}

	/**
	 * Zeige einen Alert mit Ja/Nein-Buttons
	 * 
	 * @param alertType beeinflusst den Icon, der angezeigt wird
	 * @param title     Titel des Alert-Fensters
	 * @param frage     Frage, die dem User im Confirmation-Alert gestellt werden
	 *                  soll
	 * @return 1 bei "Ja", 0 bei "Nein"
	 */
	public static boolean zeigeAlertJaNein(AlertType alertType, String titel, String frage) {
		Alert al = new Alert(alertType);
		ButtonType jaButton = new ButtonType("ja", ButtonData.YES);
		ButtonType neinButton = new ButtonType("nein", ButtonData.NO);
		al.getButtonTypes().setAll(jaButton, neinButton);
		al.setTitle(titel);
		al.setHeaderText(frage);
		Optional<ButtonType> opbt = al.showAndWait();
		if (opbt.get() == jaButton) {
			return true;
		} else  {
			return false;
		} 
	}

	public static final int JA = 1;
	public static final int NEIN = 0;
	public static final int CANCEL = -1;

	public static void zeigeOKAlertTextCopyAlwaysOnTop(AlertType alertType, String title, String headerText, String copyText) {
		GridPane gridPane = initTextArea(copyText);
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		ButtonType buttonTypeOK = new ButtonType("OK");
		alert.getButtonTypes().setAll(buttonTypeOK);
		alert.getDialogPane().setContent(gridPane);
		setAlertAlwaysOnTop(alert);
		alert.showAndWait();
	}

	private static void setAlertAlwaysOnTop(Alert alert) {
		Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
		alertStage.setAlwaysOnTop(true);
		
	}

	private static GridPane initTextArea(String text) {
		TextArea textArea = new TextArea(text);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		GridPane gridPane = new GridPane();
		gridPane.setMaxWidth(Double.MAX_VALUE);
		gridPane.add(textArea, 0, 0);
		return gridPane;
	}
}
