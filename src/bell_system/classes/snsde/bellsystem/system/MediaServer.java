//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023

package snsde.bellsystem.system;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import snsde.bellsystem.utils.Notification;

public class MediaServer {

    public Media media;
    public MediaPlayer mediaPlayer;
    public File playingfile;
    public boolean playing = false;
    public boolean pause = false;
    public boolean played = false;

    public Timer pTimer;
    public TimerTask playerTask;
    public int i;

    public Notification nf;

    public final PseudoClass STATE_ACCENT = PseudoClass.getPseudoClass("accent");
    public final PseudoClass STATE_DANGER = PseudoClass.getPseudoClass("danger");
    public final PseudoClass STATE_WARN = PseudoClass.getPseudoClass("warning");

    public MediaServer() {
        nf = new Notification();
    }

    public void playMedia(Label pathIn, ProgressBar pbar, Slider vSlider, Label lengthlbl, Label ctimelbl, Button playBtn, ComboBox<String> sBox, Label msglbl) {
        if (playing == false && pause == false) {
            if (pathIn.getText().isEmpty()) {
                nf.showmsg("e", "No Media File Selected", msglbl);
            } else {
                playingfile = new File(pathIn.getText());
                if (playingfile.exists() && playingfile.canRead()) {
                    playing = true;
                    pause = false;
                    media = new Media(playingfile.toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    beginTimer(pbar, ctimelbl, playBtn, lengthlbl, msglbl);
                    changeSpeed(null, sBox);
                    mediaPlayer.setVolume(vSlider.getValue() * 0.01);
                    try {
                        AudioFile audioFile = AudioFileIO.read(playingfile);
                        long duration = audioFile.getAudioHeader().getTrackLength();
                        NumberFormat formatter = new DecimalFormat("00");
                        String minutes = formatter.format(duration / 60);
                        String seconds = formatter.format(duration % 60);
                        lengthlbl.setText(minutes + ":" + seconds);
                        System.out.println("Total Duration: " + minutes + ":" + seconds);

                        mediaPlayer.play();
                        playBtn.setGraphic(new FontIcon(Feather.PAUSE));
                        played = false;
                        nf.showmsg("i", "Media File Playing", msglbl);
                        Utils.log("INFO","MediaServer", "Playing Media" + mediaPlayer.getMedia().getSource());

                    } catch (IOException | CannotReadException | InvalidAudioFrameException | TagException | ReadOnlyFileException e) {
                        Utils.log("WARN", "MediaServer", "Unable to load Media" + mediaPlayer.getMedia().getSource());
                    }
                } else {
                    pathIn.pseudoClassStateChanged(STATE_WARN, true);
                    nf.showmsg("w", "Media File does not exists", msglbl);
                }
            }
        } else if (playing = true && pause == false) {
            playing = false;
            pause = true;
            cancelTimer();
            mediaPlayer.pause();
            playBtn.setGraphic(new FontIcon(Feather.PLAY));
            nf.showmsg("i", "Media File Paused", msglbl);
            Utils.log("INFO","MediaServer", "Paused Media" + mediaPlayer.getMedia().getSource());
        } else {
            playing = true;
            pause = false;
            beginTimer(pbar, ctimelbl, playBtn, lengthlbl, msglbl);
            changeSpeed(null, sBox);
            mediaPlayer.setVolume(vSlider.getValue() * 0.01);
            mediaPlayer.play();
            playBtn.setGraphic(new FontIcon(Feather.PAUSE));
            played = false;
            nf.showmsg("i", "Media File Playing", msglbl);
            Utils.log("INFO","MediaServer", "Playing Media" + mediaPlayer.getMedia().getSource());
        }
    }

    public void resetMedia(ProgressBar pbar, Button playBtn, Label lenghtlbl, Label ctimelbl, Label msglbl) {
        try {
            cancelTimer();
            mediaPlayer.seek(Duration.seconds(0));
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playing = false;
            pause = false;
            Platform.runLater(() -> {
                pbar.setProgress(0);
                playBtn.setGraphic(new FontIcon(Feather.PLAY));
                lenghtlbl.setText("00:00");
                ctimelbl.textProperty().unbind();
                ctimelbl.setText("00:00");
                if (played) {
                    nf.showmsg("i", "Media File Played", msglbl);
                } else {
                    nf.showmsg("i", "Media File Resetted", msglbl);
                }
            });
            Utils.log("INFO", "MediaServer", "Resetted Media" + mediaPlayer.getMedia().getSource());
        } catch (Exception ignore) {
            Utils.log("WARN", "MediaServer", "Unable to Reset Media" + mediaPlayer.getMedia().getSource());
        }
    }

    public void changeSpeed(ActionEvent event, ComboBox<String> sBox) {
        try {
            if (sBox.getValue() == null) {
                mediaPlayer.setRate(1);
            } else {
                mediaPlayer.setRate(Integer.parseInt(sBox.getValue().substring(0, sBox.getValue().length() - 1)) * 0.01);
            }
        } catch (NumberFormatException ignore) {
            Utils.log("WARN", "MediaServer", "Unable to Change Speed of Media" + mediaPlayer.getMedia().getSource());
        } catch (Exception ignore) {
            Utils.log("WARN", "MediaServer", "Unable to Change Speed of Media" + mediaPlayer.getMedia().getSource());
        }
    }

    public void changeVolume(Slider vSlider) {
        try {
            mediaPlayer.setVolume(vSlider.getValue() * 0.01);
        } catch (Exception ignore) {
            Utils.log("WARN", "MediaServer", "Unable to Change Volume of Media" + mediaPlayer.getMedia().getSource());
        }
    }

    public void beginTimer(ProgressBar pbar, Label ctimelbl, Button playBtn, Label lengthlbl, Label msglbl) {
        pTimer = new Timer();
        playerTask = new TimerTask() {
            @Override
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                Platform.runLater(() -> {
                    pbar.setProgress(current / end);
                });
                Platform.runLater(() -> {
                    c_time(ctimelbl);
                });
                if (current / end == 1) {
                    cancelTimer();
                    resetMedia(pbar, playBtn, lengthlbl, ctimelbl, msglbl);
                    mediaPlayer.seek(Duration.seconds(0));
                    mediaPlayer.stop();
                    played = true;
                }
            }
        };
        pTimer.scheduleAtFixedRate(playerTask, 0, 1000);

    }

    public void cancelTimer() {
        pTimer.cancel();
    }

    public void c_time(Label ctimelbl) {
        ctimelbl.textProperty().bind(
                Bindings.createStringBinding(() -> {
                    Duration time = mediaPlayer.getCurrentTime();
                    return String.format("%02d:%02d",
                            (int) time.toMinutes(),
                            (int) time.toSeconds()
                    );
                },
                        mediaPlayer.currentTimeProperty()));

    }

    private static class InstanceHolder {

        private static final MediaServer INSTANCE = new MediaServer();
    }

    public static MediaServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
