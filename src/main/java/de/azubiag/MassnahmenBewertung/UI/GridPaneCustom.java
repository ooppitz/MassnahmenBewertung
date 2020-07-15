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
	
	public static void moveElemByRowAndColumn(Node node, GridPane grid, int row, int column)
	{
		int oldrow = GridPane.getRowIndex(node);
		int oldcol = GridPane.getColumnIndex(node);
		int newrow = oldrow + row;
		int newcol = oldcol + column;
		int rowspan = GridPane.getRowSpan(node);
		int colspan = GridPane.getColumnSpan(node);
		grid.getChildren().remove(node);
		grid.add(node, newcol, newrow, colspan, rowspan);
	}
}
