package GaussianFilterServer;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Client {

    final static int FIXEDPORT = 20001;
    final static String FIXEDHOSTNAME = "localhost";

    public static void main(String[] argv) {
        if (argv.length == 0)
            new Client().initialize(FIXEDHOSTNAME);
        else
            new Client().initialize(argv[0]);
    }

    /**
     * Hold one conversation across the net
     */
    protected void initialize(String hostName) {
        try {
            Socket serverSocket = new Socket(hostName, FIXEDPORT); // echo server

            decisionMaker(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void sendMessageToServer(String msg, PrintWriter os) {
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

    void closeConnection(Socket serverSocket) {
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

    String getParameterFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[1];
    }

    String getPathFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[2];
    }

    void decisionMaker(Socket serverSocket) throws IOException {
        PrintWriter os = new PrintWriter(serverSocket.getOutputStream(), true);
        //   String path = "C:\\Workspace\\Licenta\\Images\\elephant.jpg";
        while (!serverSocket.isClosed()) {
            String msgFromServer = getInput();

            //DEBUG
            System.out.println(msgToSend);

            sendMessageToServer(msgToSend, os);

            String command = getCommandFromMsg(msgFromServer);
            String parameter = getParameterFromMsg(msgFromServer);
            String path = getPathFromMsg(msgFromServer);

            switch (command) {
                case "SEND":
                    sendFileToServer(parameters[0]);
                    break;
                case "CLOSE":
                    closeConnection(serverSocket);
                    break;
                default:
                    System.err.println("ERROR! Method: int decisionMaker(String line)! Unknown command! Command:" + command);
                    break;
            }

        }
    }

    String getInput() {
        return (new Scanner(System.in)).nextLine().toUpperCase();
    }

}