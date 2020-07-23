package de.azubiag.MassnahmenBewertung.UI;

import java.time.LocalDate;

public class FragebogenEigenschaften {

	public String fragebogen_name;
	public String seminarleiter_name;
	public String auftrags_nummer;
	public String von_datum;
	public String bis_datum;
	public String ausstellungs_datum;
	
	public FragebogenEigenschaften(String fragebogen_name, String seminarleiter_name, String auftrags_nummer, LocalDate von_Datum, LocalDate bis_Datum, LocalDate ausstellungs_datum) {
		this.fragebogen_name = fragebogen_name;
		this.seminarleiter_name = seminarleiter_name;
		this.auftrags_nummer = auftrags_nummer;
		if (von_Datum==null)
		{
			this.von_datum = "";
		}
		else
		{
			this.von_datum = von_Datum.toString();
		}
		
		if (bis_Datum==null)
		{
			this.bis_datum = "";
		}
		else
		{
			this.bis_datum = bis_Datum.toString();
		}
		if (ausstellungs_datum==null)
		{
			this.ausstellungs_datum = "";
		}
		else
		{
			this.ausstellungs_datum = ausstellungs_datum.toString();
		}
	}
	
}
