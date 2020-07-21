package de.azubiag.MassnahmenBewertung.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Serialize Data
 * 
 * @author Benedikt Greinwald
 */
public class Serialize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Serialize ser = new Serialize();
		ser.saveData();
//		ser.loadData();
	}

	// Serialize data
	public void saveData() {
		// ArrayList to store all objects
		ArrayList<Object> data = new ArrayList<Object>();

		// Add Objects here
		data.add("A"); // Object 0
		data.add("B"); // Object 1
		data.add("C"); // Object 2

		try {
			FileOutputStream fos = new FileOutputStream("data.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(data);
			oos.close();
			fos.close();
			System.out.println("Serialized data was saved in \"data.ser\"");

		} catch (IOException e) {
			Logger l = new Logger();
			l.logError(e);
			e.printStackTrace();
		}
	}

	// Deserialize data
	@SuppressWarnings("unchecked")
	public void loadData() {
		// ArrayList to store all deserialized objects
		ArrayList<Object> deserialized = new ArrayList<Object>();

		try {
			FileInputStream fis = new FileInputStream("data.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			deserialized = (ArrayList<Object>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException e) {
			Logger l = new Logger();
			l.logError(e);
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			Logger l = new Logger();
			l.logError(e);
			e.printStackTrace();
			return;
		}

		
		// Recieve loaded objects here
		System.out.println((String) deserialized.get(0)); // Object 0
		System.out.println((String) deserialized.get(1)); // Object 1
		System.out.println((String) deserialized.get(2)); // Object 2

	}
}
