package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;
/**
 * 
 * @author Denis Bode
 *
 */
public class TestFeeder3 {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner usc = new Scanner(System.in);

		System.out.print("Pfad > ");

		String path = usc.nextLine();

		ArrayList<String> buffer = new ArrayList<>();

		Scanner fsc = null;

		try {
			fsc = new Scanner(new FileInputStream(new File(path)), "UTF-8");

			while (fsc.hasNextLine()) {
				buffer.add(fsc.nextLine());
			}

			fsc.close();

		} catch (Exception e) {
			if (fsc != null)
				fsc.close();
			throw new RuntimeException(e);
		}

		AuswertungMassnahme am = new AuswertungMassnahme(
				AuswertungMassnahme.konvertiereBewertungMassnahmeInAuswertungMassnahme(buffer));
		System.out.println(am);
	}

}
