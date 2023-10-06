/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//Designed and Developed by S.N.S. De Saram
package snsde.bellsystem.utils;

import javafx.scene.layout.AnchorPane;

public class splashScreen extends AnchorPane {
    
    private splashScreen(){
        
        
        
    }

    private static class InstanceHolder {

        private static final splashScreen INSTANCE = new splashScreen();
    }

    public static splashScreen getInstance() {
        return InstanceHolder.INSTANCE;
    }

}
