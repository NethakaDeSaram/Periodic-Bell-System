//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023
package snsde.bellsystem.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import snsde.bellsystem.utils.NodeUtils;
import snsde.bellsystem.utils.Notification;
import snsde.bellsystem.utils.Res;
import snsde.bellsystem.utils.splashScreen;

public class SystemB extends AnchorPane {

    public static final int APPWIDTH = 940;
    public int[] speeds = {75, 100, 125};
    public final int defaultspeed = 1;

    //Title Section
    public Label Mlbl;
    public Label mlbl1;
    public Label mlbl2;
    //Sch Pane
    public Separator sp1;
    public Button schBtn;
    public Label stlbl;
    public ProgressBar stpb;
    public String stready = "System Ready";
    public String ststarted = "System Started";
    public String ststopped = "System Stopped";
    public String stTxt = "";
    //Player Section
    public Label Flbl;
    public Label pathIn;
    public HBox pathBox;
    public Button playBtn;
    public Button stopBtn;
    public Label speedlbl;
    public ComboBox sBox;
    public Label volumelbl;
    public Slider vSlider;
    public Label ctimelbl;
    public ProgressBar pBar;
    public Label lengthlbl;
    //time Section
    public Separator sp2;
    public Label timelbl;
    public Separator sp3;
    //bell Section
    public ListView<Bell> bList;
    public Label bellTitle;
    public Label timeProplbl;
    public TextField timeIn;
    public Label pathProplbl;
    public TextField pathIn2;
    public Button brBtn;
    public Button addBtn;
    public Button delBtn;
    public Button upBtn;
    //footer
    public Separator sp4;
    public ToggleButton shutBtn;
    public Label vlbl;
    public Label blbl;
    public Label msglbl;
    public Button AboutBtn;

    public Notification nf = new Notification();
    public MediaServer ms;

    public Timer schTimer;
    public TimerTask schTask;
    public String cTime;
    public DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public ObservableList<Bell> bellArray;
    public List<Bell> loadedBells;

    public boolean schOn = false;
    public boolean shutaf = false;

    public final PseudoClass STATE_ACCENT = PseudoClass.getPseudoClass("accent");

    public Clipboard clipboard = Clipboard.getSystemClipboard();
    public ClipboardContent content = new ClipboardContent();

    public final void createInterface() {
        topPane();
        playerPane();
        schPane();
        timePane();
        bellPane();
        footPane();
    }

    public final void topPane() {
        Mlbl = new Label("Periodic Bell System");
        Mlbl.prefHeight(85.0);
        Mlbl.getStyleClass().add("mlbl");
        NodeUtils.setAnchors(Mlbl, new Insets(0, 370, 605, 0));
        mlbl1 = new Label("Designed and Developed by SNS_DE_SARAM");
        mlbl1.getStyleClass().add("mlbl1");
        NodeUtils.setAnchors(mlbl1, new Insets(23, 9, 647, 631));
        mlbl2 = new Label("Contact : snsdesaram@gmail.com");
        mlbl2.getStyleClass().add("mlbl2");
        NodeUtils.setAnchors(mlbl2, new Insets(42, 10, 628, 630));

        getChildren().addAll(Mlbl, mlbl1, mlbl2);
    }

    public final void playerPane() {
        Flbl = new Label("Current File :");
        Flbl.prefHeight(33);
        Flbl.prefWidth(93);
        Flbl.getStyleClass().add("default");
        NodeUtils.setAnchors(Flbl, new Insets(104, 821, 553, 26));

        pathIn = new Label();
        pathIn.prefHeight(33);
        pathIn.prefWidth(530);
        pathIn.getStyleClass().add("path-in");
        NodeUtils.setAnchors(pathIn, new Insets(103, 284, 554, 126));
        pathIn.setOnMouseEntered((var e) -> {
            var ptp = new Tooltip(pathIn.getText());
            pathIn.setTooltip(ptp);
        });
        pathIn.pseudoClassStateChanged(STATE_ACCENT, true);

        stopBtn = new Button(null, new FontIcon(Feather.SQUARE));
        stopBtn.prefHeight(53);
        stopBtn.prefWidth(64);
        stopBtn.setOnAction((var e)
                -> ms.resetMedia(pBar, playBtn, lengthlbl, ctimelbl, msglbl)
        );
        NodeUtils.setAnchors(stopBtn, new Insets(153, 722, 484, 154));

        speedlbl = new Label("Speed :");
        speedlbl.prefHeight(33);
        speedlbl.prefWidth(42);
        speedlbl.getStyleClass().add("default");
        NodeUtils.setAnchors(speedlbl, new Insets(149, 633, 521, 242));

        sBox = new ComboBox<String>();
        sBox.prefHeight(17);
        sBox.prefWidth(183);
        for (int i = 0; i < speeds.length; i++) {
            sBox.getItems().add(Integer.toString(speeds[i]) + "%");
        }
        sBox.getSelectionModel().select(defaultspeed);
        sBox.setOnAction((var e)
                -> ms.changeSpeed((ActionEvent) e, sBox)
        );
        NodeUtils.setAnchors(sBox, new Insets(174, 520, 484, 237));

        volumelbl = new Label("Volume :");
        volumelbl.prefHeight(17);
        volumelbl.prefWidth(65);
        volumelbl.getStyleClass().add("default");
        NodeUtils.setAnchors(volumelbl, new Insets(149, 437, 521, 438));

        vSlider = new Slider(0, 100, 50);
        vSlider.prefWidth(193);
        vSlider.getStyleClass().add("small");
        vSlider.getStyleClass().add("vSlider");
        vSlider.valueProperty().addListener((ObservableValue<? extends Number> arg0, Number arg1, Number arg2) -> {
            ms.changeVolume(vSlider);
        });
        NodeUtils.setAnchors(vSlider, new Insets(170, 314, 474, 433));

        ctimelbl = new Label("00:00");
        ctimelbl.prefWidth(80);
        ctimelbl.getStyleClass().add("default");
        NodeUtils.setAnchors(ctimelbl, new Insets(233, 866, 436, 32));

        pBar = new ProgressBar();
        pBar.getStyleClass().add("progress-bar");
        NodeUtils.setAnchors(pBar, new Insets(240, 324, 444, 76));
        pBar.setProgress(0);

        lengthlbl = new Label("00:00");
        lengthlbl.prefWidth(80);
        lengthlbl.getStyleClass().add("default");
        NodeUtils.setAnchors(lengthlbl, new Insets(233, 276, 436, 622));

        playBtn = new Button(null, new FontIcon(Feather.PLAY));
        playBtn.prefHeight(53);
        playBtn.prefWidth(64);
        playBtn.setOnAction((var e)
                -> ms.playMedia(pathIn, pBar, vSlider, lengthlbl, ctimelbl, playBtn, sBox, msglbl)
        );
        NodeUtils.setAnchors(playBtn, new Insets(153, 795, 484, 81));

        getChildren().addAll(Flbl, pathIn, playBtn, stopBtn, speedlbl, sBox, volumelbl, vSlider, ctimelbl, pBar, lengthlbl);
    }

    public void schPane() {
        sp1 = new Separator();
        sp1.setOrientation(Orientation.VERTICAL);
        sp1.prefHeight(154);
        sp1.prefWidth(29);
        NodeUtils.setAnchors(sp1, new Insets(97, 251, 439, 660));

        schBtn = new Button("START");
        schBtn.prefHeight(76);
        schBtn.prefWidth(176);
        NodeUtils.setAnchors(schBtn, new Insets(100, 47, 514, 716));
        schBtn.getStyleClass().add("sch-btn");
        schBtn.setOnAction(e
                -> schState()
        );
        schBtn.requestFocus();

        stlbl = new Label(stready);
        stlbl.prefHeight(20);
        stlbl.prefWidth(176);
        stlbl.getStyleClass().add("stlbl");
        NodeUtils.setAnchors(stlbl, new Insets(185, 45, 485, 718));

        stpb = new ProgressBar();
        stpb.setProgress(0);
        NodeUtils.setAnchors(stpb, new Insets(221, 32, 457, 706));

        getChildren().addAll(sp1, schBtn, stlbl, stpb);
    }

    public void timePane() {
        sp2 = new Separator();
        sp2.setOrientation(Orientation.HORIZONTAL);
        sp2.prefHeight(29);
        sp2.prefWidth(413);
        NodeUtils.setAnchors(sp2, new Insets(270, 33, 391, 494));

        timelbl = new Label("00:00:00");
        timelbl.prefWidth(413);
        timelbl.prefHeight(139);
        timelbl.getStyleClass().add("timelbl");
        NodeUtils.setAnchors(timelbl, new Insets(278, 29, 273, 498));

        sp3 = new Separator();
        sp3.setOrientation(Orientation.HORIZONTAL);
        sp3.prefHeight(29);
        sp3.prefWidth(413);
        NodeUtils.setAnchors(sp3, new Insets(398, 33, 263, 494));

        getChildren().addAll(sp2, timelbl, sp3);
    }

    public void bellPane() {
        bList = new ListView<>(bellArray);
        bList.prefHeight(334);
        bList.prefWidth(449);
        bList.getStyleClass().add("dense");
        bList.setOnMouseClicked((var e) -> {
            Bell selectedBell = bList.getSelectionModel().getSelectedItem();
            if (selectedBell != null) {
                int selectedIndex = bellArray.indexOf(selectedBell);
                timeIn.setText(bellArray.get(selectedIndex).getBTime());
                pathIn.setText(bellArray.get(selectedIndex).getBPath());
                pathIn2.setText(bellArray.get(selectedIndex).getBPath());
            }
        });
        NodeUtils.setAnchors(bList, new Insets(281, 474, 75, 11));

        bellTitle = new Label("Selected Bell");
        bellTitle.getStyleClass().add("bellTitle");
        NodeUtils.setAnchors(bellTitle, new Insets(435, 302, 229, 491));

        timeProplbl = new Label("Time :");
        timeProplbl.getStyleClass().add("Caption");
        NodeUtils.setAnchors(timeProplbl, new Insets(488, 376, 181, 513));

        timeIn = new TextField();
        timeIn.setPromptText("00:00:00");
        timeIn.prefHeight(32);
        timeIn.prefWidth(330);
        timeIn.getStyleClass().add("path-in");
        NodeUtils.setAnchors(timeIn, new Insets(484, 46, 174, 564));

        pathProplbl = new Label("Path :");
        pathProplbl.getStyleClass().add("Caption");
        NodeUtils.setAnchors(pathProplbl, new Insets(528, 376, 141, 513));

        pathIn2 = new TextField();
        pathIn2.setPromptText("Media File Path...");
        pathIn2.prefHeight(32);
        pathIn2.prefWidth(294);
        pathIn2.getStyleClass().add("path-in");
        NodeUtils.setAnchors(pathIn2, new Insets(523, 83, 135, 564));
        pathIn2.requestFocus();
        pathIn2.setOnMouseEntered((var e) -> {
            var ptp = new Tooltip(pathIn2.getText());
            pathIn2.setTooltip(ptp);
        });

        brBtn = new Button(null, new FontIcon(Feather.FOLDER));
        brBtn.prefHeight(32);
        brBtn.prefWidth(20);
        NodeUtils.setAnchors(brBtn, new Insets(523, 46, 135, 862));
        brBtn.setOnAction((var e) -> {
            Utils.br_media2(pathIn, msglbl, pathIn2);
        });

        addBtn = new Button("Add Bell", new FontIcon(Feather.PLUS));
        addBtn.prefHeight(32);
        addBtn.prefWidth(114);
        NodeUtils.setAnchors(addBtn, new Insets(583, 304, 75, 522));
        addBtn.setOnAction((var e) -> {
            Bell selectedBell = bList.getSelectionModel().getSelectedItem();
            if (selectedBell != null) {
                int index = bellArray.indexOf(selectedBell);
                if (Utils.isTimeV(timeIn.getText())) {
                    bellArray.add(index + 1, getBellInfo());
                    Utils.writeBells(new ArrayList<>(bellArray));
                    nf.showmsg("i", "Bell Added", msglbl);
                    Utils.log("INFO", "System", "Added Bell ---> " + " ( " + bellArray.get(index + 1).getBTime() + " : " + bellArray.get(index + 1).getBPath() + " ) ");
                } else {
                    nf.showmsg("e", "Time Format is Wrong", msglbl);
                }
            }
        });

        delBtn = new Button("Delete Bell", new FontIcon(Feather.X_CIRCLE));
        delBtn.prefHeight(32);
        delBtn.prefWidth(114);
        NodeUtils.setAnchors(delBtn, new Insets(583, 180, 75, 646));
        delBtn.setOnAction((var e) -> {
            Bell selectedBell = bList.getSelectionModel().getSelectedItem();
            if (selectedBell != null) {
                bellArray.remove(selectedBell);
                Utils.writeBells(new ArrayList<>(bellArray));
                Utils.log("INFO", "System", "Deleted Bell ---> " + " ( " + selectedBell.getBTime() + " : " + selectedBell.getBPath() + " ) ");
            }
        });

        upBtn = new Button("Update", new FontIcon(Feather.TOOL));
        upBtn.prefHeight(32);
        upBtn.prefWidth(114);
        NodeUtils.setAnchors(upBtn, new Insets(583, 57, 75, 769));
        upBtn.setOnAction((var e) -> {
            Bell selectedBell = bList.getSelectionModel().getSelectedItem();
            if (selectedBell != null && Utils.isTimeV(timeIn.getText())) {
                Bell bell = getBellInfo();
                int selectedIndex = bellArray.indexOf(selectedBell);
                bellArray.set(selectedIndex, bell);
                Utils.writeBells(new ArrayList<>(bellArray));
                nf.showmsg("i", "Bell Updated", msglbl);
                Utils.log("INFO", "System", "Updated Bell ---> " + " ( " + selectedBell.getBTime() + " : " + selectedBell.getBPath() + " ) ");
            } else {
                if (!Utils.isTimeV(timeIn.getText())) {
                    nf.showmsg("e", "Time Format is Wrong", msglbl);
                }
            }
        });

        getChildren().addAll(bList, bellTitle, timeProplbl, timeIn, pathProplbl, pathIn2, brBtn, addBtn, delBtn, upBtn);
    }

    public void footPane() {

        sp4 = new Separator();
        sp4.setOrientation(Orientation.HORIZONTAL);
        sp4.prefHeight(25);
        sp4.prefWidth(918);
        NodeUtils.setAnchors(sp4, new Insets(624, 11, 41, 11));

        shutBtn = new ToggleButton("Shutdown After Alarm");
        shutBtn.prefHeight(32);
        shutBtn.prefWidth(193);
        shutBtn.setSelected(false);
        shutBtn.setOnAction((var e) -> {
            shutFunc();
        });
        NodeUtils.setAnchors(shutBtn, new Insets(648, 718, 10, 29));

        msglbl = new Label(null);
        msglbl.prefHeight(20);
        msglbl.prefWidth(413);
        msglbl.getStyleClass().add("msglbl");
        NodeUtils.setAnchors(msglbl, new Insets(654, 227, 16, 300));

        vlbl = new Label("v3.0.2");
        vlbl.getStyleClass().add("vlbl");
        NodeUtils.setAnchors(vlbl, new Insets(641, 59, 22, 810));

        blbl = new Label("build.2023");
        blbl.getStyleClass().add("blbl");
        NodeUtils.setAnchors(blbl, new Insets(666, 58, 3, 813));

        AboutBtn = new Button(null, new FontIcon(Feather.INFO));
        AboutBtn.prefHeight(34);
        AboutBtn.prefWidth(34);
        NodeUtils.setAnchors(AboutBtn, new Insets(648, 16, 10, 890));
        AboutBtn.setOnAction((var e) -> {
            aboutPage();
        });

        getChildren().addAll(sp4, shutBtn, msglbl, vlbl, blbl, AboutBtn);
    }

    private void starttimeServer() {
        schTimer = new Timer();
        schTask = new TimerTask() {
            @Override
            public void run() {
                cTime = timeToString();
                Platform.runLater(() -> {
                    timelbl.setText(cTime);
                });
                if (schOn) {
                    Platform.runLater(() -> {
                        for (Bell x : bellArray) {
                            if (cTime.equals(x.getBTime())) {
                                pathIn.setText(x.getBPath());
                                ms.resetMedia(pBar, playBtn, lengthlbl, ctimelbl, msglbl);
                                ms.playMedia(pathIn, pBar, vSlider, lengthlbl, ctimelbl, playBtn, sBox, msglbl);
                            }
                        }
                    });
                }
            }
        };
        schTimer.scheduleAtFixedRate(schTask, 0, 1000);
    }

    private Bell getBellInfo() {
        String time = timeIn.getText();
        String path = pathIn2.getText();

        return new Bell(time, path);
    }

    public String timeToString() {
        return timeFormat.format(Calendar.getInstance().getTime());
    }

    private void schState() {
        if (schOn) {
            Utils.log("INFO", "System", "Stopping watchdog services...");
            loadPB(false);
            schOn = false;
            schBtn.setText("START");
            schBtn.setStyle("-fx-background-color: #17b80f;");
            stlbl.setText(this.ststopped);
            nf.showmsg("i", this.ststopped, msglbl);
            Utils.log("INFO", "System", "Scheduler --------> Stopped");
        } else {
            Utils.log("INFO", "System", "Starting watchdog services...");
            loadPB(true);
            schOn = true;
            schBtn.setText("STOP");
            schBtn.setStyle("-fx-background-color: #fc2803;");
            stlbl.setText(this.ststarted);
            nf.showmsg("i", this.ststarted, msglbl);
            Utils.log("INFO", "System", "Scheduler --------> Started");
        }
    }

    private void loadPB(boolean x) {
        var task = new Task<Void>() {
            @Override
            protected Void call() {
                int steps = 100;
                for (int i = 0; i < steps; i++) {
                    try {
                        Thread.sleep(10);
                        updateProgress(i, steps);
                        updateMessage(String.valueOf(i));
                    } catch (InterruptedException ex) {
                        nf.showalert("Error in PB Loader Deamon. Please re-start the system", "e");
                        Utils.system_crash();
                    }
                }
                return null;
            }
        };
        task.setOnSucceeded(evt2 -> {
            stpb.progressProperty().unbind();
            if (x) {
                stpb.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
            } else {
                stpb.setProgress(0);
            }
        });
        stpb.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    private void aboutPage() {
        String aboutTXT = "VERSION : " + Utils.VERSION + "\n"
                + """
                          Designed and Developed by SNS_DE_SARAM
                          Contact : snsdesaram@gmail.com
                          This System uses following Open Source Libraries :""";

        var about = new Alert(AlertType.INFORMATION);
        about.setTitle("About Periodic Bell System");
        about.setHeaderText("Periodic Bell System");
        about.setContentText(aboutTXT);

        var gson = new Hyperlink("https://github.com/google/gson");
        var jat = new Hyperlink("https://github.com/marcoc1712/jaudiotagger");
        var ikonli = new Hyperlink("https://github.com/kordamp/ikonli");

        gson.setOnMouseEntered((var e) -> {
            content.putString(gson.getText());
            clipboard.setContent(content);
            var ptp = new Tooltip("URL Copied to Clipboard");
            gson.setTooltip(ptp);
        });

        jat.setOnMouseEntered((var e) -> {
            content.putString(jat.getText());
            clipboard.setContent(content);
            var ptp = new Tooltip("URL Copied to Clipboard");
            jat.setTooltip(ptp);
        });

        ikonli.setOnMouseEntered((var e) -> {
            content.putString(ikonli.getText());
            clipboard.setContent(content);
            var ptp = new Tooltip("URL Copied to Clipboard");
            ikonli.setTooltip(ptp);
            System.out.println("done");
        });

        var acontent = new VBox(gson, jat, ikonli);
        about.getDialogPane().setExpandableContent(acontent);
        about.initOwner(getScene().getWindow());

        DialogPane dialogPane = about.getDialogPane();
        dialogPane.getStyleClass().add("about");
        about.setResizable(false);
        about.showAndWait();
    }

    public void shutFunc() {
        nf.showmsg("w", "System Driver Failure", msglbl);
    }

    public void shutdown() {

        var exitA = new Alert(AlertType.CONFIRMATION);
        exitA.setTitle("Exit Confirmation");
        exitA.setHeaderText(null);
        exitA.setContentText("Do you really need to exit ?");

        ButtonType yesBtn = new ButtonType("Yes", ButtonData.YES);
        ButtonType noBtn = new ButtonType("No", ButtonData.NO);

        exitA.getButtonTypes().setAll(yesBtn, noBtn);
        exitA.initOwner(getScene().getWindow());
        exitA.showAndWait();
        if (exitA.getResult().getButtonData() == ButtonData.YES) {
            exit();
        }
    }

    public void exit() {
        Utils.log("END", "SYSTEM", "Running Shutting Down Task...");
        Utils.log("END", "SYSTEM", "Running Finalize Deamon...");
        Utils.log("END", "SYSTEM", "System Dump :-");
        Utils.logout();
        Platform.exit();
        System.exit(0);
    }

    public void crash() {
        Utils.log("DUMP", "SYSTEM", "System Dump :-");
        Utils.system_crash();
    }

    public SystemB() {
        super();

        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("splash.fxml"));
            NodeUtils.setAnchors(pane, new Insets(216, 170, 252, 170));
            getChildren().addAll(pane);

            //Load splash screen with fade in effect
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            //Finish splash with fade out effect
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(4), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                init_services();
                fadeOut.play();
            });

            //After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
                Utils.log("INIT", "System", "Running FXML Interface Loader Deamon...");
                getStyleClass().add("pane");
                createInterface();
                Utils.log("INIT", "System", "Done loading FXML Interfaces");
                nf.showmsg("i", "System is ready", msglbl);
                starttimeServer();
            });
        } catch (IOException ex) {
            Utils.log("ERROR", "SYSTEM", "INIT ERROR");
            nf.showalert("Error on Initializing System", "e");
        }
    }

    public void init_services() {
        ms = MediaServer.getInstance();

        bellArray = FXCollections.observableArrayList();
        loadedBells = Utils.loadbells();
        bellArray.addAll(loadedBells);
        Utils.log("INIT", "SYSTEM", "System Services Started");
    }

    private static class InstanceHolder {

        private static final SystemB INSTANCE = new SystemB();
    }

    public static SystemB getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
