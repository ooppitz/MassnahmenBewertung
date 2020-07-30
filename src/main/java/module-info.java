module de.azubiag.MassnahmenBewertung {
    requires transitive javafx.controls;
    requires javafx.fxml;
/*	requires org.junit.jupiter.api; */
	requires java.desktop;

	requires javafx.graphics;
	requires javafx.base;
	requires org.jsoup;
	requires org.eclipse.jgit;
	requires pdfbox;

    opens de.azubiag.MassnahmenBewertung to javafx.fxml;
    exports de.azubiag.MassnahmenBewertung;
    exports de.azubiag.MassnahmenBewertung.UI;
    exports de.azubiag.MassnahmenBewertung.auswertung.test;
    opens de.azubiag.MassnahmenBewertung.UI;
    
    exports de.azubiag.MassnahmenBewertung.UI.test;
}