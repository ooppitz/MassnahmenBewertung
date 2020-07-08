module de.azubiag.MassnahmenBewertung {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.azubiag.MassnahmenBewertung to javafx.fxml;
    exports de.azubiag.MassnahmenBewertung;
}