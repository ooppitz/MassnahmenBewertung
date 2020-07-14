package de.azubiag.MassnahmenBewertung.upload;

public class Upload {

	// Credentials werden an den Constructor übergeben oder in Upload fest
	// implementiert

	static String repositoryPfad;

	public Upload() {

	}

	/*
	 * Wird genutzt, um den Fragebogen beim Erzeugen an der richtigen Stelle
	 * abzulegen.
	 */
	public static String getRepositoryPfad() {
		return repositoryPfad;
	}

	
	/* Falls das Repo lokal schon existiert, kehrt die Methode zurück.
	 * Falls kein ein lokales Repo existiert, wird es angelegt durch clonen des remote Repo.
	 */
	public void repoUeberpruefen() {
		
		// Überprüfen, ob ein lokales Repo existiert
		
		// Clonen des remote Repo
		
	}
	
	/*
	 * Lädt einen Fragebogen hoch. Die Methode kümmert sich um alle Details: git
	 * pull, git add, git commit, git push
	 * Setzt voraus, dass der Fragebogen in einem Folder des Repos abgelegt wurde.
	 * 
	 * @return boolean : Zeigt Erfolg oder Misserfolg an
	 */

	public boolean hochladen() {

		// git pull
		
		// git add .    --> Fügt Fragebogen im Staging Area hinzu
		
		// git commit   
		
		// git push     --> Schreibt die Änderungen in das remote Repo
		
		return true;

	}

}
