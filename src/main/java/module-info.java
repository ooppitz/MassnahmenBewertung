module de.azubiag.MassnahmenBewertung {
    requires javafx.controls;
    requires javafx.fxml;
/*	requires org.junit.jupiter.api; */
	requires java.desktop;

    opens de.azubiag.MassnahmenBewertung to javafx.fxml;
    exports de.azubiag.MassnahmenBewertung;
}