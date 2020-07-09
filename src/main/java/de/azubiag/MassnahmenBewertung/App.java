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
       //  launch();
    	
    	String auswertung = berechneAuswertung();
    	
    	System.out.println(auswertung);
    	
    }
    
    static String berechneAuswertung() {
    	
    	
    	String ergebnis = "";
    	
    	// Benedikt
    	// Input von verschlüsselten Strings  -> Branch von Benedikt
    	// entschlüsseln der String
    	
    	
    	// Michael
    	// Input ein Array von *unverschlüsselten* Strings
    	// -> AzubiAntwort-Objekte
    	
    	
    	
    	//Input: ArrayList AzubiAntworten
    	getAuswertungenAllerReferenten(); 
    	
    	List<AuswertungReferent> auswertungenReferenten = new ArrayList<>(); 
    	
    	
    	
    	//-> Auswerten der AzubiAntwort-Objekte
    	//  Louisa
    	//  Denis
    	
    	
    	// -> Ausgeben der Ergebnisse

    	// Ouput
    	
    	return ergebnis;
    	
    }

	public static List<AuswertungReferent> getAuswertungenAllerReferenten(ArrayList<AzubiAntwort> azubiAntworten) {
	List<AuswertungReferent> auswertungenAllerReferenten = new ArrayList<>(); 	
	
	List <ArrayList<BewertungReferent>> sortierteBewertungen = new ArrayList<>(); 
	
	
	int anzahlReferenten = azubiAntworten.get(0).referenten.size(); 
	
	for (int positionRefImFragebogen = 0; positionRefImFragebogen < anzahlReferenten; positionRefImFragebogen++) {
		
		ArrayList<BewertungReferent> bewertungenEinesReferenten  = new ArrayList<BewertungReferent>();
		
		for (int j = 0; j <azubiAntworten.size(); j++) {
			bewertungenEinesReferenten.add(azubiAntworten.get(j).referenten.get(positionRefImFragebogen));
		}
		
		auswertungenAllerReferenten.add(new AuswertungReferent(bewertungenEinesReferenten)); 
	}
	return auswertungenAllerReferenten; 
		
	}

}