module de.azubiag.MassnahmenBewertung {
    requires javafx.controls;
    requires javafx.fxml;
/*	requires org.junit.jupiter.api; */
	requires java.desktop;
	requires org.junit.jupiter.api;

	
	opens de.azubiag.MassnahmenBewertung.tools to org.junit.jupiter.api; 
    opens de.azubiag.MassnahmenBewertung to javafx.fxml;
    exports de.azubiag.MassnahmenBewertung;
    exports de.azubiag.MassnahmenBewertung.tools; 
    exports de.azubiag.MassnahmenBewertung.auswertung; 
}