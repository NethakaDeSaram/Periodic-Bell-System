//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023

module bell_system {
    
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.feather;
    requires org.kordamp.ikonli.javafx;
    requires jaudiotagger;
    requires com.google.gson;
    requires java.management;

    opens snsde.bellsystem to 
            javafx.fxml, 
            javafx.media, 
            java.logging,
            org.kordamp.ikonli.core, 
            org.kordamp.ikonli.feather, 
            org.kordamp.ikonli.javafx, 
            jaudiotagger,
            com.google.gson,
            java.management;
    opens snsde.bellsystem.system to com.google.gson;
    exports snsde.bellsystem; 
    exports snsde.bellsystem.system;
    exports snsde.bellsystem.utils;
}
