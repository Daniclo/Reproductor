package com.daniel.reproductor.audio;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class AudioRecorder {
    private Mixer dispositivoEntrada;
    public TargetDataLine lineaEntrada;

    public void recordSound(Line.Info info, Mixer dispositivo, String ruta, String nombre) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        dispositivoEntrada = dispositivo;
        lineaEntrada = (TargetDataLine) dispositivoEntrada.getLine(info);
        lineaEntrada.open();

        lineaEntrada.start();
        AudioInputStream recordingStream = new AudioInputStream(lineaEntrada);
        File fileOut = new File(ruta + "\\" + nombre + ".wav");
        AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
        if (!AudioSystem.isFileTypeSupported(fileType,recordingStream)){
            System.err.println("Unsupported file type");
            return;
        }
        Thread t = new Thread(() ->{
            try {
                System.out.println("Grabando");
                AudioSystem.write(recordingStream,fileType,fileOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
    }
}