package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class Main extends Application {
	Player player;
	FileChooser fileChooser;

	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		MenuItem open = new MenuItem("Open");
		Menu menuFile = new Menu("File");
		MenuBar menu = new MenuBar();
		menuFile.getItems().add(open);
		menu.getMenus().add(menuFile);

		fileChooser = new FileChooser();

		open.setOnAction(event -> {
			if (player.mediaPlayer !=null) {
				player.mediaPlayer.pause();
			}
			File file = fileChooser.showOpenDialog(primaryStage);

			// Choosing the file to play
			if (file != null) {
				try {
					player = new Player(file.toURI().toURL().toExternalForm());
					Scene scene2 = new Scene(player, 1280, 760, Color.BLACK);
					primaryStage.setScene(scene2);
				}
				catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});

		File defaultFile = new File("video/sample.mp4");
		player = new Player(defaultFile.toURI().toURL().toExternalForm());
		player.setTop(menu);
		Scene scene = new Scene(player, 1280, 780, Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
