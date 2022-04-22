import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class Server {
    public static int listNum;
    public static int clientNum_Max;
    public static LogRecording logRecording;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        ExecutorService service;
        boolean listening = true;

        if ( !Server.validInitial(args)) {
            System.out.println("Invalid input! Please enter two Integer numbers!");
            return;
        }

        listNum = Integer.parseInt(args[0]);
        clientNum_Max = Integer.parseInt(args[1]);
        logRecording = new LogRecording(listNum, clientNum_Max);

        try {
            serverSocket = new ServerSocket(9777);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 9777");
            System.exit(-1);
        }

        service = Executors.newFixedThreadPool(25);

        while (listening) {
            Socket socket = serverSocket.accept();
            service.submit(new Handler(socket));
        }
    }

    public static boolean isNumber (String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        for (int i = 0; i < str.length(); i++) {
            if (!pattern.matcher(str).matches()) {
                return false;
            }
        }
        return true;
    }

    public static boolean validInitial (String[] initialInput) {

        if (initialInput.length == 2) {
            return isNumber(initialInput[0]) && isNumber(initialInput[1]) &&
                    Integer.parseInt(initialInput[0]) > 0 && Integer.parseInt(initialInput[1]) > 0;
        } else {
            return false;
        }
    }
}
