package com.daniel.reproductor.controller;

import com.daniel.reproductor.audio.AudioPlayer;
import com.daniel.reproductor.audio.AudioRecorder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReproductorController implements Initializable {

    //Reproducir
    private final FileChooser fileChooser = new FileChooser();
    @FXML
    private Label lbNombre;
    @FXML
    private TextField tfSrc;
    @FXML
    private Slider slVolume;
    @FXML
    private ProgressBar songProgress;
    private List<File> songs;
    private int actualSong = 0;
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final AudioRecorder audioRecorder = new AudioRecorder();
    @FXML
    void getFile(){
        songs = fileChooser.showOpenMultipleDialog(new Stage());
        if (songs != null){
            if (songs.size() == 1){
                tfSrc.setText(songs.get(0).getName());
            }
            if (songs.size() > 1){
                tfSrc.setText("Multiple songs selected.");
            }
        }
    }

    @FXML
    void playAudio() {
        if (!songs.isEmpty()) {
            try {
                if (audioPlayer.isPlaying()){
                    audioPlayer.stopSound();
                }
            } catch (LineUnavailableException | IOException e) {
                System.err.println(e.getMessage());
            }
            File file = new File(songs.get(actualSong).getAbsolutePath());
            lbNombre.setText("Sonando: " + songs.get(actualSong).getName());
            Thread t = new Thread(() -> {
                try {
                    audioPlayer.playSound(file);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            });
            t.start();
            while (true) {
                try {
                    if (!audioPlayer.isPlaying()){
                        t.interrupt();
                        break;
                    }
                } catch (LineUnavailableException | IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    @FXML
    void stopAudio(){
        try {
            if (audioPlayer.isPlaying()){
                audioPlayer.stopSound();
            }
        } catch (IOException | LineUnavailableException ex) {
            System.err.println(ex.getMessage());
        }
    }
    @FXML
    void previousAudio(){
        actualSong--;
        if (actualSong < 0) actualSong = songs.size() - 1;
        playAudio();
    }
    @FXML
    void nextAudio(){
        actualSong++;
        if (actualSong >= songs.size()) actualSong = 0;
        playAudio();
    }

    //Grabar

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        songs.add(new File("src/main/resources/media/BeepBox-Song.wav"));
        fileChooser.setInitialDirectory(new File("src/main/resources/media"));
    }
}