package de.azubiag.MassnahmenBewertung.UI;

import java.util.ArrayList;

public class MultiAntwortParser {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
