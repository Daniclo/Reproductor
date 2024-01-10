package com.daniel.reproductor.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;
    public void playSound(File file) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        this.clip = clip;
        clip.open(audioStream);
        clip.start();
        Thread.sleep(clip.getMicrosecondLength() / 1000);
        clip.close();
        audioStream.close();
    }
    public void stopSound(){
        if (clip != null){
            clip.stop();
            clip.close();
        }
    }
    public boolean isPlaying() throws LineUnavailableException, IOException {
        if (clip != null){
            return clip.isActive();
        }
        return false;
    }
    public void volume(){
        Control[] controls = clip.getControls();
    }
}