//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023


package snsde.bellsystem.system;

public class Bell {

    private String time;
    private String path;
    
    public Bell(String time, String path){
        this.time = time;
        this.path = path;
    }

    public String getBTime() {
        return time;
    }

    public void setBTime(String time) {
        this.time = time;
    }

    public String getBPath() {
        return path;
    }

    public void setBPath(String path) {
        this.path = path;
    }
    
    @Override
    public String toString() {
        return time + " - " + path;
    }
}
