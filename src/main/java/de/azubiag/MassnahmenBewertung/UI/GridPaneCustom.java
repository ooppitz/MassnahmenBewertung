package de.azubiag.MassnahmenBewertung.UI;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GridPaneCustom {

	public static Node getElemByRowAndColumn(GridPane grid, int row, int column)
	{
		for (Node node : grid.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				
				return node;
			}
		}
		
		return new Label();
	}
}
