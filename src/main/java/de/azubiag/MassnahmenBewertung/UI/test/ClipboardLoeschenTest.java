package de.azubiag.MassnahmenBewertung.UI.test;

import javafx.scene.input.Clipboard;

public class ClipboardLoeschenTest {

	public static void loeschenTest() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		System.out.println(clipboard.getString());
	
		clipboard.clear();
		System.out.println(clipboard.getString());
	}
		
	

}
