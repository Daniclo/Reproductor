package com.daniel.reproductor.controller;

import com.daniel.reproductor.audio.AudioPlayer;
import com.daniel.reproductor.audio.AudioRecorder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.*;
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
    private boolean looping = false;
    private final AudioPlayer audioPlayer = new AudioPlayer();
    private final AudioRecorder audioRecorder = new AudioRecorder();
    @FXML
    void showControls(){
        audioPlayer.pruebas();
    }
    @FXML
    void getFile(){
        songs = fileChooser.showOpenMultipleDialog(new Stage());
        if (songs != null){
            if (songs.size() == 1){
                tfSrc.setText(songs.get(0).getAbsolutePath());
            }
            if (songs.size() > 1){
                tfSrc.setText("Multiple songs selected.");
            }
        }
    }

    @FXML
    void playAudio() {
        lbNombre.setText("Reproduciendo " + songs.get(actualSong).getName());
        if (audioPlayer.isPlaying()){
            audioPlayer.stopSound();
        }
        Thread t = new Thread(() -> {
            try {
                audioPlayer.playSound(songs.get(actualSong), cbLineaSalida.getValue(), audioPlayer.getDispositivo(cbDispositivosSalida.getValue()));
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
                System.err.println(e.getMessage());
            }
        });
        t.start();
    }
    @FXML
    void stopAudio(){
        if (audioPlayer.isPlaying()){
            audioPlayer.stopSound();
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
    //Cosas de la pestaña grabar

    //Producir
    //Cosas de la pestaña producir

    //Publicar
    //Cosas de la pestaña publicar

    //Ajustes
    @FXML
    ChoiceBox<Mixer.Info> cbDispositivosSalida;
    @FXML
    ChoiceBox<Mixer.Info> cbDispositivoEntrada;
    @FXML
    ChoiceBox<Line.Info> cbLineaSalida;
    @FXML
    ChoiceBox<Line.Info> cbLineaEntrada;
    public void updateSourceLines(){
        cbLineaSalida.getItems().clear();
        cbLineaSalida.getItems().addAll(audioPlayer.getLineasEntrada(audioPlayer.getDispositivo(cbDispositivosSalida.getValue())));
    }
    public void updateTargetLines(){
        cbLineaEntrada.getItems().clear();
        cbLineaEntrada.getItems().addAll(audioPlayer.getLineasSalida(audioPlayer.getDispositivo(cbDispositivoEntrada.getValue())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        songs = new ArrayList<>();
        songs.add(new File("src/main/resources/media/BeepBox-Song.wav"));
        fileChooser.setInitialDirectory(new File("src/main/resources/media"));
        cbDispositivosSalida.getItems().addAll(audioPlayer.getDispositivos());
        cbDispositivosSalida.setOnAction(t -> updateSourceLines());
        cbDispositivoEntrada.getItems().addAll(audioPlayer.getDispositivos());
        cbDispositivoEntrada.setOnAction(t -> updateTargetLines());
    }
}