package de.azubiag.MassnahmenBewertung.tools;

import java.util.Random;

/**
 * randomly picks from a number of preset textstrings
 * <p>
 * Usage: call the {@link DemoTextGenerator#generate()} method
 * 
 * @author Luna
 *
 */
public final class DemoTextGenerator {
	
	private static final String[] TEXTE = {
			"Test-Kommentar",
			"Toller Unterricht",
			"Langweilig",
			"Zu langer Arbeitstag",
			"miau",
			"Bin überfordert",
			"Der Referent stinkt",
			"Ich würde gerne meine Katze mitbringen",
			"ich hasse die gfi",
			"pika pikachu",
			"Straight Outta Compton",
			"All Of The Lights",
			"In 200 Metern links abbiegen",
			"Fluxkatzen sind besser als Menschen",
			"Menschen stinken",
			"unedfined",
			"Arbeit nervt",
			
	};
	
	private Random rng;
	
	/**
	 * Creates a new {@link DemoTextGenerator}.
	 */
	public DemoTextGenerator() {
		rng = new Random();
	}
	
	/**
	 * randomly picks from an internal array of {@link String}s
	 * @return the picked {@link String}
	 * 
	 * @author Luna
	 */
	public String generate() {
		return TEXTE[rng.nextInt(TEXTE.length)];
	}
	
}
