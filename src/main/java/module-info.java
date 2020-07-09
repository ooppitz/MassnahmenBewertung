module de.azubiag.MassnahmenBewertung {
    requires transitive javafx.controls;
    requires javafx.fxml;
/*	requires org.junit.jupiter.api; */
	requires java.desktop;

    opens de.azubiag.MassnahmenBewertung to javafx.fxml;
    exports de.azubiag.MassnahmenBewertung;
    exports de.azubiag.MassnahmenBewertung.UI;
    opens de.azubiag.MassnahmenBewertung.UI;
}