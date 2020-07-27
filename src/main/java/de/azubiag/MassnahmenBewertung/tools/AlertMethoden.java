	package de.azubiag.MassnahmenBewertung.tools;

	import java.util.Optional;

	import javafx.scene.control.Alert;
	import javafx.scene.control.ButtonType;
	import javafx.scene.control.Alert.AlertType;
	import javafx.scene.control.ButtonBar.ButtonData;
	
public class AlertMethoden {


		/**
		 * Confirmation-Alert mit einem Ja- und einem Nein-Button anzeigen
		 * @param dialogTitel Titel des Confirmation-Alert
		 * @param frage Frage, die dem User im Confirmation-Alert gestellt werden soll
		 * 
		 * @return true, wenn "ja" geklickt wurde, false, wenn "nein" geklickt wurde
		 */
		public static boolean entscheidungViaDialogAbfragen(String dialogTitel, String frage) {
			ButtonType buttonTypeYes = new ButtonType("Ja");
			ButtonType buttonTypeCancel = new ButtonType("Nein", ButtonData.CANCEL_CLOSE);
			Alert alertKlonen = new Alert(AlertType.CONFIRMATION);
			alertKlonen.setTitle(dialogTitel);
			alertKlonen.setHeaderText(frage);

			alertKlonen.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);
			Optional<ButtonType> buttonType = alertKlonen.showAndWait();
			
			return buttonType.get() == buttonTypeYes;
		}

		/**
		 * Zeige einfaches Alert-Fenster mit Ok-Button um das Fenster zu schließen. 
		 * Geeignet fuer AlertType.WARNING, AlertType.ERROR, AlertType.INFORMATION.
		 * Fuer einen Confirmation-Alert nutzen Sie bitte die entscheidungUeberDialogAbfragen()-Methode
		 * @param alertType
		 * @param title Titel des Alert-Fensters
		 * @param text Text, der in dem Alert-Fenster angezeigt werden soll
		 */
		public static void zeigeEinfachenAlert(AlertType alertType, String title, String text) {
			Alert error = new Alert(alertType);
			error.setTitle(title);
			error.setHeaderText(
					text);
			ButtonType end = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
			error.getButtonTypes().setAll(end);
			error.showAndWait();
		}
	}


