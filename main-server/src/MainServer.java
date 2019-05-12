import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class MainServer {
    private static final int FIXEDPORT = 20000;
    private static final int NUM_THREADS = 5;

    /**
     * Constructor
     */
    private MainServer(int port, int numThreads) {
        ServerSocket mainServerSocket;

        try {
            mainServerSocket = new ServerSocket(port);

        } catch (IOException e) {
            /* Crash the server if IO fails. Something bad has happened */
            throw new RuntimeException("Could not create ServerSocket ", e);
        }

        // Create a series of threads and start them.
        for (int i = 0; i < numThreads; i++) {
            new Handler(mainServerSocket, i).start();
        }
    }


    /**
     * Main method, to start the servers.
     */
    public static void main(String[] av) {
        new MainServer(FIXEDPORT, NUM_THREADS);

    }
}


/**
 * A Thread subclass to handle one client conversation.
 */
class Handler extends Thread {
    private final static int GaussianFilterServerPort = 20001;
    private final static int ImageShapeConversionServerPort = 20002;
    private final static int ZoomServerPort = 20003;
    /**
     * Parameters
     */
    private Socket specializedServerSocket;
    private ServerSocket mainServerSocket;
    private int threadNumber;

    /**
     * Construct a Handler.
     */
    Handler(ServerSocket s, int i) {
        mainServerSocket = s;
        threadNumber = i;
        setName("Thread " + threadNumber);
    }

    public void run() {
        /* Wait for a connection. Synchronized on the ServerSocket
         * while calling its accept() method.
         */
        try {
            System.out.println(getName() + " waiting");

            Socket inSocket;
            // Wait here for the next connection.
            synchronized (mainServerSocket) {
                inSocket = mainServerSocket.accept();
            }

            decisionMaker(inSocket);

            if (!inSocket.isClosed()) {
                inSocket.close();
            }

            System.out.println(getName() + " Closed ");

        } catch (IOException ex) {
            System.out.println(getName() + ": IO Error on socket " + ex);
        }

    }

    private void closeConnection(Socket inSocket) {
        String clientInetAddress = inSocket.getInetAddress().toString();
        try {
            inSocket.close();
        } catch (IOException e) {
            System.err.println("ERROR! method void closeConnection(Socket inSocket)");
            e.printStackTrace();
        }

        if (inSocket.isClosed())
            System.out.println("Connection closed! The socket with IP " + clientInetAddress + " is closed!");
        else
            System.err.println("ERROR! method void closeConnection(Socket inSocket). Connection with " + clientInetAddress + "is still open!");
    }

    /**
     * Hold one conversation across the net
     */
    private void initialize(String hostName, int port) {
        try {
            specializedServerSocket = new Socket(hostName, port); // echo server
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private String getCommandFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[0].toUpperCase();
    }

    private String getParameterFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[1];
    }

    private String getPathFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[2];
    }

    private String getMsgFromClient(BufferedReader is) throws IOException {
        return is.readLine();
    }

    private void sendMessage(String msg, PrintWriter os) {
        os.print(msg + "\r\n");
        os.flush();
    }

    private void decisionMaker(Socket inSocket) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
        System.out.println(getName() + " starting, IP=" + inSocket.getInetAddress());

        String msgToSend = getMsgFromClient(is);
        String command = getCommandFromMsg(msgToSend);

        //DEBUG
        System.out.println(msgToSend);

        PrintWriter os;
        switch (command) {
            case "GAUSSIAN-FILTER": {
                initialize("local_network_gaussian_filter_server", GaussianFilterServerPort);
            }
            case "IMAGE-SHAPE-CONVERSION": {
                initialize("local_network_image_shape_conversion_server", ImageShapeConversionServerPort);
            }
            case "ZOOM": {
                initialize("local_network_zoom_server", ZoomServerPort);
            }
        }

        os = new PrintWriter(specializedServerSocket.getOutputStream(), true);

        sendMessage(msgToSend, os);
        closeConnection(inSocket);

    }
}
