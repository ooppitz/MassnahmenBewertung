package de.azubiag.MassnahmenBewertung.UI;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
import de.azubiag.MassnahmenBewertung.auswertung.AuswertungReferent;
import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungMassnahme;
import de.azubiag.MassnahmenBewertung.datenstrukturen.BewertungReferent;
import de.azubiag.MassnahmenBewertung.tools.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/* Ausgabe der Auswertung */

public class ControllerAuswertungAnzeigen {		// was fehlt:  GridPane muss möglicherweise bei zeile>?? bei der Höhe +49 addieren

	List<AzubiAntwort> antwortListe = new ArrayList<AzubiAntwort>();
	List<AuswertungReferent> auswertungenReferenten;
	AuswertungMassnahme auswertungMassnahme;
	FragebogenEigenschaften eigenschaft;
	private static DecimalFormat zweiStellenNachKomma = new DecimalFormat("#.##");

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

	public void setEigenschaft(FragebogenEigenschaften eigenschaft) {
		this.eigenschaft = eigenschaft;
	}

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

	public void init(MainApp app, FragebogenEigenschaften eigenschaft, List<AzubiAntwort> antwortListe) {

		this.setMainApp(app);
		setName(eigenschaft.fragebogen_name);
		System.out.println("AuswertungAnzeigen: "+antwortListe.size());
		for (AzubiAntwort azubiAntwort : antwortListe) {
			System.out.println("AuswertungAnzeigen->AntwortListe>>> "+azubiAntwort);			// <-- DEBUG
		}

		if (antwortListe.size()==0)
		{
			Logger log = Logger.getLogger();
			log.logError("In ControllerAuswertungAnzeigen hat AntwortListe die Länge 0 !!!");
			Label warning = new Label("     In ControllerAuswertungAnzeigen hat AntwortListe die Länge 0 !!!");
			grid.add(warning, 0, zeile);
			zeile = 2;
			return;
		}

		final List<BewertungMassnahme> bewertungListe = new ArrayList<BewertungMassnahme>();

		for (AzubiAntwort azubiAntwort : antwortListe) {
			bewertungListe.add(azubiAntwort.massnahme);
		}

		auswertungMassnahme = new AuswertungMassnahme(bewertungListe);
		auswertungenReferenten = AuswertungReferent.getAuswertungenAllerReferenten(antwortListe);

		auswertungMassnahme.alleBemerkBetrng = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkBetrng);
		auswertungMassnahme.alleBemerkRefAllg = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkRefAllg);
		auswertungMassnahme.alleBemerkVerl = filtereUndMischeArrayList(auswertungMassnahme.alleBemerkVerl);

		setName(eigenschaft.fragebogen_name);

		zeile = 1; 


	}

	public ArrayList<String> filtereUndMischeArrayList(ArrayList<String> liste) {

		for (int i=0; i<liste.size(); i++) {	// entfernen von leeren Einträgen
			if (liste.get(i) == null || liste.get(i).isBlank())
			{
				liste.remove(i);
			}
		}
		Collections.shuffle(liste);				// zufällige Reihenfolge
		return liste;
	}

	public ArrayList<String> filtereUndMischeList(List<String> eingabe) {

		ArrayList<String> liste = (ArrayList<String>) eingabe;
		for (int i=0; i<liste.size(); i++) {	// entfernen von leeren Einträgen
			if (liste.get(i) == null || liste.get(i).isBlank())
			{
				liste.remove(i);
			}
		}
		Collections.shuffle(liste);				// zufällige Reihenfolge
		return liste;
	}


	public void erzeugeDarstellung() {

		anfang();
		verlauf();
		betreuung();
		bemerkungen();
		referenten();
		setze_alle_Fonts();

	}

	public void anfang() {

		Label massnahme = new Label();
		massnahme.setText("Maßnahme von "+eigenschaft.von_datum+" bis "+eigenschaft.bis_datum);
		grid.add(massnahme, 0, zeile);
		zeile++;

		Label auftrnummer = new Label();
		auftrnummer.setText("Auftragsnummer: "+eigenschaft.auftrags_nummer);
		grid.add(auftrnummer, 0, zeile);
		zeile++;

		Label leitung = new Label();
		leitung.setText("Seminarleitung: "+eigenschaft.seminarleiter_name);
		grid.add(leitung, 0, zeile);
		zeile++;

		Label datum = new Label();
		datum.setText("Datum: "+eigenschaft.ausstellungs_datum);
		grid.add(datum, 0, zeile);
		zeile++;
		Label spacer = new Label("  ");
		grid.add(spacer, 0, zeile);
		zeile++;

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
		Label o_d = new Label(zweiStellenNachKomma.format(auswertungMassnahme.durchschnOrg));

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
		Label v_d = new Label(zweiStellenNachKomma.format(auswertungMassnahme.durchschnVerl));

		grid.add(verlauf_frage, 0, zeile);
		grid.add(v__2, 1, zeile);
		grid.add(v__1, 2, zeile);
		grid.add(v_0, 3, zeile);
		grid.add(v_1, 4, zeile);
		grid.add(v_2, 5, zeile);
		grid.add(v_d, 6, zeile);
		zeile ++;

		Label ueberschrift_bemerkungen = new Label("Bemerkungen dazu:");
		grid.add(ueberschrift_bemerkungen, 0, zeile);
		zeile++;

		if (auswertungMassnahme.alleBemerkVerl.isEmpty()) {
		    Label leer_hinweiß = new Label("(Es gibt keine Bemerkungen.)");
		    grid.add(leer_hinweiß, 0, zeile);
		    zeile++;
		} else {
		    int anzahlBemerkungen = auswertungMassnahme.alleBemerkVerl.size(); 
		    for (int i = 0; i < anzahlBemerkungen; i++) {
			String bemerkung = auswertungMassnahme.alleBemerkVerl.get(i);
			if (bemerkung.contains("\n")) {
			    String[] zeilenDerBemerkung = bemerkung.split("\\n");
			    for (String zeileBem : zeilenDerBemerkung) {
				Label labelZeile = new Label(zeileBem);
				labelZeile.setWrapText(true);
				grid.add(labelZeile, 0, zeile);
				zeile++;
			    }
			} else {
			    Label temp_bemerkung = new Label(bemerkung);
			    temp_bemerkung.setWrapText(true);
			    grid.add(temp_bemerkung, 0, zeile);
			    zeile++;
			}

			if (i<anzahlBemerkungen-1) {
			    Label lblTrennlinie = new Label("-------");
			    lblTrennlinie.setWrapText(true);
			    grid.add(lblTrennlinie, 0, zeile);
			}
		    }
		}
		Label spacer = new Label("  ");
		grid.add(spacer, 0, zeile);
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
		Label b_d = new Label(zweiStellenNachKomma.format(auswertungMassnahme.durchschnBetrng));

		grid.add(betreuung_frage, 0, zeile);
		grid.add(b__2, 1, zeile);
		grid.add(b__1, 2, zeile);
		grid.add(b_0, 3, zeile);
		grid.add(b_1, 4, zeile);
		grid.add(b_2, 5, zeile);
		grid.add(b_d, 6, zeile);
		zeile ++;

		Label ueberschrift_bemerkungen = new Label("Bemerkungen dazu:");
		grid.add(ueberschrift_bemerkungen, 0, zeile);
		zeile++;

		if (auswertungMassnahme.alleBemerkBetrng.isEmpty())
		{
			Label leer_hinweiß = new Label("(Es gibt keine Bemerkungen.)");
			grid.add(leer_hinweiß, 0, zeile);
			zeile++;
		}
		else
		{
			for (String bemerkung : auswertungMassnahme.alleBemerkBetrng) {

				Label temp_bemerkung = new Label(bemerkung);
				temp_bemerkung.setWrapText(true);
				grid.add(temp_bemerkung, 0, zeile);
				zeile++;
			}
		}
		Label spacer = new Label("  ");
		grid.add(spacer, 0, zeile);
		zeile ++;
	}

	public void bemerkungen() {

		Label ueberschrift_bemerkungen = new Label("3.Bewertung der Referenten bzw. Referentinnen:");
		grid.add(ueberschrift_bemerkungen, 0, zeile);
		zeile++;

		if (auswertungMassnahme.alleBemerkRefAllg.isEmpty())
		{
			Label leer_hinweiß = new Label("(Es gibt keine Bemerkungen.)");
			grid.add(leer_hinweiß, 0, zeile);
			zeile++;
		}
		else
		{
			for (String bemerkung : auswertungMassnahme.alleBemerkRefAllg) {

				Label temp_bemerkung = new Label(bemerkung);
				temp_bemerkung.setWrapText(true);
				grid.add(temp_bemerkung, 0, zeile);
				zeile++;
			}
		}
		Label spacer = new Label("  ");
		grid.add(spacer, 0, zeile);
		zeile ++;
	}

	public void referenten() {
		Label ueberschrift = new Label("4.Auswertung der Referenten:");
		grid.add(ueberschrift, 0, zeile);
		zeile++;
		Label spacer = new Label("  ");
		grid.add(spacer, 0, zeile);
		zeile++;

		for (AuswertungReferent auswertungReferent : auswertungenReferenten) {

			Label ref_name = new Label(auswertungReferent.getName());
			Label r__2 = new Label("-2");
			Label r__1 = new Label("-1");
			Label r_0 = new Label(" 0");
			Label r_1 = new Label("+1");
			Label r_2 = new Label("+2");
			Label r_d = new Label("Ø");

			grid.add(ref_name, 0, zeile);
			grid.add(r__2, 1, zeile);
			grid.add(r__1, 2, zeile);
			grid.add(r_0, 3, zeile);
			grid.add(r_1, 4, zeile);
			grid.add(r_2, 5, zeile);
			grid.add(r_d, 6, zeile);
			zeile ++;

			Label vorbereitung_frage = new Label("Wie war ihr/sein Unterricht vorbereitet ?");
			Label vo__2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVorbereitung[0]));
			Label vo__1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVorbereitung[1]));
			Label vo_0 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVorbereitung[2]));
			Label vo_1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVorbereitung[3]));
			Label vo_2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVorbereitung[4]));
			Label vo_d = new Label(zweiStellenNachKomma.format(auswertungReferent.durchschnittVorbereitung));

			grid.add(vorbereitung_frage, 0, zeile);
			grid.add(vo__2, 1, zeile);
			grid.add(vo__1, 2, zeile);
			grid.add(vo_0, 3, zeile);
			grid.add(vo_1, 4, zeile);
			grid.add(vo_2, 5, zeile);
			grid.add(vo_d, 6, zeile);
			zeile ++;

			Label fachwissen_frage = new Label("Wie umfangreich war ihr/sein Fachwissen ? ");
			Label f__2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnFachwissen[0]));
			Label f__1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnFachwissen[1]));
			Label f_0 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnFachwissen[2]));
			Label f_1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnFachwissen[3]));
			Label f_2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnFachwissen[4]));
			Label f_d = new Label(zweiStellenNachKomma.format(auswertungReferent.durchschnittFachwissen));

			grid.add(fachwissen_frage, 0, zeile);
			grid.add(f__2, 1, zeile);
			grid.add(f__1, 2, zeile);
			grid.add(f_0, 3, zeile);
			grid.add(f_1, 4, zeile);
			grid.add(f_2, 5, zeile);
			grid.add(f_d, 6, zeile);
			zeile ++;

			Label probleme_frage = new Label("Wie ging sie/er auf spezielle thematische Probleme ein ? ");
			Label p__2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnEingehenAufProbleme[0]));
			Label p__1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnEingehenAufProbleme[1]));
			Label p_0 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnEingehenAufProbleme[2]));
			Label p_1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnEingehenAufProbleme[3]));
			Label p_2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnEingehenAufProbleme[4]));
			Label p_d = new Label(zweiStellenNachKomma.format(auswertungReferent.durchschnittEingehenAufProbleme));

			grid.add(probleme_frage, 0, zeile);
			grid.add(p__2, 1, zeile);
			grid.add(p__1, 2, zeile);
			grid.add(p_0, 3, zeile);
			grid.add(p_1, 4, zeile);
			grid.add(p_2, 5, zeile);
			grid.add(p_d, 6, zeile);
			zeile ++;

			Label vermittlung_frage = new Label("Wie verständlich sie/er die Inhalte vermitteln ? ");
			Label ve__2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnInhaltsvermittlung[0]));
			Label ve__1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnInhaltsvermittlung[1]));
			Label ve_0 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnInhaltsvermittlung[2]));
			Label ve_1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnInhaltsvermittlung[3]));
			Label ve_2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnInhaltsvermittlung[4]));
			Label ve_d = new Label(zweiStellenNachKomma.format(auswertungReferent.durchschnittInhaltsvermittlung));

			grid.add(vermittlung_frage, 0, zeile);
			grid.add(ve__2, 1, zeile);
			grid.add(ve__1, 2, zeile);
			grid.add(ve_0, 3, zeile);
			grid.add(ve_1, 4, zeile);
			grid.add(ve_2, 5, zeile);
			grid.add(ve_d, 6, zeile);
			zeile ++;

			Label verhalten_frage = new Label("Wie sagte Ihnen ihr/sein Verhalten gegenüber den Seminarteilnehmern zu ? ");
			Label vr__2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVerhalten[0]));
			Label vr__1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVerhalten[1]));
			Label vr_0 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVerhalten[2]));
			Label vr_1 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVerhalten[3]));
			Label vr_2 = new Label(Integer.toString(auswertungReferent.stimmenProRadioBtnVerhalten[4]));
			Label vr_d = new Label(zweiStellenNachKomma.format(auswertungReferent.durchschnittVerhalten));

			grid.add(verhalten_frage, 0, zeile);
			grid.add(vr__2, 1, zeile);
			grid.add(vr__1, 2, zeile);
			grid.add(vr_0, 3, zeile);
			grid.add(vr_1, 4, zeile);
			grid.add(vr_2, 5, zeile);
			grid.add(vr_d, 6, zeile);
			zeile ++;

			ArrayList<String> bemerkungen = filtereUndMischeList(auswertungReferent.getBemerkungen());

			Label ueberschrift_bemerkungen = new Label("Bemerkungen zu: "+auswertungReferent.getName());
			grid.add(ueberschrift_bemerkungen, 0, zeile);
			zeile++;

			if (bemerkungen.isEmpty())
			{
				Label leer_hinweiß = new Label("(Es gibt keine Bemerkungen.)");
				grid.add(leer_hinweiß, 0, zeile);
				zeile++;
			}
			else
			{
				for (String bemerkung : bemerkungen) {

					Label temp_bemerkung = new Label(bemerkung);
					temp_bemerkung.setWrapText(true);
					grid.add(temp_bemerkung, 0, zeile);
					zeile++;
				}
			}
			Label spacer2 = new Label("  ");
			grid.add(spacer2, 0, zeile);
			zeile ++;
		}
	}

	public void setze_alle_Fonts() {

		for (Node node : grid.getChildrenUnmodifiable() ) {

			Label label = (Label) node;
			label.setFont(new Font(20));
		}
	}
}
