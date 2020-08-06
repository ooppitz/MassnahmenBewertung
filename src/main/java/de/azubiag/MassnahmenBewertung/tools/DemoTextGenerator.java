package de.azubiag.MassnahmenBewertung.tools;

import java.util.Random;

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
	
	public DemoTextGenerator() {
		rng = new Random();
	}
	
	public String generate() {
		return TEXTE[rng.nextInt(TEXTE.length)];
	}
	
}
