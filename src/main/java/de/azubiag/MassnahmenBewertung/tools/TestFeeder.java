package de.azubiag.MassnahmenBewertung.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import de.azubiag.MassnahmenBewertung.auswertung.AuswertungMassnahme;

/**
 * 
 * @author Luna
 *
 */
public class TestFeeder {

	public static void main(String[] args) throws Exception {
		String path = JOptionPane.showInputDialog(null, "Bitte Testdatenort angeben", "TestFeeder", JOptionPane.PLAIN_MESSAGE);
		LinkedList<String> buffer = new LinkedList<>();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			while(true) {
				String line = br.readLine();
				if (line == null) break;
				buffer.add(line);
			}
			
			br.close();
			
			@SuppressWarnings("unused")
			AuswertungMassnahme am = new AuswertungMassnahme(extractArray(buffer));
			
			nop();
			nop();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, textify(e), "TestFeeder Error", JOptionPane.ERROR_MESSAGE);
			throw e;
		}
	}
	
	private static String textify(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return e.getClass().getName() + "\n" + e.getMessage() + "\n" + sw.toString();
	}

	private static void nop() {}
	
	private static String[] extractArray(List<String> liste) {
		String[] array = new String[liste.size()];
		for(int i=0; i<array.length; i++) {
			array[i] = liste.get(i);
		}
		return array;
	}

}
