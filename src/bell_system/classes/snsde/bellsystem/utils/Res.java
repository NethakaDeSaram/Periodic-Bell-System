//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023

package snsde.bellsystem.utils;

import java.net.URI;
import java.net.URL;
import java.util.Objects;
import snsde.bellsystem.Launcher;

public class Res {

    public static String MODULE_DIR = null;

    public static String resolve(String resource) {
        System.out.println(MODULE_DIR);
        URL url =  Launcher.class.getResource(resource);
        return null;
    }

    public static URI getResource(String resource) {
        URL url = Objects.requireNonNull(Launcher.class.getResource(resource), "Resource not found: " + resource);
        return URI.create(url.toExternalForm());
    }

}
