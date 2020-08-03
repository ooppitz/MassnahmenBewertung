package de.azubiag.MassnahmenBewertung.UI.test;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConcurrencyTest_2 extends Application {

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {

//		ObservableValue<boolean> ob = new ObservableValueBase<boolean>(true) {
//
//			@Override
//			public T getValue() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
		
		
		BooleanProperty dp = new SimpleBooleanProperty();
		
		dp.addListener(new ChangeListener(){
			@Override public void changed(ObservableValue o, Object oldVal, Object newVal){
				System.out.println("change");
			}
		});

		Task<Void> task = new Task<Void>() {

			@Override
			public Void call() throws Exception {
				for (int i = 7 ; i > 0; i--) {
					updateMessage("Bitte warten ... (" + i + ")");
					Thread.sleep(300);
				}
				updateMessage("-> link <-");
				
				dp.set(true);
				
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