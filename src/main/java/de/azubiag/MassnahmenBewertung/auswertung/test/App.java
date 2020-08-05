package de.azubiag.MassnahmenBewertung.auswertung.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.crypto.Decrypt;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import de.azubiag.MassnahmenBewertung.UI.ControllerAuswertungAnzeigen;
import de.azubiag.MassnahmenBewertung.UI.MainApp;

/**
 * The User Interface
 * 
 * @author Filip Golanski
 */
public class App extends MainApp {

	public static void main(String[] args) {
		launch(args);
	}

	public void showAuswertungAnzeigen(String name, int index, List<AzubiAntwort> antwortListe) { // incomplete
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("ControllerAuswertungAnzeigen.fxml"));
			BorderPane z3 = (BorderPane) loader.load(); // !!
			Tab tab_z3 = new Tab();
			tab_z3.setContent(z3);
			tab_z3.setClosable(true);
			// tab_z3.setStyle("-fx-background-color:#DFD; -fx-border-color:#444");
			tab_z3.setText(name);
			System.out.println(index);
			rootLayout.getTabs().add(index + 1, tab_z3);
			// rootLayout.getTabs().remove(index);
			ControllerAuswertungAnzeigen controller = loader.getController();
			// System.out.println(controller);

			//controller.init(this, new FragebogenEigenschaften("test", "Max Mustermann", "auftragsnummer", new LocalDate(1,1,1), new LocalDate(1, 2, 3), new LocalDate(), "link"), antwortListe);
			controller.erzeugeDarstellung();

			//addDeleteToButton(controller.delete, rootLayout, tab_z3);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTabPane() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("tabpane.fxml"));
			rootLayout = (TabPane) loader.load();
			rootLayout.setPrefSize(800, 600);
			rootLayout.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.hide();
			primaryStage.setMaxHeight(600);
			primaryStage.setMaxWidth(800);

			ArrayList<AzubiAntwort> antwortListe = getTestDaten();
			showAuswertungAnzeigen("Testfragebogen XYZ", -1, antwortListe);

			// showFragebogenErstellen();

			// am Ende Plus Tab anzeigen
			showTabPlus();

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<AzubiAntwort> getTestDaten() {

		String[] verschluesselteAntworten = {
				"---AU2FsdGVkX187J7t88AiFwFQeFQ3PSAIfca4+FMiyTpIAAJnOBbJm69+6ZpYUsJPmrdvBNHzuxco92C9HIByAObiKPOwzmy1MtL2qY98/H/IQXUbyDTb37EBZED/Nmp289sdTfaQLkX5lVkIbEQNyp51bqhFKfILhEx1rBCaiB8A7gzVt73jhRbxH+C4WXqC2---",
				"---AU2FsdGVkX187J7t88AiFwFQeFQ3PSAIfca4+FMiyTpIAAJnOBbJm69+6ZpYUsJPmrdvBNHzuxco92C9HIByAObiKPOwzmy1MtL2qY98/H/IQXUbyDTb37EBZED/Nmp289sdTfaQLkX5lVkIbEQNyp51bqhFKfILhEx1rBCaiB8A7gzVt73jhRbxH+C4WXqC2---",
				"---AU2FsdGVkX187J7t88AiFwFQeFQ3PSAIfca4+FMiyTpIAAJnOBbJm69+6ZpYUsJPmrdvBNHzuxco92C9HIByAObiKPOwzmy1MtL2qY98/H/IQXUbyDTb37EBZED/Nmp289sdTfaQLkX5lVkIbEQNyp51bqhFKfILhEx1rBCaiB8A7gzVt73jhRbxH+C4WXqC2---"

		};

		ArrayList<AzubiAntwort> liste = new ArrayList<>();

		for (String v : verschluesselteAntworten) {
			String entschluesselteAntwort = Decrypt.decrypt_any_type(v);
			AzubiAntwort a = new AzubiAntwort(entschluesselteAntwort);
			System.out.println("VerifyID = " + a.umfrageID + "  "  + a);
			liste.add(a);
		}

		System.out.println(AuswertungMassnahme.getAuswertungMassnahme(liste));
		var referenten = AuswertungReferent.getAuswertungenAllerReferenten(liste);
		for (AuswertungReferent referent : referenten) {
			System.out.println(referent);
		}

		return liste;
	}
}
