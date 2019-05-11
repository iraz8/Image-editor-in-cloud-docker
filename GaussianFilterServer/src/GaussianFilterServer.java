import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;


class GaussianFilterServer {

    private static final int FIXEDPORT = 20001;
    private static final int NUM_THREADS = 3;
    final static String FIXEDHOSTNAME = "localhost";

    /**
     * Constructor
     */
    private GaussianFilterServer(int port, int numThreads) {
        ServerSocket specializedServerSocket;

        try {
            specializedServerSocket = new ServerSocket(port);

        } catch (IOException e) {
            /* Crash the server if IO fails. Something bad has happened */
            throw new RuntimeException("Could not create ServerSocket ", e);
        }

        // Create a series of threads and start them.
        for (int i = 0; i < numThreads; i++) {
            new Handler(specializedServerSocket, i).start();
        }
    }


    /**
     * Main method, to start the servers.
     */
    public static void main(String[] av) {
        new GaussianFilterServer(FIXEDPORT, NUM_THREADS);

    }
}

/**
 * A Thread subclass to handle one client conversation.
 */
class Handler extends Thread {
    public static final int FIXEDPORT = 20000;
    public static final int NUM_THREADS = 5;
    final static String FIXEDHOSTNAME = "local_network";
    final static int GaussianFilterServerPort = 20001;
    static final Path currentPath = FileSystems.getDefault().getPath(".");

    private ServerSocket specializedServerSocket;
    private int threadNumber;

    /**
     * Parameters
     */
    String serverFilesPath = "Server Files";

    /**
     * Construct a Handler.
     */
    Handler(ServerSocket s, int i) {
        specializedServerSocket = s;
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
            synchronized (specializedServerSocket) {
                inSocket = specializedServerSocket.accept();
            }
            Process d = Runtime.getRuntime().exec("touch /home/Images/connected");
            decisionMaker(inSocket);
            Process e = Runtime.getRuntime().exec("touch /home/Images/connected2");
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


    private String getCommandFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[0].toUpperCase();
    }

    private String getPathFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        return msgSplited[1];
    }

    private String[] getParametersFromMsg(String msg) {
        String[] msgSplited = msg.split(" ");
        String[] parameters = Arrays.copyOfRange(msgSplited,2,msgSplited.length);
        return parameters;
    }

    private String getMsgFromMainServer(BufferedReader is) throws IOException {
        return is.readLine();
    }

    void sendMessage(String msg, PrintWriter os) {
        os.print(msg + "\r\n");
        os.flush();
    }

    private void decisionMaker(Socket inSocket) throws IOException {
        Process c = Runtime.getRuntime().exec("touch /home/Images/abcde");
        BufferedReader is = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));

        //  PrintWriter os = new PrintWriter(inSocket.getOutputStream(), true);
        System.out.println(getName() + " starting, IP=" + inSocket.getInetAddress());

        String msgReceived = getMsgFromMainServer(is);
        String command = getCommandFromMsg(msgReceived);
        String[] parameters = getParametersFromMsg(msgReceived);
        String path = getPathFromMsg(msgReceived);
        //DEBUG
        System.out.println(msgReceived);


        GaussianFilter filter = new GaussianFilter();
        filter.apply(path,parameters);

        closeConnection(inSocket);

    }
}

class GaussianFilter {
    static final String currentPath = new File("").getAbsolutePath();

    String getFilename (String path) {
        File f = new File(path);
        return f.getName();
    }

    void apply(String path, String[] parameters){

        try {

            File f = null;
            //  File f2 = null;
            String OS = System.getProperty("os.name").toLowerCase();

            if (OS.contains("win")) {
                if (System.getProperty("sun.arch.data.model").equals("32")) {
                    // 32-bit JVM
                    f = new File(currentPath +  "/Libs/x86/opencv_java410.dll");
                } else {
                    // 64-bit JVM
                    f = new File(currentPath + "/Libs/x64/opencv_java410.dll");
                }

            } else {
                if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
                    //   f = new File("/usr/lib/libopencv_java410.so");
                    f = new File("/home/razvan/Workspace/Image-editor-in-cloud-docker/Libs/opencv4/libopencv_java410.so");
                    //         f2 = new File("/usr/lib/libopencv_imgcodecs.so");
                }
            }
            Process d = Runtime.getRuntime().exec("touch /home/Images/abcdefgh1");
            if (f != null) {
                System.load(f.getAbsolutePath());
                //      System.load(f2.getAbsolutePath());


                System.out.println(f.getAbsolutePath());
                //    System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
            } else
                System.out.println("ERROR! System.load(f.getAbsolutePath());");
            Process e = Runtime.getRuntime().exec("touch /home/Images/abcdefgh2");
            //  Mat source = Imgcodecs.imread(path,Imgcodecs.IMREAD_COLOR);
            Mat source = Imgcodecs.imread(path, Imgcodecs.IMREAD_COLOR);
            Process h = Runtime.getRuntime().exec("touch /home/Images/abcdefgh3");
            Mat destination = new Mat(source.rows(), source.cols(), source.type());
            Process g = Runtime.getRuntime().exec("touch /home/Images/abcdefgh4");
            Imgproc.GaussianBlur(source, destination, new Size(Double.valueOf(parameters[0]), Double.valueOf(parameters[1])), Double.valueOf(parameters[2]));
            Imgcodecs.imwrite(currentPath + "/Images/[Processed-GaussianFilter]" + getFilename(path), destination);
            //   Imgcodecs.imwrite("/home/Images/[Processed-GaussianFilter]" +  getFilename(path), destination);

        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
