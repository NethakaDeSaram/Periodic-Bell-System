/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//Designed and Developed by S.N.S. De Saram
package snsde.bellsystem.system;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import snsde.bellsystem.utils.NodeUtils;
import snsde.bellsystem.utils.Res;

public class Splash extends AnchorPane {

    public Splash() {
        super();

        getStyleClass().add("splash");
        
        ImageView img = new ImageView(Res.getResource("assets/icon.png").toString());
        img.setFitHeight(111);
        img.setFitWidth(107);
        img.preserveRatioProperty().set(true);
        img.smoothProperty().set(true);
        NodeUtils.setAnchors(img, new Insets(19, 474, 96, 19));

        Label plbl = new Label("Periodic");
        plbl.prefHeight(34);
        plbl.prefWidth(149);
        plbl.getStyleClass().add("s_lbl1");
        plbl.setTextFill(Color.valueOf("#5e5e5e"));
        NodeUtils.setAnchors(plbl, new Insets(32, 321, 156, 136));

        Label blbl = new Label("Bell System");
        blbl.prefHeight(57);
        blbl.prefWidth(278);
        blbl.getStyleClass().add("s_lbl2");
        blbl.setTextFill(Color.valueOf("#000000"));
        NodeUtils.setAnchors(blbl, new Insets(54, 196, 98, 126));

        Label dlbl = new Label("Designed And Developed by SNS_DE_SARAM");
        dlbl.prefHeight(20);
        dlbl.prefWidth(308);
        dlbl.getStyleClass().add("s_lbl3");
        dlbl.setTextFill(Color.valueOf("#000000"));
        NodeUtils.setAnchors(dlbl, new Insets(168, 265, 34, 27));

        Label clbl = new Label("Contact :- snsdesaram@gmail.com");
        clbl.prefHeight(20);
        clbl.prefWidth(308);
        clbl.getStyleClass().add("s_lbl4");
        clbl.setTextFill(Color.valueOf("#000000"));
        NodeUtils.setAnchors(clbl, new Insets(188, 265, 14, 27));

        Label vlbl = new Label("v3.0.2");
        vlbl.prefHeight(26);
        vlbl.prefWidth(51);
        vlbl.getStyleClass().add("s_lbl5");
        vlbl.setTextFill(Color.valueOf("#000000"));
        NodeUtils.setAnchors(vlbl, new Insets(175, 14, 21, 535));

        getChildren().addAll(img, plbl, blbl, dlbl, clbl, vlbl);

    }

    private static class InstanceHolder {

        private static final Splash INSTANCE = new Splash();
    }

    public static Splash getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
