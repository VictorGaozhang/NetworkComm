

import java.net.*;
import java.io.*;
import java.util.Date;

public class Handler extends Thread {

    private Socket socket = null;

    public Handler(Socket socket) {
        super("Handler");
        this.socket = socket;
    }

    public void run () {
        try {
            // Input and output streams to/from the client
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            // Operation request
            String request = in.readLine();
            Server.logRecording.MainLog(request);

            // Logging
            InetAddress inet = socket.getInetAddress();
            Date date = new Date();
            System.out.println("\nDate: " + date.toString());
            System.out.println("Connection made from " + inet.getHostAddress());

            // Initialise a protocol object for this client
            String inputLine, outputLine;
            Protocol protocol = new Protocol();

            // Sequential protocol
            while ((inputLine = in.readLine()) != null) {
                outputLine = protocol.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Finished!")) {
                    break;
                }
            }
            // Free up resources for this connection
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
