//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023
package snsde.bellsystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import snsde.bellsystem.utils.Res;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import snsde.bellsystem.system.SystemB;
import snsde.bellsystem.system.Utils;

public class Launcher extends Application {

    //public Image icon = new Image("res/icon.png");
    @Override
    public void start(Stage stage) throws Exception {

        var path = Paths.get(Utils.LOG_DIRECTORY);

        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Utils.loggerError(true);
                System.exit(1);
            }
        }
        Utils.loginit();

        Utils.log("INIT", "System", "Running Core System Services Initialization Task");

        var root = SystemB.getInstance();

        var scene = new Scene(root, 940, 690, false);
        scene.setFill(Color.LIGHTGRAY);

        scene.getStylesheets().addAll(Res.getResource("styles/index.css").toString());
        setUserAgentStylesheet(Res.getResource("styles/cupertino-light.css").toString());

        stage.setScene(scene);
        stage.setTitle("Bell System");
        File iconFile = new File("icon.png");
        Image icon = new Image(iconFile.getAbsolutePath());
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent arg0) -> {
            arg0.consume();
            root.shutdown();
        });

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
        });

        Utils.log("INIT", "System", "Done Core System Services Initialization");
        Utils.log("INIT", "System", "System is ready and stable...");
    }

    public static void main(String[] args) {
        launch(args);

    }

}
