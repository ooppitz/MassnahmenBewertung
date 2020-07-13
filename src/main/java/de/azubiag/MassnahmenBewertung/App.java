package de.azubiag.MassnahmenBewertung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException {
		scene = new Scene(loadFXML("primary"), 640, 480);
		stage.setScene(scene);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		// launch();

		String auswertung = berechneAuswertung();

		System.out.println(auswertung);

	}

	static String berechneAuswertung() {

		String ergebnis = "";

		// Benedikt
		// Input von verschlüsselten Strings -> Branch von Benedikt
		// entschlüsseln der String

		
		List<String> klarTextStrings;
		
		// AzubiAntwort.konvertiereStringsInAzubiAntworten(klarTextStrings);
	
	    List<AzubiAntwort> antworten = null; // TODO: Wait for Input from Michael TestFeeder2. ; 

	    
	    
		// Input: ArrayList AzubiAntworten

		
		
		List<AuswertungReferent> auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(new ArrayList<AzubiAntwort>());

		// -> Auswerten der AzubiAntwort-Objekte
		// Louisa
		// Denis

		// -> Ausgeben der Ergebnisse

		// Ouput

		return ergebnis;

	}

	

}