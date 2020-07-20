package de.azubiag.MassnahmenBewertung.upload;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/* Um Github zu verwenden, um die Fragebogen zu hosten, braucht man einen Account. 
 * 
 * https://pages.github.com/
 * 
 *    Head over to GitHub and create a new repository named username.github.io, 
 *    where username is your username (or organization name) on GitHub.
 * 
 * Zu Testzwecken verwenden wir https://.github.io/DATEINAMEN.
 *
 */

public class Upload {
	
	// Zugangsdaten für den bei der Registrierung verwendenten Gmail-Account
	static String gitHubEmail = "gfigithubaccess@gmail.com";
	static String emailPasswort = "GfiGitHubAccess2020!";
	static String telefonNummer = "017672414900"; // Oliver Oppitz
	
	// Zugriffsdaten etc. für Github
	static String repositoryName = "gfigithubaccess.github.io";
	static String gitHubBenutzernamen = "gfigithubaccess"; // Nutzername für den GitHub-Account
<<<<<<< HEAD
	static String gitHubPasswort = "	"; // Passwort für den GitHub-Account
=======
	static String gitHubPasswort = "GfiGitHubAccess2020!"; // Passwort für den GitHub-Account
>>>>>>> GUI-Filip
	static String remoteRepoPath = "https://github.com/gfigithubaccess/jgitest.git";
	
	static Upload instance = null;
	
	CredentialsProvider cp;
	String remotePfad;

	Git gitController;
	Repository lokalRepo;
	
	// Credentials werden an den Constructor übergeben oder in Upload fest
	// implementiert

	static String repositoryPfad = null;

	public Upload(String gitHubBenutzername, String gitHubPasswort, String remotePfad, String repositoryName)
			throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		
		this.remotePfad = remotePfad;
		this.repositoryName = repositoryName;
		
		repoUeberpruefen();
		
		cp = new UsernamePasswordCredentialsProvider(gitHubBenutzername, gitHubPasswort);
		lokalRepo = new FileRepository(getRepositoryPfad() + "/.git");
		gitController = new Git(lokalRepo);
	}

	
	public static Upload getInstance() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		
		if ( instance == null ) {
			instance = new Upload(gitHubBenutzernamen, gitHubPasswort, remoteRepoPath, repositoryName);
		}
		return instance;
	}
	
	/*
	 * Wird genutzt, um den Fragebogen beim Erzeugen an der richtigen Stelle
	 * abzulegen.
	 */
	public String getRepositoryPfad() {

		if (repositoryPfad == null) {
			repositoryPfad = System.getProperty("java.io.tmpdir") + repositoryName + "\\";
		}
		return repositoryPfad;
	}

	/*
	 * Falls das Repo lokal schon existiert, kehrt die Methode zurück. Falls kein
	 * ein lokales Repo existiert, wird es angelegt durch clonen des remote Repo.
	 */
	public void repoUeberpruefen() throws InvalidRemoteException, TransportException, GitAPIException {

		// Überprüfen, ob ein lokales Repo existiert
		try {
			Git.open(new File(getRepositoryPfad()));
		} catch (IOException e) {
			// Clonen des remote Repo
			Git.cloneRepository().setURI(remotePfad).setDirectory(new File(getRepositoryPfad()))
					.setCredentialsProvider(cp).call();
		}

	}

	/*
	 * Lädt einen Fragebogen hoch. Die Methode kümmert sich um alle Details: git
	 * pull, git add, git commit, git push Setzt voraus, dass der Fragebogen in
	 * einem Folder des Repos abgelegt wurde.
	 * 
	 * @return boolean : Zeigt Erfolg oder Misserfolg an
	 */

	public boolean hochladen() {

		try {
			// git pull
			gitController.pull().setCredentialsProvider(cp).call();

			// git add . --> Fügt Fragebogen im Staging Area hinzu
			gitController.add().addFilepattern(".").call();

			// git commit
			gitController.commit().setMessage("Test").call();

			// git push --> Schreibt die Änderungen in das remote Repo
			// $ git push --all
			gitController.push().setCredentialsProvider(cp).setPushAll().call();

			return true;
			
		} catch (GitAPIException e) {
			return false;
		}

	}

}
