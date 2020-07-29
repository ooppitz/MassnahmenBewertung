package de.azubiag.MassnahmenBewertung.UI;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;

public class MultiAntwortParser {

	public static void main(String[] args) throws UnsupportedFlavorException, IOException {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		String data = (String) clipboard.getData(DataFlavor.stringFlavor);
		var result = parse(data);
		for (String line : result) {
			System.out.println(line);
		}
	}
	
	private static final char MINUS = '-';
	private static final int MINUSES_REQUIRED = 3;
	
	/**
	 * Zerlegt einen String mit einer oder mehreren verschlüsselten Antworten in einzelne Strings.
	 * @param source ein String mit einer oder mehrerer verschlüsselter Antworten
	 * @return
	 */
	public static ArrayList<String> parse(String source) {
		int minusesInARow = 0;
		var buffer = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		boolean inGap = true;
		for(int i=0; i<source.length(); i++) {
			if (source.charAt(i) == MINUS) {
				minusesInARow++;
			} else {
				minusesInARow = 0;
			}
			if (minusesInARow >= MINUSES_REQUIRED) {
				minusesInARow = 0;
				if (inGap) {
					sb = new StringBuilder();
					for (int j=3; j-->0;) {
						sb.append(MINUS);
					}
					inGap = false;
				} else {
					sb.append(MINUS);
					buffer.add(sb.toString());
					inGap = true;
				}
			} else if (!inGap) sb.append(source.charAt(i));
		}
		return buffer;
	}

}
