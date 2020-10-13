package de.azubiag.MassnahmenBewertung.upload;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.azubiag.MassnahmenBewertung.tools.Logger;
import de.azubiag.MassnahmenBewertung.tools.Tools;
import javafx.application.Platform;

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
	static String gitHubPasswort = "GfiGitHubAccess2020!"; // Passwort für den GitHub-Account
	static String remoteRepoPath = "https://github.com/gfigithubaccess/gfigithubaccess.github.io.git";
	final static String appName = "MassnahmenBewertung";

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

		repoKlonenFallsNichtVorhanden();

		cp = new UsernamePasswordCredentialsProvider(gitHubBenutzername, gitHubPasswort);
		lokalRepo = new FileRepository(getRepositoryPfad() + "/.git");
		gitController = new Git(lokalRepo);

		// Überprüfe ob sich repository im master Branch befindet
		String lokalBranch = gitController.getRepository().getBranch();
		if (!lokalBranch.equals("master")) {			
			String errorMessage = "lokaler Branch ist nicht \"master\" (aktueller Branch: \"" + lokalBranch + "\")";
			popupAndExit(errorMessage, "Achtung", JOptionPane.ERROR_MESSAGE);
		}
		
		gitStatusPruefen();
		
		// Potentieller fix zur umgehung eines fehlenden pushes durch Programmabsturz
		// wodurch es zu einem merge-conflict kommen kann
		gitController.reset().setMode(ResetType.HARD).setRef("refs/heads/master").call();
		
		gitController.pull().setCredentialsProvider(cp).call();
	}


	public static Upload getInstance() throws InvalidRemoteException, TransportException, GitAPIException, IOException {

		if ( instance == null ) {
			instance = new Upload(gitHubBenutzernamen, gitHubPasswort, remoteRepoPath, repositoryName);
		}
		return instance;
	}
	/*
	 * Überprüfe git status auf ausstehende änderungen
	 */
	void gitStatusPruefen() throws NoWorkTreeException, GitAPIException {
		
		Status status = gitController.status().call();
		
		if (!status.getAdded().isEmpty()) {
			String statusMessage = "Added: " + status.getAdded();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getChanged().isEmpty()) {
			String statusMessage = "Changed: " + status.getChanged();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getConflicting().isEmpty()) {
			String statusMessage = "Conflicting: " + status.getConflicting();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getConflictingStageState().isEmpty()) {
			String statusMessage = "ConflictingStageState: " + status.getConflictingStageState();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getIgnoredNotInIndex().isEmpty()) {
			String statusMessage = "IgnoredNotInIndex: " + status.getIgnoredNotInIndex();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getMissing().isEmpty()) {
			String statusMessage = "Missing: " + status.getMissing();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getModified().isEmpty()) {
			String statusMessage = "Modified: " + status.getModified();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getRemoved().isEmpty()) {
			String statusMessage = "Removed: " + status.getRemoved();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
		if (!status.getUntracked().isEmpty()) {
			String statusMessage = "Untracked: " + status.getUntracked();
			popupAndExit(statusMessage, "Git Status Warnung", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	void popupAndExit(String message, String header, int type) {
		Logger.getLogger().logWarning(header + "\t\t" + message);
		JOptionPane.showMessageDialog(new JFrame(), message, header, type);
		// TODO merkwürdiges verhalten von Platform.exit(); klären
		Platform.exit();
	}
	

	/*
	 * Wird genutzt, um den Fragebogen beim Erzeugen an der richtigen Stelle
	 * abzulegen.
	 */
	public String getRepositoryPfad() {

		if (repositoryPfad == null) {
			repositoryPfad = System.getenv("LOCALAPPDATA")+"\\" + appName + "\\" + repositoryName + "\\";
		}
		return repositoryPfad;
	}

	/* Liefert den Pfad auf das Template Directory */
	public String getTemplateDirectory() {
		return getRepositoryPfad() + "template\\";
	}

	/* Pfad zum Ordner des Seminarleiters mit seinen Fragebögen. 
	   @param seminarleiterName wird normalisiert.
	 */
	public String getSeminarleiterDirectory(String seminarleiterName) {

		seminarleiterName = Tools.normalisiereString(seminarleiterName);
		return getRepositoryPfad() + "fragebogen\\" + seminarleiterName + "\\"; 
	}

	/* Liefert den Pfad auf die Fragebogendatei.
	 * @param seminarleiterName wird normalisiert 
	 * @param fragebogenName wird normalisiert */
	public String getFragebogenPfad(String seminarleiterName, String fragebogenName) {

		fragebogenName = Tools.normalisiereString(fragebogenName);		
		return getSeminarleiterDirectory(seminarleiterName) + fragebogenName + ".html"; 
	}





	/*
	 * Falls das Repo lokal schon existiert, kehrt die Methode zurück. Falls kein
	 * ein lokales Repo existiert, wird es angelegt durch clonen des remote Repo.
	 */
	public void repoKlonenFallsNichtVorhanden() throws InvalidRemoteException, TransportException, GitAPIException {

		// Überprüfen, ob ein lokales Repo existiert
		try {
			Git.open(new File(getRepositoryPfad()));
		} catch (IOException e) {
			// Clonen des remote Repo
			Git.cloneRepository().setURI(remotePfad).setDirectory(new File(getRepositoryPfad()))
			.setCredentialsProvider(cp).call();
		}

	}

	/** Liefert den Ordner für Programmdaten zurück.
	 *  Falls er nicht existiert, wird er erzeugt.
	 *   */
	
	public static File getProgrammDatenOrdner() {
		
		String appData = System.getenv("LOCALAPPDATA");
		File programmDatenOrdner = new File(appData+"\\"+appName);

		if(programmDatenOrdner.exists() == false) {
			programmDatenOrdner.mkdirs();
		}
		return programmDatenOrdner;
	}

	/**
	 * Lädt einen Fragebogen hoch. Die Methode kümmert sich um alle Details: git
	 * pull, git add, git commit, git push Setzt voraus, dass der Fragebogen in
	 * einem Folder des Repos abgelegt wurde.
	 * @param
	 * @return boolean : Zeigt Erfolg oder Misserfolg an
	 */

	public boolean synchronisieren(String fragebogenname, String nutzername) {

		try {
			// git pull
			gitController.pull().setCredentialsProvider(cp).call();

			// git add . --> Fügt Fragebogen im Staging Area hinzu
			gitController.add().addFilepattern(".").call();

			// git commit
			gitController.commit().setAll(true).setMessage(fragebogenname+" von "+nutzername).call();

			// git push --> Schreibt die Änderungen in das remote Repo
			// $ git push --all
			gitController.push().setCredentialsProvider(cp).setPushAll().call();

			return true;

		} catch (GitAPIException e) {
			return false;
		}

	}
	
	public static boolean istFragebogenOnline(long millis, String uri, int umfrageId) {

		long timeStart = System.currentTimeMillis();
		long endTime = timeStart + millis;
		Document doc = null;
		int umfrageID_document;

		while((System.currentTimeMillis() <= endTime ))
		{
			doc = null;

			try {
				
				doc = Jsoup.connect(uri).get();
				
				String temp_string = doc.body().attr("umfrageid");
				umfrageID_document = Integer.parseInt(temp_string);

				if ( umfrageId == umfrageID_document)
				{
					Logger.getLogger().logInfo("UPLOAD: Das Dokument wurde online gefunden und die umfrageID stimmt überein.");
					return true;
				}
				else
				{
					Logger.getLogger().logInfo("UPLOAD: umfrageID in Datei stimmt nicht überein."+umfrageID_document+" ≠ "+umfrageId+
							" Erneuter Versuch in 1000ms. Zeit bis zum Timeout: " + (endTime - System.currentTimeMillis()));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException expectedException) {
				Logger.getLogger().logInfo("UPLOAD: Datei "+uri+" nicht gefunden. Erneuter Versuch in 1000ms. Zeit bis zum Timeout: " + (endTime - System.currentTimeMillis()));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		Logger.getLogger().logInfo("UPLOAD: Timeout von "+millis+"ms überschritten.");
		return false;
	}

}
