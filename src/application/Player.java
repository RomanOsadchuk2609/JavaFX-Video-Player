package application;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class Player extends BorderPane {
	Media media;
	MediaPlayer mediaPlayer;
	MediaView mediaView;
	Pane pane;
	MediaBar bar;

	public Player(String filePath) {
		media = new Media(filePath);
		mediaPlayer = new MediaPlayer(media);
		mediaView = new MediaView(mediaPlayer);
		mediaView.setFitHeight(1280);
		mediaView.setFitHeight(720);
		pane = new Pane();
		setCenter(pane);
		pane.getChildren().add(mediaView);

		bar = new MediaBar(mediaPlayer);
		setBottom(bar);
		mediaPlayer.play();
	}

	public Player() {pane = new Pane();
		setCenter(pane);

//		media = new Media(filePath);
//		mediaPlayer = new MediaPlayer(media);
//		mediaView = new MediaView(mediaPlayer);
		pane = new Pane();
//		pane.getChildren().add(mediaView);
		setCenter(pane);

		bar = new MediaBar(mediaPlayer);
		setBottom(bar);
		mediaPlayer.play();
	}

}