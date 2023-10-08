package dev.monospace.plane_ahead;

// Java program to play an Audio
// file using Clip Object
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.*;

public class Audio
{

    // to store current position
    Long currentFrame;
    Clip clip;

    String status;
    String filePath;

    AudioInputStream audioInputStream;

    // constructor to initialize streams and clip
    public Audio(String filePath)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        this.filePath = filePath;
        // create AudioInputStream object
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // create clip reference

        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-20.0f); // Reduce volume by 10 decibels.

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.stop();
    }

    // Method to play the audio
    public void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }
}