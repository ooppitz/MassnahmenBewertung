package de.azubiag.MassnahmenBewertung.upload.test;

import java.io.IOException;
import org.eclipse.jgit.api.errors.GitAPIException;

import de.azubiag.MassnahmenBewertung.UI.MainApp;
import de.azubiag.MassnahmenBewertung.upload.Upload;

public class PfadfindungTest {

	public static void main(String[] args) {
		
		MainApp.upload.getProgrammDatenOrdner();
//			Files.list(new File("C:\\Users\\"+username+"\\AppData\\Local\\Packages").toPath()).forEach(path -> System.out.println(path));;
	}

}

//C:\Users\<username>\AppData\Local\Packages ist der Standard-Speicherordner
//C:\Users\denis\AppData\Roaming ist der Speicher, wenn man etwas über jeden PC in der Dömäne zugreifen möchte