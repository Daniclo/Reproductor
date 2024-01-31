package com.daniel.reproductor.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioPlayer {
    private Mixer dispositivoSalida;
    private Clip lineaSalidaClip;
    private SourceDataLine lineaSalidaSDL;

    public void playSoundByClip(File file, Line.Info info, Mixer dispositivo) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        dispositivoSalida = dispositivo;
        lineaSalidaClip = (Clip) dispositivoSalida.getLine(info);
        lineaSalidaClip.open(audioStream);
        lineaSalidaClip.start();
        Thread.sleep(lineaSalidaClip.getMicrosecondLength() / 1000);
        lineaSalidaClip.close();
        audioStream.close();
    }
    public void playSoundBySourceDataLine(File file,Line.Info info, Mixer dispositivo) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        dispositivoSalida = dispositivo;
        lineaSalidaSDL = (SourceDataLine) dispositivoSalida.getLine(info);
        lineaSalidaSDL.open(audioStream.getFormat());
        lineaSalidaSDL.start();
        lineaSalidaSDL.write(audioStream.readAllBytes(),0,audioStream.readAllBytes().length);
    }
    public void stopSound(){
        if (lineaSalidaClip != null){
            lineaSalidaClip.close();
        }
        if (lineaSalidaSDL != null){
            lineaSalidaSDL.close();
        }
    }
    public boolean isPlaying(){
        if (lineaSalidaClip != null) return lineaSalidaClip.isOpen();
        else if (lineaSalidaSDL != null) return lineaSalidaSDL.isOpen();
        else return false;
    }
    public void pruebas(){
        //Controls son Gain 0, Mute 1, Balance 2 y Pan 3
        Mixer.Info[] info = AudioSystem.getMixerInfo();
        Mixer mixer = AudioSystem.getMixer(info[0]);
        Line.Info[] info2 = mixer.getSourceLineInfo();
        Arrays.stream(info2).forEach(System.out::println);
    }
    public List<Mixer.Info> getDispositivos(){
        Mixer.Info[] info = AudioSystem.getMixerInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
    public Mixer getDispositivo(Mixer.Info info){
        return AudioSystem.getMixer(info);
    }
    public List<Line.Info> getLineasEntrada(Mixer mixer){
        Line.Info[] info = mixer.getSourceLineInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
    public List<Line.Info> getLineasSalida(Mixer mixer){
        Line.Info[] info = mixer.getTargetLineInfo();
        return new ArrayList<>(Arrays.asList(info));
    }
}