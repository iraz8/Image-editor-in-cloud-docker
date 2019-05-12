import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

class ClientSimulation {

    private final static int FIXEDPORT = 20000;
    private final static String FIXEDHOSTNAME = "local_network_main_server";

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


    private void decisionMaker(Socket serverSocket) throws IOException {
        PrintWriter os = new PrintWriter(serverSocket.getOutputStream(), true);

        final String currentPath = new File("").getAbsolutePath();

        // String msgToSend = "GAUSSIAN-FILTER " + "/home/Images/test.jpg 45 45 60";
        // String msgToSend = "IMAGE-SHAPE-CONVERSION " + "/home/Images/test.jpg 45 45 60";
        String msgToSend = "ZOOM " + "/home/Images/test.jpg 2";

        //DEBUG
        System.out.println(msgToSend);

        sendMessageToServer(msgToSend, os);

        //DEBUG
        System.out.println("2 : " + msgToSend);
        closeConnection(serverSocket);
    }
}