package de.azubiag.MassnahmenBewertung.UI;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class UploadController {
	
	
	
	@FXML
	public Label upload_pending;
	
	@FXML 
	public Hyperlink link;
	
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
