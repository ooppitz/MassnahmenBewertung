package de.azubiag.MassnahmenBewertung.tools;

public class Logger {

	static Logger theLogger; 
	
	public static Logger getLogger() {
		if (theLogger == null) {
			theLogger = new Logger();
		}
		return  theLogger;
	}
	
	public static void log(String logMessage) {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
