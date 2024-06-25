package audio;

import gameStates.GameState;
import main.Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {
    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int JUMP_LAND = 2;
    public static int GAMEOVER = 3;
    public static int LVL_COMPLETED = 4;
    public static int FALLING_BOMB = 5;
    public static int EXPLODE = 6;
    public static int ATTACK_ONE = 7;
    public static int ATTACK_TWO = 8;
    public static int ATTACK_THREE = 9;
    public static int COLLECT_POTION = 10;
    public static int COLLECT_COIN = 11;

    private Clip[] songs, effects;
    private int currentSongId = 0;
    private float volume = 1f;
    private boolean songMute, effectMute;
    private Random random = new Random();

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        if (GameState.state == GameState.MENU) playSong(MENU_1);
        else setLevelSong(0);
    }

    private void loadSongs() {
        String[] names = {"menu", "game-music-loop", "level2"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
    }

    private void loadEffects() {
        String[] effectNames = {
                "die", "jump", "jumpland",
                "gameover", "lvlcompleted",
                "falling-bomb", "explode",
                "attack1", "attack2", "attack3",
                "energy-up", "collect-coin"
        };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(effectNames[i]);
        }
        updateEffectsVolume();
    }

    private void CheckFileType(String name) {
        String audioFilePath = "res/Audio/" + name + ".wav";

        try {
            File audioFile = new File(audioFilePath);
            AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(audioFile);

            // Print format details
            System.out.println("File type: " + audioFileFormat.getType());
            System.out.println("Format: " + audioFileFormat.getFormat());
            System.out.println("Frame length: " + audioFileFormat.getFrameLength());
            System.out.println("Frame rate: " + audioFileFormat.getFormat().getFrameRate());
            System.out.println("Channels: " + audioFileFormat.getFormat().getChannels());
            System.out.println("Encoding: " + audioFileFormat.getFormat().getEncoding());

        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/Audio/" + name + ".wav");
//        CheckFileType(name);
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
//            System.out.println("Check: " + url + "\n");
            throw new RuntimeException(e);
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void gameOver() {
        stopSong();
        playEffect(GAMEOVER);
    }

    public void playAttackSound() {
        int start = 7;
        start += random.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public boolean isSameSong(int song) {
        return currentSongId == song && songs[currentSongId].isActive();
    }

    public void playSong(int song) {
        if (!isSameSong(song)) {
            stopSong();
            currentSongId = song;
            updateSongVolume();
            songs[currentSongId].setMicrosecondPosition(0);
            songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }
    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute) playEffect(JUMP);
    }

    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
