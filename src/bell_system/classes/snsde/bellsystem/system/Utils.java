//Designed and Developed by S.N.S. De Saram
//For Bell System v3.0.2 - 2023

package snsde.bellsystem.system;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import snsde.bellsystem.utils.Notification;

public class Utils {

    public static final String VERSION = "3.0.2";

    public static String lastOpenPath;

    public final PseudoClass STATE_ACCENT = PseudoClass.getPseudoClass("accent");
    public final PseudoClass STATE_DANGER = PseudoClass.getPseudoClass("danger");
    public final PseudoClass STATE_WARN = PseudoClass.getPseudoClass("warning");

    public static Notification nf = new Notification();

    public final static String dataFile = "data/data.json";

    public static final String LOG_DIRECTORY = "./log/";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void br_media2(Label pathIn, Label msglbl, TextField pathIn2) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Media Server - Open MP3 File");

        FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("MP3 Files", "*.mp3");
        fileChooser.getExtensionFilters().add(mp3Filter);

        if (lastOpenPath != null && !lastOpenPath.equals("")) {
            File initialDirectory = new File(lastOpenPath);
            fileChooser.setInitialDirectory(initialDirectory);
        } else {
            File initialDirectory = new File("./");
            fileChooser.setInitialDirectory(initialDirectory);
        }

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            if (file.exists() && file.canRead()) {
                lastOpenPath = file.getParent();
                System.out.println(file.getAbsoluteFile().toString());
                String mediaPath = file.getAbsoluteFile().toString();
                pathIn.setText(mediaPath);
                pathIn2.setText(mediaPath);
            } else {
                nf.showmsg("w", "Media File does not exists", msglbl);
            }
        } else {
            if (pathIn.getText().isBlank()) {
                nf.showmsg("w", "No media File Selected", msglbl);
            }
        }
    }

    public static List<Bell> loadbells() {

        File dfile = new File(dataFile);
        if (!dfile.canRead() && !dfile.exists()) {
            nf.showalert("data.json missing or corrupted. Please re-install the System", "e");
            log("ERROR", "System", "data.json not found. exit 1");
            system_crash();
        }

        Gson gson = new Gson();
        System.out.println(dataFile);
        try (FileReader reader = new FileReader(dataFile)) {
            log("INFO", "System", "Loaded data.json succesfully UTF-8");
            return Arrays.asList(gson.fromJson(reader, Bell[].class));
        } catch (FileNotFoundException ex) {
            nf.showalert("data.json missing or corrupted. Please re-install the System", "e");
            log("ERROR", "System", "data.json not found. exit 1");
            system_crash();
        } catch (IOException ex) {
            nf.showalert("data.json missing or corrupted. Please re-install the System", "e");
            log("ERROR", "System", "data.json not found. exit 1");
            system_crash();
        }
        return null;
    }

    public static void writeBells(List<Bell> bells) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(bells, writer);
        } catch (IOException e) {
            nf.showalert("Cannot write to data.json. Please restart the system", "e");
            log("ERROR", "System", "Cannot write to data.jaon. exit 1");
            system_crash();
        }
    }

    public static boolean isTimeV(String input) {
        try {
            // Define the expected format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Parse the input into a LocalTime object
            LocalTime parsedTime = LocalTime.parse(input, formatter);

            // Format the parsed time back into the expected format
            String formattedTime = parsedTime.format(formatter);

            // Compare the input with the formatted time
            return input.equals(formattedTime);
        } catch (Exception e) {
            return false;
        }
    }

    public static void log(String type, String comp, String msg) {
        String logFileName = LOG_DIRECTORY + "log_" + sdf.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStamp = sdf.format(new Date());

            // Append the actual log data
            writer.newLine();
            writer.write(timeStamp + " : " + " [ " + type + " ] " + " [ " + comp + " ] " + msg);
        } catch (IOException e) {
            loggerError(false);
        }
    }

    public static void loginit() {
        String logFileName = LOG_DIRECTORY + "log_" + sdf.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeStamp = sdf1.format(new Date());

            // Append general data at the start of the log
            writer.write("                                      ");
            writer.newLine();
            writer.write("================== Bell System Started: " + timeStamp + " ==================");
            writer.newLine();
            writer.write("     ");
            writer.newLine();

            writer.write("System Version : " + VERSION);
            writer.newLine();
            writer.write("Environment: " + System.getProperty("os.name"));
            writer.newLine();
            writer.write("Java Version: " + System.getProperty("java.version"));
            writer.newLine();
            writer.write("     ");
        } catch (IOException e) {
            loggerError(true);
        }
    }

    public static void logout() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = sdf2.format(new Date());

        String logFileName = LOG_DIRECTORY + "log_" + sdf.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
            systeminfo(writer);
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
            writer.write("================== Bell System Shutdown: " + timeStamp + " ==================");
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
        } catch (IOException ex) {
            loggerError(false);
        }
    }

    public static void system_crash() {
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = sdf2.format(new Date());

        String logFileName = LOG_DIRECTORY + "log_" + sdf.format(new Date()) + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFileName, true))) {
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
            systeminfo(writer);
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
            writer.write("================== Bell System Crashed: " + timeStamp + " ==================");
            writer.newLine();
            writer.write("                                ");
            writer.newLine();
        } catch (IOException ex) {
            loggerError(false);
        }
        Platform.exit();
        System.exit(1);
    }

    public static void loggerError(boolean init) {
        if (init) {
            var about = new Alert(Alert.AlertType.ERROR);
            about.setTitle("Periodic Bell System");
            about.setHeaderText("Error Initializing Periodic Bell System");
            about.setContentText("Error Initializing Logger Service 0x24e5");
            about.setResizable(false);
            about.showAndWait();
            SystemB.getInstance().crash();
        } else {
            var about = new Alert(Alert.AlertType.ERROR);
            about.setTitle("Periodic Bell System");
            about.setHeaderText("Error in Core System");
            about.setContentText("Error occured Logger Service");
            about.setResizable(false);
            about.showAndWait();
            SystemB.getInstance().crash();
        }
    }

    public static void systeminfo(BufferedWriter writer) {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

            writer.write("[ DUMP ] " + "CPU Load : " + String.valueOf(osBean.getSystemLoadAverage()));
            writer.newLine();
            for (Long threadID : threadMXBean.getAllThreadIds()) {
                ThreadInfo info = threadMXBean.getThreadInfo(threadID);
                writer.write("[ DUMP ] " + "Thread name: " + info.getThreadName());
                writer.newLine();
                writer.write("[ DUMP ] " + "Thread State: " + info.getThreadState());
                writer.newLine();
                writer.write("[ DUMP ] " + String.format("CPU time: %s ns",
                        threadMXBean.getThreadCpuTime(threadID)));
                writer.newLine();
            }

            writer.write("                                ");
            writer.newLine();

            writer.write("[ DUMP ] " + "Used Memory: " + String.valueOf(memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024)) + " MB");
            writer.newLine();
            writer.write("[ DUMP ] " + "Max Memory: " + String.valueOf(memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024)) + " MB");

            writer.newLine();
            writer.write("                                ");
            writer.newLine();

            File file = new File("/");
            writer.write("[ DUMP ] " + "Total Space: " + String.valueOf(file.getTotalSpace() / (1024 * 1024 * 1024)) + " GB");
            writer.newLine();
            writer.write("[ DUMP ] " + "Free Space: " + String.valueOf(file.getFreeSpace() / (1024 * 1024 * 1024)) + " GB");
            writer.newLine();
            writer.write("[ DUMP ] " + "Usable Space: " + String.valueOf(file.getUsableSpace() / (1024 * 1024 * 1024)) + " GB");

        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
