package de.azubiag.MassnahmenBewertung.tools;

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

	static Logger theLogger; 
	
	public static Logger getLogger() {
		if (theLogger == null) {
			theLogger = new Logger();
		}
		return  theLogger;
	}
	
	/* Wird von der GUI aufgerufen, um relevante Daten zu loggen */
	
	public static void log(String logMessage) {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
