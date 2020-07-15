package de.azubiag.MassnahmenBewertung.upload.test;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;

import de.azubiag.MassnahmenBewertung.upload.Upload;

/* Testcode für das Hochladen eines Fragebogens */

public class App {
	
	public static void main(String[] args) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
		
		// TODO Auto-generated method stub
		Upload upl = new Upload("githubbenutzername", "githubpasswort", "remotePfad");
		if(upl.hochladen()==true) {
			System.out.println("Hochladen hat geklappt");
		} else {
			System.out.println("Hochladen gescheitert");
		}
		
	}

}
