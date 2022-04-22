
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;

public class Client {

    private Socket socket = null;
    private PrintWriter socketOutput = null;
    private BufferedReader socketInput = null;

    public String request;

    public void PlaySignIn(String[] args) {

        try {
            socket = new Socket("localhost", 9777);
            socketOutput = new PrintWriter(socket.getOutputStream(), true);
            socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host.\n");
            System.exit(1);

        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to host.\n");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));
        String fromServer;
        // String fromUser;

        socketOutput.println(request);

        socketOutput.println(args[0]);

        try {
            label:
            while ((fromServer = socketInput.readLine()) != null) {
                switch (fromServer) {
                    case "Finished!":
                        break label;

                    case "invalid command":
                        System.out.println("Invalid command!");
                        break label;

                    case "invalid":
                        System.out.println("Invalid input!");
                        break label;

                    case "Totals begin:":
                        if (args.length == 1) {
                            socketOutput.println("send list number");
                            fromServer = socketInput.readLine();
                            String[] totalNumber = fromServer.split("\\s+");
                            String listNum = totalNumber[0];
                            String clientNum = totalNumber[1];
                            System.out.println("There are " + listNum + " list(s), each with a maximum size of "
                                    + clientNum + ".");
                            for (int i = 1; i <= Integer.parseInt(listNum); i++) {
                                System.out.println("List " + i + " has " + totalNumber[i + 1] + " member(s).");
                            }
                            socketOutput.println("Success.");
                        } else {
                            socketOutput.println("wrong command");
                            System.out.println("The command is invalid!");
                        }
                        break;

                    case "Join begin:":
                        if (isNumber(args[1]) && args.length == 3) {
                            socketOutput.println(args[1]);
                            fromServer = socketInput.readLine();
                            if (fromServer.equals("valid number")) {
                                socketOutput.println(args[2]);
                                System.out.println("Success.");
                            } else if (fromServer.equals("invalid number")) {
                                System.out.println("Failed.");
                                socketOutput.println("wrong command");
                            } else {
                                System.out.println("Failed.");
                                socketOutput.println("wrong command");
                            }
                        } else {
                            socketOutput.println("wrong command");
                            System.out.println("Invalid input of list numbers!");
                        }
                        break;

                    case "List begin:":
                        if (isNumber(args[1]) && args.length == 2) {
                            socketOutput.println(args[1]);
                            fromServer = socketInput.readLine();
                            if (fromServer.equals("valid number")) {
                                socketOutput.println("client find");
                                fromServer = socketInput.readLine();
                                String[] clientName = fromServer.split("\\t+");
                                for (String s : clientName) {
                                    System.out.println(s);
                                }
                                socketOutput.println("Success.");
                            } else {
                                socketOutput.println("wrong command");
                                System.out.println("Invalid input of list numbers!");
                            }
                        } else {
                            socketOutput.println("wrong command");
                            System.out.println("Invalid input of list numbers!");
                        }
                        break;
                }
            }
            socketOutput.close();
            socketInput.close();
            stdIn.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("I/O exception during execution\n");
            System.exit(1);
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

    public static void main(String[] args) {
        Client client = new Client();
        client.request = args[0];
        for (int i = 1; i < args.length; i++) {
            client.request = client.request + " " + args[i];
        }
        client.PlaySignIn(args);
    }
}
