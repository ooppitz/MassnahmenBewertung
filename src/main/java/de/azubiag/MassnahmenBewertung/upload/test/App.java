package de.azubiag.MassnahmenBewertung.upload.test;

import java.io.IOException;

import de.azubiag.MassnahmenBewertung.upload.Upload;

/* Testcode für das Hochladen eines Fragebogens */

public class App {
	
	public static void main(String[] args) throws IOException {
		
		// TODO Auto-generated method stub
		new Upload("githubbenutzername", "githubpasswort", "remotePfad");
	}

}
