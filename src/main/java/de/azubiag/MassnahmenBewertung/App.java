package de.azubiag.MassnahmenBewertung;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;
import de.azubiag.MassnahmenBewertung.upload.Upload;

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
		
		try {
			// Sicherstellen, dass wir eine Instanz erzeugen können und damit garantiert ist, dass das Repo lokal vorliert
			Upload.getInstance(); 
			
			// Durch den Pull wird sichergestellt, dass Änderungen im Fragebogen-Template beim nächsten Programmstart ausgeführt werden.
			// TODO: Denis: Dafür sorgen, dass ein Pull ausgeführt. 
			
		} catch (GitAPIException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// TODO: Remove throw Excpetions, wenn Benedikt das Exception-Handling
	// korrigiert hat

	static String[] cipherStrings = {
			"AU2FsdGVkX190o2DknyWMHa9XGq2JKwmap0zvJd0c39jbzkEqRC44FQ34TGGs2wETooOKw1nIwnbk31R+o4Tg3k/h5fa/BAeZJeC9Y1DHqECvZWlljvv5lOHJhD1L4/TnZb3Ks2cMJE1eky+8BchhrkvMB1zAqhOVb8vKw7ov3t23eEGm+o3/yT2V27EH+gizutaSy9BU/X/kXTVpncxi4hlCNXR8p0mJQRRj4oKZy+19jAGqYt+grJqUlTir+zXjs6jP+QjCPFhFdfQZQ2IYbA==",
			"AU2FsdGVkX184mE21H5Jv3ktZys9mIF5HfvrFw9Od8gTDZe5TljcoItmuKmY3MPVUO4043uRkyPQc+btvCxnob5aCq+O2PQok2nqwkZ37nny5Wp9dApfNG79dIweKjqWM38P6Qkmn+iJ0yKeCZrtHT4HnztL02JlE0nAxBtHS5Nf2gM42u6vZhseqn5E3o9opJg/hUYoSxUa6ZwhmFlb74g==",
			"AU2FsdGVkX18Hfx/uQqsOVkTmCzj89C+n7ChAgoh69rrPyJhNYzGdX79uOg0te+hcGHnzIgztVX06m1av5qoXiO7S9VI1Te7FE/93jg43FaNz3zIG4M/BkyRWbmCqLre4VR1eMHTyZrvyc6rWrc3eqHRCZRicDgdMLJzBCswPrx+S3hOAzURnrlCn84e7JaisYLuAl9Y5qRLOZHCtF6Hl3v3r+qc0Z+vjT5LpUlzkkto=" };

	static String berechneAuswertung() {

		String ergebnis = "";

		ArrayList<String> klarTextStrings = new ArrayList<String>();

		for (String s : cipherStrings) {

			String entschluesselt = Decrypt.decrypt_any_type(s);
			if (entschluesselt == null) {
				System.out.println(
						"Antwort-String fehlerhaft. Bitte überprüfen Sie Kopieren/Einfügen oder die Antwort des Azubis");
				continue;
			}
			klarTextStrings.add(entschluesselt);

		}

		ArrayList<AzubiAntwort> antworten = (ArrayList<AzubiAntwort>) AzubiAntwort
				.konvertiereStringsInAzubiAntworten(klarTextStrings);

		// -Auswerten der Masznahmenbezogenen Antworten
		AuswertungMassnahme auswertungMassnahme = AuswertungMassnahme.getAuswertungMassnahme(antworten);
		ergebnis += auswertungMassnahme.toString();

		// Auswertung der referentenbezogenen Antworten
		List<AuswertungReferent> auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antworten); // Louisa
		for (AuswertungReferent a : auswertungenReferenten) {
			ergebnis += a.toString() + "\n----------------------------------------\n";
		}

		return ergebnis;

	}

}