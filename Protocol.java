import java.util.regex.Pattern;

public class Protocol {

    private static final int INITIAL = 0;
    private static final int TOTALS = 1;
    private static final int DISPLAY = 2;
    private static final int ADDUSER = 3;
    private static final int INVALID = 4 ;

    private int state = INITIAL;
    private int list_Num = 0;

    public String processInput (String theInput) {
        String theOutput = null;

        if (state == INITIAL) {
            switch (theInput) {
                case "totals":
                    state = TOTALS;
                    break;
                case "join":
                    state = ADDUSER;
                    break;
                case "list":
                    state = DISPLAY;
                    break;
                default:
                    state = INVALID;
                    break;
            }
        }

        if (state == TOTALS) {
            switch (theInput) {
                case "totals":
                    theOutput = "Totals begin:";
                    break;
                case "send list number":
                    theOutput = Integer.toString(Server.listNum) + ' ' + Server.clientNum_Max;
                    for (int i = 0; i < Server.listNum; i++) {
                        int clientNum = 0;
                        for (int j = 0; j < Server.clientNum_Max; j++) {
                            if (Server.logRecording.list[i][j] != null) {
                                clientNum++;
                            }
                        }
                        theOutput = String.format("%s %d", theOutput, clientNum);
                    }
                    break;
                case "wrong command":
                case "Success.":
                    theOutput = "Finished!";
                    state = INITIAL;
                    break;
            }
        }

        if (state == ADDUSER) {
            if (theInput.equals("join")) {
                theOutput = "Join begin:";
            } else if (isNumber(theInput)) {
                if (validNumber(theInput)) {
                    list_Num = Integer.parseInt(theInput);
                    theOutput = "valid number";
                } else {
                    theOutput = "invalid number";
                }
            } else if (theInput.equals("wrong command")) {
                theOutput = "Finished!";
                state = INITIAL;
            } else {
                int clientNum = 0;
                for (int i = 0; i < Server.clientNum_Max; i++) {
                    if (Server.logRecording.list[list_Num - 1][i] != null) {
                        clientNum++;
                    }
                }
                Server.logRecording.list[list_Num - 1][clientNum] = theInput;
                theOutput = "Finished!";
                state = INITIAL;
            }
        }

        if (state == DISPLAY) {
            if (theInput.equals("list")) {
                theOutput = "List begin:";
            } else if(isNumber(theInput)) {
                if (validListNumber(theInput)) {
                    list_Num = Integer.parseInt(theInput);
                    theOutput = "valid number";
                } else {
                    theOutput = "invalid number";
                }
            }

            if (theInput.equals("client find")) {
                theOutput = String.valueOf(Server.logRecording.list[list_Num - 1][0]);
                int clientNum = 0;
                for (int i = 0; i < Server.clientNum_Max; i++) {
                    if (Server.logRecording.list[list_Num - 1][i] != null) {
                        clientNum++;
                    }
                }
                for (int i = 1; i < clientNum; i++) {
                    theOutput = String.format("%s\t%s", theOutput, Server.logRecording.list[list_Num - 1][i]);
                }
            } else {
                theOutput = "Finished!";
                state = INITIAL;
            }
        }

        if (state == INVALID) {
            theOutput = "invalid command";
            state = INITIAL;
        }

        return theOutput;
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

    private boolean validNumber(String number) {
        if (Integer.parseInt(number) <= Server.listNum && Integer.parseInt(number) > 0) {
            int clientNum = 0;
            for (int i = 0; i < Server.clientNum_Max; i++) {
                if (Server.logRecording.list[Integer.parseInt(number) - 1][i] != null) {
                    clientNum++;
                }
            }
            return clientNum < Server.clientNum_Max;
        } else {
            return false;
        }
    }

    private boolean validListNumber(String number) {
        return Integer.parseInt(number) <= Server.listNum && Integer.parseInt(number) > 0;
    }
}
