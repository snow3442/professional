package client.uicomponents;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.MalformedURLException;

/**
 * This class is made singleton and provides all audio utilities for
 * the game, it has 2 Media Players, mediaPlayer for long lasting background
 * music and gameSoundMediaPlayer for game events sounds
 */
public class MediaControl {

    private static MediaControl instance = null;
    private MediaPlayer mediaPlayer;
    private MediaPlayer gameSoundMediaPlayer;
    //Singleton pattern
    private MediaControl() {

    }

    public static synchronized MediaControl getInstance() {
        if (instance == null) {
            instance = new MediaControl();
        }
        return instance;
    }

    /**
     * uses a background daemon thread to play sound
     * thread will close upon exiting program
     * @param url {String} the path of the sound file to play
     */
    public void play(String url) {
        Thread backgroundThread = new Thread(() -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            File file = new File(url);
            Media media = null;
            try {
                media = new Media(file.toURI().toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true);
            mediaPlayer.setCycleCount(AudioClip.INDEFINITE);
            mediaPlayer.play();
        });
        backgroundThread.setDaemon(true);
        backgroundThread.start();

    }

    /**
     * Play in game events sounds
     * @param url {String} the url of the game sound
     */
    public void playGameSound(String url){
        if (gameSoundMediaPlayer != null) {
            gameSoundMediaPlayer.stop();
        }
        File file = new File(url);
        Media media = null;
        try {
            media = new Media(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        gameSoundMediaPlayer = new MediaPlayer(media);
        gameSoundMediaPlayer.setAutoPlay(true);
        //making sure sounds are playing sequentially
        gameSoundMediaPlayer.play();
    }

    /**
     * Pause the background mediaPlayer for switching sound or
     * just completely stopping background music
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * resume whatever song the media player was playing
     */
    public void resume(){
        mediaPlayer.play();
    }


}
