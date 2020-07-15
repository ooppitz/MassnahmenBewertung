package de.azubiag.MassnahmenBewertung.upload;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Upload {

	CredentialsProvider cp;
	String remotePfad;
	Git gitController;
	Repository lokalRepo;
	// Credentials werden an den Constructor übergeben oder in Upload fest
	// implementiert

	static String repositoryPfad = null;

	public Upload(String gitHubBenutzername, String gitHubPasswort, String remotePfad) {
		this.remotePfad=remotePfad;
		try {
			repoUeberpruefen();
		} catch (GitAPIException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cp = new UsernamePasswordCredentialsProvider(gitHubBenutzername, gitHubPasswort);
		try {
			lokalRepo=new FileRepository(getRepositoryPfad()+"/.git");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gitController=new Git(lokalRepo);
	}

	/*
	 * Wird genutzt, um den Fragebogen beim Erzeugen an der richtigen Stelle
	 * abzulegen.
	 */
	public static String getRepositoryPfad() {
		
		if (repositoryPfad == null) {
			repositoryPfad = System.getProperty("java.io.tmpdir")+"test\\";
		}
		return repositoryPfad;
	}

	
	/* Falls das Repo lokal schon existiert, kehrt die Methode zurück.
	 * Falls kein ein lokales Repo existiert, wird es angelegt durch clonen des remote Repo.
	 */
	public void repoUeberpruefen() throws InvalidRemoteException, TransportException, GitAPIException {
		
		// Überprüfen, ob ein lokales Repo existiert
		try {
			Git.open(new File(getRepositoryPfad()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Clonen des remote Repo
			Git.cloneRepository()
			.setURI(remotePfad)
			.setDirectory(new File(getRepositoryPfad()))
			.setCredentialsProvider(cp)
			.call();
		}
		
	
		
	}
	
	/*
	 * Lädt einen Fragebogen hoch. Die Methode kümmert sich um alle Details: git
	 * pull, git add, git commit, git push
	 * Setzt voraus, dass der Fragebogen in einem Folder des Repos abgelegt wurde.
	 * 
	 * @return boolean : Zeigt Erfolg oder Misserfolg an
	 */

	public boolean hochladen() {

			try {
				// git pull
				gitController.pull().setCredentialsProvider(cp).call();
				// git add .    --> Fügt Fragebogen im Staging Area hinzu
				gitController.add().addFilepattern(".");
				// git commit   
				gitController.commit().setMessage("Test").call();
				// git push     --> Schreibt die Änderungen in das remote Repo
				gitController.push().setPushAll();
				return true;
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

	}

}
