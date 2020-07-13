package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.azubiag.MassnahmenBewertung.datenstrukturen.AzubiAntwort;

public class TestFeeder2 {

	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner usc = new Scanner(System.in);
		
		System.out.print("Pfad > ");
		String path = usc.nextLine();
		for (AzubiAntwort aa : loadAzubiAntwortenFromFilePath(path)) System.out.println(aa);
		
	}
	
	public static List<AzubiAntwort> loadAzubiAntwortenFromFilePath(String filepath) {

		ArrayList<String> buffer = new ArrayList<>();
		
		Scanner fsc = null;
		
		try {
			fsc = new Scanner(new FileInputStream(new File(filepath)), "UTF-8");
			
			while (fsc.hasNextLine()) {
				buffer.add(fsc.nextLine());
			}
			
			fsc.close();
			
		} catch (Exception e) {
			if (fsc != null) fsc.close();
			throw new RuntimeException(e);
		}
		
		return AzubiAntwort.konvertiereStringsInAzubiAntworten(buffer);
	}
}
