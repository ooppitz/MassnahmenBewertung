package de.azubiag.MassnahmenBewertung.upload.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PfadfindungTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String username =System.getProperty("user.name");
		System.out.println(Files.exists(Paths.get("C:\\Users\\"+username+"\\AppData\\Local\\Packages")));
		try {
			Files.list(new File("C:\\Users\\"+username+"\\AppData\\Local\\Packages").toPath()).forEach(path -> System.out.println(path));;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

//C:\Users\<username>\AppData\Local\Packages ist der Standard-Speicherordner
//C:\Users\denis\AppData\Roaming ist der Speicher, wenn man etwas über jeden PC in der Dömäne zugreifen möchte