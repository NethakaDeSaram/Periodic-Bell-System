//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023

package snsde.bellsystem.utils;

import java.util.Objects;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class Notification {

    public static final Interpolator EASE = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
    public final PseudoClass STATE_ACCENT = PseudoClass.getPseudoClass("accent");
    public final PseudoClass STATE_DANGER = PseudoClass.getPseudoClass("danger");
    public final PseudoClass STATE_WARN = PseudoClass.getPseudoClass("warning");
    public final PseudoClass STATE_D = PseudoClass.getPseudoClass("default");

    public static Timeline flash(Node node) {
        Objects.requireNonNull(node, "Node cannot be null!");

        var t = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.opacityProperty(), 1, EASE)
                ),
                new KeyFrame(Duration.millis(250),
                        new KeyValue(node.opacityProperty(), 0, EASE)
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(node.opacityProperty(), 1, EASE)
                ),
                new KeyFrame(Duration.millis(750),
                        new KeyValue(node.opacityProperty(), 0, EASE)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(node.opacityProperty(), 1, EASE)
                )
        );

        t.statusProperty().addListener((obs, old, val) -> {
            if (val == Animation.Status.STOPPED) {
                node.setOpacity(1);
            }
        });

        return t;
    }

    public void showmsg(String type, String msg, Label msglbl) {
        msglbl.setText(msg);
        switch (type) {
            case "e" -> {
                msglbl.setGraphic(new FontIcon(Feather.X_OCTAGON));
                msglbl.setGraphicTextGap(10);
                msglbl.pseudoClassStateChanged(STATE_WARN, false);
                msglbl.pseudoClassStateChanged(STATE_ACCENT, false);
                msglbl.pseudoClassStateChanged(STATE_DANGER, true);
            }
            case "w" -> {
                msglbl.setGraphic(new FontIcon(Feather.ALERT_TRIANGLE));
                msglbl.setGraphicTextGap(10);
                msglbl.pseudoClassStateChanged(STATE_ACCENT, false);
                msglbl.pseudoClassStateChanged(STATE_DANGER, false);
                msglbl.pseudoClassStateChanged(STATE_WARN, true);
            }
            case "i" -> {
                msglbl.setGraphic(new FontIcon(Feather.INFO));
                msglbl.setGraphicTextGap(10);
                msglbl.pseudoClassStateChanged(STATE_WARN, false);
                msglbl.pseudoClassStateChanged(STATE_DANGER, false);
                msglbl.pseudoClassStateChanged(STATE_ACCENT, true);
            }
            default -> {
                msglbl.setGraphic(new FontIcon(Feather.INFO));
                msglbl.setGraphicTextGap(10);
                msglbl.pseudoClassStateChanged(STATE_WARN, false);
                msglbl.pseudoClassStateChanged(STATE_DANGER, false);
                msglbl.pseudoClassStateChanged(STATE_ACCENT, true);
            }
        }
        flash(msglbl).playFromStart();
    }

    public void showalert(String msg, String type) {
        switch (type) {
            case "e" -> {
                var al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error in Periodic Bell System");
                al.setHeaderText(null);
                al.setContentText(msg);
                al.setResizable(false);
                al.showAndWait();
            }
            case "w" -> {
                var al = new Alert(Alert.AlertType.WARNING);
                al.setTitle("Warning");
                al.setHeaderText(null);
                al.setContentText(msg);
                al.setResizable(false);
                al.showAndWait();
            }
            case "i" -> {
                var al = new Alert(Alert.AlertType.INFORMATION);
                al.setTitle("Information");
                al.setHeaderText(null);
                al.setContentText(msg);
                al.setResizable(false);
                al.showAndWait();
            }
            default -> {
                var al = new Alert(Alert.AlertType.ERROR);
                al.setTitle("Error in Periodic Bell System");
                al.setHeaderText(null);
                al.setContentText(msg);
                al.setResizable(false);
                al.showAndWait();
            }
        }
    }

}
