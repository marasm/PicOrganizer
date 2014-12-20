package com.pic.organizer;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,640,480);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("PicOrganizer");
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("images/icon_16x16.png"));
			primaryStage.getIcons().add(new Image("images/icon_32x32.png"));
			primaryStage.getIcons().add(new Image("images/icon_48x48.png"));
			primaryStage.getIcons().add(new Image("images/icon_64x64.png"));
			primaryStage.getIcons().add(new Image("images/icon_128x128.png"));
			primaryStage.getIcons().add(new Image("images/icon_256x256.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
