package de.azubiag.MassnahmenBewertung.UI;

import java.util.ArrayList;
import java.util.List;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

/* Ausgabe der Auswertung */

public class ControllerAuswertungAnzeigen {

	public List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
	List<AuswertungReferent> auswertungenReferenten;
	AuswertungMassnahme auswertungMassnahme;

	int zeile;

	Tab tab;

	@FXML
	public Label ueberschrift;

	@FXML
	public Button save;

	@FXML
	public Button delete;

	@FXML
	GridPane grid;

	private MainApp mainapp;

	public void setMainApp (MainApp app){
		mainapp = app;
	}

	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String getName() {
		return ueberschrift.getText();
	}

	public void setName(String name) {
		this.ueberschrift.setText("Ergebnisse für den Fragebogen "+name);
	}

	public void init() {

		final List<BewertungMassnahme> bewertungListe = new ArrayList<BewertungMassnahme>();

		for (AzubiAntwort azubiAntwort : antwortListe) {
			bewertungListe.add(azubiAntwort.massnahme);
		}
		
		auswertungMassnahme = new AuswertungMassnahme(bewertungListe);
		auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antwortListe);
		
		zeile = 0;
		verlauf();
		betreuung();
		bewertung();
		bemerkungen();

	}

	public void verlauf() {

		Label ueberschrift_verlauf = new Label("1. Maßnahmeverlauf:");
		Label u__2 = new Label("-2");
		Label u__1 = new Label("-1");
		Label u_0 = new Label(" 0");
		Label u_1 = new Label("+1");
		Label u_2 = new Label("+2");
		Label u_d = new Label("Ø");

		grid.add(ueberschrift_verlauf, 0, zeile);
		grid.add(u__2, 1, zeile);
		grid.add(u__1, 2, zeile);
		grid.add(u_0, 3, zeile);
		grid.add(u_1, 4, zeile);
		grid.add(u_2, 5, zeile);
		grid.add(u_d, 6, zeile);
		zeile ++;

		Label organisation_frage = new Label("Wie empfinden die Teilnehmer die Organisation des Seminars?");
		Label o__2 = new Label(Integer.toString(auswertungMassnahme.pktvertOrg[0]));
		Label o__1 = new Label(Integer.toString(auswertungMassnahme.pktvertOrg[1]));
		Label o_0 = new Label(Integer.toString(auswertungMassnahme.pktvertOrg[2]));
		Label o_1 = new Label(Integer.toString(auswertungMassnahme.pktvertOrg[3]));
		Label o_2 = new Label(Integer.toString(auswertungMassnahme.pktvertOrg[4]));
		Label o_d = new Label(Double.toString(auswertungMassnahme.durchschnOrg));

		grid.add(organisation_frage, 0, zeile);
		grid.add(o__2, 1, zeile);
		grid.add(o__1, 2, zeile);
		grid.add(o_0, 3, zeile);
		grid.add(o_1, 4, zeile);
		grid.add(o_2, 5, zeile);
		grid.add(o_d, 6, zeile);
		zeile ++;

		Label verlauf_frage = new Label("Wie empfinden die Teilnehmer den Verlauf des Seminars?");
		Label v__2 = new Label(Integer.toString(auswertungMassnahme.pktvertVerl[0]));
		Label v__1 = new Label(Integer.toString(auswertungMassnahme.pktvertVerl[1]));
		Label v_0 = new Label(Integer.toString(auswertungMassnahme.pktvertVerl[2]));
		Label v_1 = new Label(Integer.toString(auswertungMassnahme.pktvertVerl[3]));
		Label v_2 = new Label(Integer.toString(auswertungMassnahme.pktvertVerl[4]));
		Label v_d = new Label(Double.toString(auswertungMassnahme.durchschnVerl));

		grid.add(verlauf_frage, 0, zeile);
		grid.add(v__2, 1, zeile);
		grid.add(v__1, 2, zeile);
		grid.add(v_0, 3, zeile);
		grid.add(v_1, 4, zeile);
		grid.add(v_2, 5, zeile);
		grid.add(v_d, 6, zeile);
		zeile ++;

	}

	public void betreuung() {

		Label ueberschrift_betreuung = new Label("2. Maßnahmebetreuung:");
		Label u__2 = new Label("-2");
		Label u__1 = new Label("-1");
		Label u_0 = new Label(" 0");
		Label u_1 = new Label("+1");
		Label u_2 = new Label("+2");
		Label u_d = new Label("Ø");

		grid.add(ueberschrift_betreuung, 0, zeile);
		grid.add(u__2, 1, zeile);
		grid.add(u__1, 2, zeile);
		grid.add(u_0, 3, zeile);
		grid.add(u_1, 4, zeile);
		grid.add(u_2, 5, zeile);
		grid.add(u_d, 6, zeile);
		zeile ++;

		Label betreuung_frage = new Label("Wie zufrieden sind die Teilnehmer mit der Betreuung des BFZ?");
		Label b__2 = new Label(Integer.toString(auswertungMassnahme.pktvertBetrng[0]));
		Label b__1 = new Label(Integer.toString(auswertungMassnahme.pktvertBetrng[1]));
		Label b_0 = new Label(Integer.toString(auswertungMassnahme.pktvertBetrng[2]));
		Label b_1 = new Label(Integer.toString(auswertungMassnahme.pktvertBetrng[3]));
		Label b_2 = new Label(Integer.toString(auswertungMassnahme.pktvertBetrng[4]));
		Label b_d = new Label(Double.toString(auswertungMassnahme.durchschnBetrng));

		grid.add(betreuung_frage, 0, zeile);
		grid.add(b__2, 1, zeile);
		grid.add(b__1, 2, zeile);
		grid.add(b_0, 3, zeile);
		grid.add(b_1, 4, zeile);
		grid.add(b_2, 5, zeile);
		grid.add(b_d, 6, zeile);
		zeile ++;
	}

	public void bewertung() {		// was fließt hier ein? möglicherweise gar nicht anzeigen

	}

	public void bemerkungen() {		// wird wohl nicht gebraucht

	}
}
