package de.azubiag.MassnahmenBewertung.UI;

import de.azubiag.MassnahmenBewertung.tools.Logger;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class GridPaneCustom {

	public static Node getElemByRowAndColumn(GridPane grid, int row, int column)
	{
//		System.out.println(row+"\t"+column+"\t"+grid.getChildren().toString());
		for (Node node : grid.getChildren()) {
//			System.out.println(node);
//			System.out.println(GridPane.getRowIndex(node));	// java.lang.NullPointerException !!!
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
				
				return node;
			}
		}
		Logger.getLogger().logError(new RuntimeException("GridPaneCustom: Node nicht gefunden!\nEs wurde nach row="+row+" ,column="+column+" gesucht."));
		return null;
	}
	
	public static void moveElemByRowAndColumn(Node node, GridPane grid, int row, int column)
	{
		Logger logger = Logger.getLogger();
		if (node == null) logger.logError(new NullPointerException("node == null"));
		int oldrow = GridPane.getRowIndex(node);	// java.lang.NullPointerException !!!
		int oldcol = GridPane.getColumnIndex(node);
		int newrow = oldrow + row;
		int newcol = oldcol + column;
		int rowspan = GridPane.getRowSpan(node);
		int colspan = GridPane.getColumnSpan(node);
		grid.getChildren().remove(node);
		grid.add(node, newcol, newrow, colspan, rowspan);
	}
}
