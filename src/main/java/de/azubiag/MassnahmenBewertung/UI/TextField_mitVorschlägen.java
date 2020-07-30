package de.azubiag.MassnahmenBewertung.UI;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// in Kooperation mit stackoverflow:  https://stackoverflow.com/questions/36861056/javafx-textfield-auto-suggestions
public class TextField_mitVorschlägen extends TextField{

	private ContextMenu kontext;
	private final SortedSet<String> set;


	public TextField_mitVorschlägen() {

		super();	// Konstruktor des TextFields
		this.set = new TreeSet<String>();
		kontext = new ContextMenu();
	}

	public void fill_context_menu(ArrayList<String> zutreffende_Nutzer) {

		kontext.getItems().clear();
		
		if (zutreffende_Nutzer.isEmpty())
		{
			kontext.hide();
		}
		else
		{	// jedes Element der Liste muss in ein MenuItem umgewandelt werden
			for (String nutzer : zutreffende_Nutzer) {
				
				Label dieses_label = new Label(nutzer);
				dieses_label.setFont(this.getFont());
				CustomMenuItem dieses_menu_item = new CustomMenuItem(dieses_label,true);
				kontext.getItems().add(dieses_menu_item);
			}
			kontext.show(TextField_mitVorschlägen.this, Side.BOTTOM, 0, 0);
		}
//		System.out.println("TextField Items-> "+kontext.getItems());
	}
}
