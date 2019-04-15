import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class MainServer {

    public static final int FIXEDPORT = 20000;
    public static final int NUM_THREADS = 10;
    final static String FIXEDHOSTNAME = "localhost";

    /**
     * Constructor
     */
    public MainServer(int port, int numThreads) {
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            /* Crash the server if IO fails. Something bad has happened */
            throw new RuntimeException("Could not create ServerSocket ", e);
        }

        // Create a series of threads and start them.
        for (int i = 0; i < numThreads; i++) {
            new Handler(serverSocket, i).start();
        }
    }


    /**
     * Main method, to start the servers.
     */
    public static void main(String[] av) {
        //    new FileTransferServer(FIXEDPORT + 1, NUM_THREADS);
        new MainServer(FIXEDPORT, NUM_THREADS);

    }
}


/**
 * A Thread subclass to handle one client conversation.
 */
class Handler extends Thread {
    public static final int FIXEDPORT = 20000;
    public static final int NUM_THREADS = 10;
    final static String FIXEDHOSTNAME = "localhost";
    final static int GaussianFilterServerPort = 20001;


    ServerSocket serverSocket;
    int threadNumber;

    /**
     * Parameters
     */
    String serverFilesPath = "Server Files";

    /**
     * Construct a Handler.
     */
    Handler(ServerSocket s, int i) {
        serverSocket = s;
        threadNumber = i;
        setName("Thread " + threadNumber);
    }

    public void run() {
        /* Wait for a connection. Synchronized on the ServerSocket
         * while calling its accept() method.
         */
        try {
            System.out.println(getName() + " waiting");

            Socket clientSocket;
            // Wait here for the next connection.
            synchronized (serverSocket) {
                clientSocket = serverSocket.accept();
            }

            decisionMaker(clientSocket);

            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }

            System.out.println(getName() + " Closed ");

        } catch (IOException ex) {
            System.out.println(getName() + ": IO Error on socket " + ex);
        }

    }

    void closeConnection(Socket clientSocket) {
        String clientInetAddress = clientSocket.getInetAddress().toString();
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("ERROR! method void closeConnection(Socket clientSocket)");
            e.printStackTrace();
        }

        if (clientSocket.isClosed())
            System.out.println("Connection closed! The socket with IP " + clientInetAddress + " is closed!");
        else
            System.err.println("ERROR! method void closeConnection(Socket clientSocket). Connection with " + clientInetAddress + "is still open!");
    }


    String getCommandFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[0].toUpperCase();
    }

    String getParameterFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[1];
    }

    String getPathFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[2];
    }

    String getMsgFromClient(BufferedReader is) throws IOException {
        return is.readLine();
    }

    void sendMessage(String msg, PrintWriter os) {
        os.print(msg + "\r\n");
        os.flush();
    }

    void decisionMaker(Socket clientSocket) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //  PrintWriter os = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println(getName() + " starting, IP=" + clientSocket.getInetAddress());

        String msgToSend = getMsgFromClient(is);
        //  String msgToSend = "GAUSSIAN-FILTER 45 ..\\common-files\\test.jpg";
        String command = getCommandFromMsg(msgToSend);
        String parameter = getParameterFromMsg(msgToSend);
        String path = getPathFromMsg(msgToSend);

        //DEBUG
        System.out.println(msgToSend);

/*
        Socket specializedServer;

        switch(command) {
            case "GAUSSIAN-FILTER":
                specializedServer = "localhost" + GaussianFilterServerPort;

        switch (command) {
            case "GAUSSIAN-FILTER":

                break;
            case "CLOSE":
                closeConnection(clientSocket);
                break;
            default:
                System.err.println("ERROR! Method: int decisionMaker(String line)! Unknown command! Command:" + command);
                break;
        }*/
        closeConnection(clientSocket);

    }
}
