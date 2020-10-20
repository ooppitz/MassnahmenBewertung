package de.azubiag.MassnahmenBewertung.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/*

Fehlermeldung und Stackbacktrace automatisch in Log-File schreiben 

Michael: 
- Die tatsächlich geworfene Exception anzeigen 
- StringWriter, PrintWriter um die Methode printStacktTrace letztlich in ein File schreiben 
- getClass und GetMessage

Log-Datei : kann man auch ignorieren, man kann den Anwender bitten, die Datei zu schicken

1. Anlegen der Logdatei im Repository
2. Committen und Pushen des Logfiles
3. Anzeigen einer Fehlermeldung mit Hinweis, die AzubiAG zu informieren
4. Inhalt des Logfiles
   - Uhrzeit, Datum
   - Stackbacktrace
   - "Alles", was angeklickt wird bzw. zu erfassen ist

 */

public class Logger {

	private static Logger theLogger; 
	
	public static Logger getLogger() {
		if (theLogger == null) {
			theLogger = new Logger(); // TODO: Add default file for logging
		}
		return  theLogger;
	}
	
	/* Wird von der GUI aufgerufen, um relevante Daten zu loggen */
	@Deprecated
	public void log(String logMessage) {
		logInfo(logMessage);
	}
	
	public static void main(String[] args) {

		Logger logger = Logger.getLogger();
		
		logger.logInfo("Button gedrückt");
		logger.logError(new IOException("LMFAO"));

	}
	
	// Doesn't have a sinnvoll target yet, using stderr as placeholder
	private Logger() {
		this.logstream = System.err;
	}
	
	public Logger(PrintStream ps) {
		this.logstream = ps;
	}
	
	private Logger (File file) {
		try {
			this.logstream = new PrintStream(new FileOutputStream(file), true, "UTF-8");
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PrintStream logstream;
	
	
	public void logError(Throwable error) {
		logIt("ERROR", error);
	}
	
	public void logWarning(Object warning) {
		logIt("WARNING", warning);
	}
	
	public void logInfo(Object info) {
		logIt("INFO", info);
	}
	
	public void logStackTrace() {
		logIt("TRACE", new StackTrace());
	}
	
	@SuppressWarnings("serial")
	private static class StackTrace extends Throwable {
		
	}
	
	private synchronized void logIt(String category, Object logMe) {
		logstream.println(category + "@" + timeString());
		if (logMe instanceof Throwable) {
			Throwable ball = (Throwable) logMe;
			ball.printStackTrace(logstream);
		} else {
			logstream.println(logMe.toString());
		}
		logstream.println();
	}
	
	
	
	private static String timeString() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		return String.format("%04d%02d%02d%02d%02d%02d", year, month+1, day, hour, minute, second);
	}
}
