package application;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MediaBar extends HBox {
	Slider timeSlider = new Slider();
	Slider volSlider = new Slider();
	Button playButton = new Button("||");
	Button stopButton = new Button("Stop");
	Button restartButton = new Button("Restart");
	Label volume = new Label("Volume: ");
	Label timeLabel = new Label();
	MediaPlayer mediaPlayer;

	public MediaBar(MediaPlayer player) {
		this.mediaPlayer = player;

		setAlignment(Pos.CENTER);
		setPadding(new Insets(5, 10, 5, 10));
		volSlider.setPrefWidth(70);
		volSlider.setMinWidth(30);
		volSlider.setValue(100);
		playButton.setPrefWidth(30);
		timeLabel.setText("  00:00:00 / 00:00:00");
		HBox.setHgrow(timeSlider, Priority.ALWAYS);

		getChildren().add(stopButton);
		getChildren().add(playButton);
		getChildren().add(restartButton);
		getChildren().add(timeLabel);
		getChildren().add(timeSlider);
		getChildren().add(volume);
		getChildren().add(volSlider);

		playButton.setOnAction(e -> {
			Status status = MediaBar.this.mediaPlayer.getStatus(); // To get the status of Player
			if (status == status.PLAYING) {

				if (MediaBar.this.mediaPlayer.getCurrentTime().greaterThanOrEqualTo(MediaBar.this.mediaPlayer.getTotalDuration())) {

					MediaBar.this.mediaPlayer.seek(MediaBar.this.mediaPlayer.getStartTime()); // Restart the video
					MediaBar.this.mediaPlayer.play();
				} else {
					MediaBar.this.mediaPlayer.pause();

					playButton.setText(">");
				}
			}
			if (status == Status.HALTED || status == Status.STOPPED || status == Status.PAUSED) {
				MediaBar.this.mediaPlayer.play();
				playButton.setText("||");
			}
		});

		stopButton.setOnAction(e -> {
			Status status = MediaBar.this.mediaPlayer.getStatus(); // To get the status of Player
			if (status == status.PLAYING) {
				MediaBar.this.mediaPlayer.pause();
				MediaBar.this.mediaPlayer.seek(Duration.millis(0));
			} else if (status == Status.HALTED || status == Status.STOPPED || status == Status.PAUSED) {
				MediaBar.this.mediaPlayer.seek(Duration.millis(0));
			}
			playButton.setText(">");
			updateTimeLabel();
			timeSlider.setValue(0);
		});


		stopButton.setOnAction(e -> stopPlayer());
		restartButton.setOnAction(e -> {
			stopPlayer();
			mediaPlayer.play();
			playButton.setText("||");
		});


		this.mediaPlayer.currentTimeProperty().addListener(ov -> updatesValues());

		timeSlider.valueProperty().addListener(ov -> {
			if (timeSlider.isPressed()) {
				MediaBar.this.mediaPlayer.seek(MediaBar.this.mediaPlayer.getMedia().getDuration().multiply(timeSlider.getValue() / 100));
			}
		});

		volSlider.valueProperty().addListener(ov -> {
			if (volSlider.isPressed()) {
				MediaBar.this.mediaPlayer.setVolume(volSlider.getValue() / 100);
			}
		});
	}

	private void stopPlayer() {
		mediaPlayer.pause();
		mediaPlayer.seek(Duration.millis(0));
		playButton.setText(">");
		updateTimeLabel();
		timeSlider.setValue(0);
	}

	protected void updatesValues() {
		Platform.runLater(() -> {
			timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis() / mediaPlayer.getTotalDuration().toMillis() * 100);
			updateTimeLabel();
		});
	}

	private void updateTimeLabel() {
		LocalDateTime totalDuration = Instant.ofEpochMilli((long) mediaPlayer.getTotalDuration().toMillis()).atZone(ZoneId.of("UTC")).toLocalDateTime();
		LocalDateTime currentTime = Instant.ofEpochMilli((long) mediaPlayer.getCurrentTime().toMillis()).atZone(ZoneId.of("UTC")).toLocalDateTime();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		timeLabel.setText("  " + formatter.format(currentTime) + " / " + formatter.format(totalDuration));
	}
}