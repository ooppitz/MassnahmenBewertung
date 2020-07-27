package de.azubiag.MassnahmenBewertung.UI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class FragebogenEigenschaften implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1246909262064781758L;
	//	public static int zoom;

	public String fragebogen_name;
	public String seminarleiter_name;
	public String auftrags_nummer;
	public String von_datum;
	public String bis_datum;
	public String ausstellungs_datum;
	public String link;

	
	/* Der Konstruktor liest die Werte aus den Feldern des Controllers aus */
	public FragebogenEigenschaften(ControllerFragebogenErstellen controller, String webpath) {
		
		this(controller.getName(), MainApp.getUserName(), 
			controller.auftragsnummer_textfield.getText(), 
			controller.von_Datum.getValue(), controller.bis_Datum.getValue(), 
			controller.heute_datum.getValue(), webpath);
		
	}
	
	public FragebogenEigenschaften(String fragebogen_name, String seminarleiter_name, String auftrags_nummer, 
			LocalDate von_Datum, LocalDate bis_Datum, LocalDate ausstellungs_datum, String link) {
		this.fragebogen_name = fragebogen_name;
		this.seminarleiter_name = seminarleiter_name;
		this.auftrags_nummer = auftrags_nummer;
		if (von_Datum==null)
		{
			this.von_datum = "";
		}
		else
		{
			this.von_datum = Integer.toString(von_Datum.getDayOfMonth())+"."+Integer.toString(von_Datum.getMonthValue())+"."+Integer.toString(von_Datum.getYear());
		}

		if (bis_Datum==null)
		{
			this.bis_datum = "";
		}
		else
		{
			this.bis_datum = Integer.toString(bis_Datum.getDayOfMonth())+"."+Integer.toString(bis_Datum.getMonthValue())+"."+Integer.toString(bis_Datum.getYear());
		}
		if (ausstellungs_datum==null)
		{
			this.ausstellungs_datum = "";
		}
		else
		{
			this.ausstellungs_datum = Integer.toString(ausstellungs_datum.getDayOfMonth())+"."+Integer.toString(ausstellungs_datum.getMonthValue())+"."+Integer.toString(ausstellungs_datum.getYear());
		}
		this.link = link;
	}


	public final void writeObject(ObjectOutputStream os) {

		try {
			os.defaultWriteObject();
			os.writeUTF(fragebogen_name);
			os.writeUTF(seminarleiter_name);
			os.writeUTF(auftrags_nummer);
			os.writeUTF(von_datum);
			os.writeUTF(bis_datum);
			os.writeUTF(ausstellungs_datum);
			os.writeUTF(link);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public final void readObject(ObjectInputStream is) {

		try {
			is.defaultReadObject();
			fragebogen_name = is.readUTF();
			seminarleiter_name = is.readUTF();
			auftrags_nummer = is.readUTF();
			von_datum = is.readUTF();
			bis_datum = is.readUTF();
			ausstellungs_datum = is.readUTF();
			link = is.readUTF();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}
