package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

/**
 * 
 * @author Luna
 *
 */
public class TestFeeder2 {

	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner usc = new Scanner(System.in);

		System.out.print("Pfad > ");

		String path = usc.nextLine();
		for (AzubiAntwort aa : loadAzubiAntwortenFromFilePath(path)) System.out.println(aa);
		
	}
	
	public static ArrayList<AzubiAntwort> loadAzubiAntwortenFromFilePath(String filepath) {

		ArrayList<String> buffer = new ArrayList<>();

		Scanner fsc = null;

		try {
			fsc = new Scanner(new FileInputStream(new File(filepath)), "UTF-8");

			while (fsc.hasNextLine()) {

			}

			fsc.close();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (fsc != null) fsc.close();
		}
		
		return AzubiAntwort.konvertiereStringsInAzubiAntworten(buffer);

	}
}
