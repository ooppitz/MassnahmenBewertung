package de.azubiag.MassnahmenBewertung.UI.test;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConcurrencyTest_2 extends Application {

	@Override
	public void start(Stage primaryStage) {

		Task<Void> task = new Task<Void>() {

			@Override
			public Void call() throws Exception {
				for (int i = 7 ; i > 0; i--) {
					updateMessage("Bitte warten ... (" + i + ")");
					Thread.sleep(500);
				}
				updateMessage("-> link <-");
				return null ;
			}
		};

		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();

		Label label =  new Label();
		label.textProperty().bind(task.messageProperty());

		VBox root = new VBox(label);
		root.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, 250, 250);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}