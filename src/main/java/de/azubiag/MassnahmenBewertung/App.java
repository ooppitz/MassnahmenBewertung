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
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
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

	// TODO: Remove throw Excpetions, wenn Benedikt das Exception-Handling
	// korrigiert hat

	static String berechneAuswertung() {

		String ergebnis = "";

		// Benedikt
		// Input von verschlüsselten Strings -> Branch von Benedikt
		// entschlüsseln der String

		
		ArrayList<String> klarTextStrings = new ArrayList<String>();
		
		String[] klarText = {
				"3|1|Alles war toll|4|Das war super toll|Es war klasse!|Robert Hackfuß|4|4|4|4|0|Er macht mir Angst!|Franz Karrenschlepper|2|0|0|0|4||",
				"4|4||0|Gr8 b8 m8 r8 8/8|Werf Sie raus!!!|Robert Hackfuß|1|2|1|0|3||Franz Karrenschlepper|0|1|1|0|0|Er hätte beim Karrenschleppen bleiben sollen.",
				"0|4|Nö|3||Lass mich in Ruhe|Robert Hackfuß|3|1|2|2|0|Warum trägt er immer ein Hackebeil?|Franz Karrenschlepper|3|1|0|0|3|Seine Augen sind immer weit offen.Das ist unheimlich!|" };

		
		String[] cipherString = {
				"AU2FsdGVkX190o2DknyWMHa9XGq2JKwmap0zvJd0c39jbzkEqRC44FQ34TGGs2wETooOKw1nIwnbk31R+o4Tg3k/h5fa/BAeZJeC9Y1DHqECvZWlljvv5lOHJhD1L4/TnZb3Ks2cMJE1eky+8BchhrkvMB1zAqhOVb8vKw7ov3t23eEGm+o3/yT2V27EH+gizutaSy9BU/X/kXTVpncxi4hlCNXR8p0mJQRRj4oKZy+19jAGqYt+grJqUlTir+zXjs6jP+QjCPFhFdfQZQ2IYbA==",
				"AU2FsdGVkX184mE21H5Jv3ktZys9mIF5HfvrFw9Od8gTDZe5TljcoItmuKmY3MPVUO4043uRkyPQc+btvCxnob5aCq+O2PQok2nqwkZ37nny5Wp9dApfNG79dIweKjqWM38P6Qkmn+iJ0yKeCZrtHT4HnztL02JlE0nAxBtHS5Nf2gM42u6vZhseqn5E3o9opJg/hUYoSxUa6ZwhmFlb74g==",
				"AU2FsdGVkX18Hfx/uQqsOVkTmCzj89C+n7ChAgoh69rrPyJhNYzGdX79uOg0te+hcGHnzIgztVX06m1av5qoXiO7S9VI1Te7FE/93jg43FaNz3zIG4M/BkyRWbmCqLre4VR1eMHTyZrvyc6rWrc3eqHRCZRicDgdMLJzBCswPrx+S3hOAzURnrlCn84e7JaisYLuAl9Y5qRLOZHCtF6Hl3v3r+qc0Z+vjT5LpUlzkkto=" };

		for (String s : cipherString) {

			try {
				
				klarTextStrings.add(Decrypt.decrypt_any_type(s));
				
			} catch (Exception e) {
				e.printStackTrace(); // TODO : Entfernen, wenn Benedikt das Exception-Handling für den Crypto-Code
										// fertig hat
				System.exit(0);
			}
		}
		
		// klarTextStrings.addAll(List.of(klarText));

		ArrayList<AzubiAntwort> antworten = (ArrayList<AzubiAntwort>) AzubiAntwort
				.konvertiereStringsInAzubiAntworten(klarTextStrings); // TODO: Wait for Input from Michael TestFeeder2.
																		// ;

		// -> Auswerten der AzubiAntwort-Objekte

		List<AuswertungReferent> auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antworten); // Louisa
		// TODO: Code einfügen von Denis
		for (AuswertungReferent a : auswertungenReferenten) {

			ergebnis += a.toString() + "\n----------------------------------------\n";

		}

		// -> Ausgeben der Ergebnisse

		return ergebnis;

	}

}