import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

class ImageShapeConversionServer {

    private static final int FIXEDPORT = 20002;
    private static final int NUM_THREADS = 3;

    /**
     * Constructor
     */
    private ImageShapeConversionServer(int port, int numThreads) {
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
        new ImageShapeConversionServer(FIXEDPORT, NUM_THREADS);

    }
}

/**
 * A Thread subclass to handle one client conversation.
 */
class Handler extends Thread {
    /**
     * Parameters
     */
    private ServerSocket specializedServerSocket;
    private int threadNumber;

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
        String[] parameters = Arrays.copyOfRange(msgSplited, 2, msgSplited.length);
        return parameters;
    }

    private String getMsgFromMainServer(BufferedReader is) throws IOException {
        return is.readLine();
    }

    private void decisionMaker(Socket inSocket) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(inSocket.getInputStream()));
        System.out.println(getName() + " starting, IP=" + inSocket.getInetAddress());

        String msgReceived = getMsgFromMainServer(is);
        String[] parameters = getParametersFromMsg(msgReceived);
        String path = getPathFromMsg(msgReceived);

        //DEBUG
        System.out.println(msgReceived);

        ImageShapeConversion filter = new ImageShapeConversion();
        filter.apply(path, parameters);

        closeConnection(inSocket);

    }
}

class ImageShapeConversion {
    static final String currentPath = new File("").getAbsolutePath();

    String getFilename(String path) {
        File f = new File(path);
        return f.getName();
    }

    void apply(String path, String[] parameters) {

        try {

            File f = null;
            String OS = System.getProperty("os.name").toLowerCase();

            if (OS.contains("win")) {
                if (System.getProperty("sun.arch.data.model").equals("32")) {
                    // 32-bit JVM
                    f = new File(currentPath + "/Libs/x86/opencv_java410.dll");
                } else {
                    // 64-bit JVM
                    f = new File(currentPath + "/Libs/x64/opencv_java410.dll");
                }

            } else {
                if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
                    f = new File("/usr/lib/libopencv_java410.so");
                }
            }

            System.load(f.getAbsolutePath());
            File input = new File(path);
            BufferedImage image = ImageIO.read(input);

            byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            mat.put(0, 0, data);

            Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
            Core.flip(mat, mat1, -1);

            byte[] data1 = new byte[mat1.rows() * mat1.cols() * (int) (mat1.elemSize())];
            mat1.get(0, 0, data1);
            BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
            image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);

            File output = new File("/home/Images/[Processed-ImageShapeConversion]" + getFilename(path));
            ImageIO.write(image1, "jpg", output);


        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
