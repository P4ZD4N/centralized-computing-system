import java.net.DatagramSocket;
import java.net.SocketException;

public class CCS {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java -jar CCS.jar <port>");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Port must be integer!");
            return;
        }

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Port " + port + " is available!");
        } catch (SocketException e) {
            System.out.println("Port " + port + " is not available!");
        }

        runCCSFindingService(port);
    }

    private static void runCCSFindingService(int port) {
        CCSFindingService ccsFindingService = new CCSFindingService(port);
        ccsFindingService.start();
    }
}