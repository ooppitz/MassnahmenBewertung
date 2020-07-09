package de.azubiag.MassnahmenBewertung.UI;

import de.muc.gfi.referentenbewertung.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class UploadController {
	
	@FXML
	public Button next;
	
	@FXML
	public Button cancel;
	
	@FXML
	public Label upload_pending;
	
	@FXML 
	public Label link;
	
	@FXML
	public ProgressIndicator progress;
	
	private MainApp mainapp;

	public MainApp getMainapp() {
		return mainapp;
	}

	public void setMainapp(MainApp mainapp) {
		this.mainapp = mainapp;
	}
	
	public String getUpload_pending() {
		return upload_pending.getText();
	}

	public void setUpload_pending(String upload_pending) {
		this.upload_pending.setText(upload_pending);
	}

	public String getLink() {
		return link.getText();
	}

	public void setLink(String link) {
		this.link.setText(link);
	}
	
}
