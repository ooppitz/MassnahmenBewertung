package de.azubiag.MassnahmenBewertung.tools;

import java.time.LocalDate;

/**
 * Datumsimplementierung, da die Java-Standard-Implementierung für unsere Zwecke unzureichend ist
 * (z. B. nimmt 31. Februar und wandelt ihn ohne Exception in 29. Januar um).
 * <p>
 * Datume vor 1900 oder nach 2110 werden nicht akzeptiert.
 * @author Luna
 */
public final class Datum implements Comparable<Datum> {

	
	public static final int GROESSER = 1;
	public static final int GLEICH = 0;
	public static final int KLEINER = -1;

	private static final int MINIMUM_PLAUSIBLE_YEAR = 1900;
	private static final int MAXIMUM_PLAUSIBLE_YEAR = 2110;
	
	public final int tag;
	public final int monat;
	public final int jahr;
	
	private Datum(int tag, int monat, int jahr) {
		this.tag = tag;
		this.monat = monat;
		this.jahr = jahr;
	}

	@Override
	public int compareTo(Datum anderes) {
		if (this.jahr != anderes.jahr)  {
			return this.jahr > anderes.jahr? GROESSER : KLEINER;
		} else if (this.monat != anderes.monat) {
			return this.monat > anderes.monat? GROESSER : KLEINER;
		} else if (this.tag != anderes.tag) {
			return this.tag > anderes.tag? GROESSER : KLEINER;
		} else return GLEICH;
	}
	
	/**
	 * Parst ein {@link Datum} aus einem String im Format dd.mm.yyyy
	 * @param datumstring Der String
	 * @return null, wenn der String nicht dem Format entspricht oder das Datum ungültig ist
	 */
	public static Datum parse(String datumstring) {
		String[] teile = datumstring.split("\\.", -1);
		try {
			int tag = Integer.parseInt(teile[0]);
			int monat = Integer.parseInt(teile[1]);
			int jahr = Integer.parseInt(teile[2]);
			Datum datum = new Datum(tag, monat, jahr);
			return datum.valid()? datum : null;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException nf_aioobex) {
			return null;
		}
	}
	
	/**
	 * Erzeugt ein gültiges {@link Datum} aus Tag, Monat und Jahr.
	 * @param tag Der Tag
	 * @param monat Der Monat
	 * @param jahr Das Jahr
	 * @return null, wenn ungültig
	 */
	public static Datum newDatum(int tag, int monat, int jahr) {
		Datum datum = new Datum(tag, monat, jahr);
		return datum.valid()? datum : null;
	}
	
	/**
	 * Prüft ein {@link Datum} auf Gültigkeit.
	 * @return true, wenn gültig
	 */
	private boolean valid() {
		return tag > 0 && tag <= tageImMonat(monat, jahr)
				&& monat > 0 && monat <= 12
				&& jahr >= MINIMUM_PLAUSIBLE_YEAR && jahr <= MAXIMUM_PLAUSIBLE_YEAR;
	}

	/**
	 * Gibt die Anzahl der tatsächlichen Tage im Monat nach dem gregorianischen Kalender zurück
	 * @param monat Der Monat
	 * @param jahr Das Jahr (relevant wegen Schaltjahr)
	 * @return Anzahl der Tage im Monat
	 */
	private static int tageImMonat(int monat, int jahr) {
		switch (monat) {
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return schaltjahr(jahr)? 29 : 28;
		default:
			return 31;
		}
	}

	/**
	 * Prüft, ob ein Jahr ein gregorianisches Schaltjahr ist.
	 * @param jahr Jahreszahl
	 * @return true, wenn dasJahr ein greg. Schaltjahr ist.
	 */
	private static boolean schaltjahr(int jahr) {
		if (jahr % 400 == 0) return true;
		if (jahr % 100 == 0) return false;
		return jahr % 4 == 0;
	}
	
	@Override
	public String toString() {
		return "Datum[tag=" +tag+ ", monat=" +monat+ ", jahr=" +jahr+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + jahr;
		result = prime * result + monat;
		result = prime * result + tag;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datum other = (Datum) obj;
		if (jahr != other.jahr)
			return false;
		if (monat != other.monat)
			return false;
		if (tag != other.tag)
			return false;
		return true;
	}
	
	/**
	 * Erzeugt ein entsprechendes {@link LocalDate}-Objekt.
	 * @return das erzeugte {@link LocalDate}-Objekt
	 */
	public LocalDate toLocalDate() {
		return LocalDate.of(jahr, monat, tag);
	}
}
