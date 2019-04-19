import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

class ClientSimulation {

    private final static int FIXEDPORT = 20000;
    private final static String FIXEDHOSTNAME = "localhost";

    public static void main(String[] argv) {
        if (argv.length == 0)
            new ClientSimulation().initialize(FIXEDHOSTNAME);
        else
            new ClientSimulation().initialize(argv[0]);
    }

    /**
     * Hold one conversation across the net
     */
    private void initialize(String hostName) {
        try {
            Socket serverSocket = new Socket(hostName, FIXEDPORT); // echo server

            decisionMaker(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer(String msg, PrintWriter os) {
        os.print(msg + "\r\n");
        os.flush();
    }

    String getCommandFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[0];
    }

    String[] getParametersFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        String[] parameters = Arrays.copyOfRange(msgSplited, 1, msgSplited.length);
        return parameters;
    }

    private void closeConnection(Socket serverSocket) {
        String serverInetAddress = serverSocket.getInetAddress().toString();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("ERROR! method void closeConnection(Socket serverSocket)");
            e.printStackTrace();
        }

        if (serverSocket.isClosed())
            System.out.println("Connection closed! The socket with IP " + serverInetAddress + " is closed!");
        else
            System.err.println("ERROR! method void closeConnection(Socket clientSocket). Connection with " + serverInetAddress + "is still open!");
    }

    Socket openNewSocketToServer() {
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(FIXEDHOSTNAME, FIXEDPORT + 1); // echo server
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }


    private void decisionMaker(Socket serverSocket) throws IOException {
        PrintWriter os = new PrintWriter(serverSocket.getOutputStream(), true);
        //   String path = "C:\\Workspace\\Licenta\\Images\\elephant.jpg";


        // String msgToSend = getInput();
        final String currentPath = new File("").getAbsolutePath();
        String msgToSend = "GAUSSIAN-FILTER " + currentPath + "/Images/test.jpg 50";

        //DEBUG
        System.out.println(msgToSend);

        sendMessageToServer(msgToSend, os);


        closeConnection(serverSocket);
    }

    String getInput() {
        return (new Scanner(System.in)).nextLine().toUpperCase();
    }

}