

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogRecording {
    public String [][] list;

    // Initial the String as list for store clients
    public LogRecording(int listNum, int clientNum_Mux) {
        list = new String[listNum][clientNum_Mux];
    }

    // print the recording of the operation
    public void MainLog(String request) throws UnknownHostException {

        // Date
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd|hh:mm:ss");
        // IP
        String ip = InetAddress.getLocalHost().getHostAddress();
        // Records
        String record = (date.format(new Date()) + "|" + ip + "|" + request + ".\n");
        // File path
        String File_Path = "log.txt";

        // Write recordings to lio.txt file
        try {
            File file = new File(File_Path);

            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(record);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
