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
	
	public static ArrayList<String> parse(String fromClipboard) {
		return new MultiAntwortParser(fromClipboard).doTheParsing();
	}
	
	private String source;
	
	private MultiAntwortParser(String raw) {
		this.source = raw;
	}
	
	private int minuses;
	private boolean inGap;
	private int strindex;
	private boolean loopOn;
	
	private StringBuilder sb;
	
	private static final char MINUS = '-';
	private static final int MINUSES_REQUIRED = 3;
	
	private ArrayList<String> buffer;
	
	private ArrayList<String> doTheParsing() {
		init();
		while(loop());
		return buffer;
	}

	private boolean loop() {
		if (source.charAt(strindex) == MINUS) {
			minuses++;
		} else {
			minuses = 0;
		}
		if (minuses >= MINUSES_REQUIRED) {
			if (inGap) {
				sb = new StringBuilder();
				for (int i=3; i-->0;) {
					sb.append(MINUS);
				}
				inGap = false;
			} else {
				sb.append(MINUS);
				buffer.add(sb.toString());
				inGap = true;
			}
		} else {
			if (inGap) {
				// pass
			} else {
				sb.append(source.charAt(strindex));
			}
		}
		loopEndActions();
		return loopOn;
	}

	private void loopEndActions() {
		strindex++;
		if (strindex >= source.length()) loopOn = false;
	}

	private void init() {
		loopOn = true;
		minuses = 0;
		buffer = new ArrayList<>();
		sb = new StringBuilder();
		inGap = true;
		strindex = 0;
	}

}
