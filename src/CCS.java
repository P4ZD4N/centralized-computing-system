import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
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

        try (DatagramSocket socket = new DatagramSocket(port);
             ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Port " + port + " is available!");
        } catch (IOException e) {
            System.out.println("Port " + port + " is not available!");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Port must be integer from the range 0 - 65535!");
            return;
        }

        Thread ccsFindingServiceThread = new Thread(() -> runCCSFindingService(port));
        Thread communicationServiceThread = new Thread(() -> runCommunicationService(port));
        Thread statisticsServiceThread = new Thread(CCS::runStatisticsService);

        ccsFindingServiceThread.start();
        communicationServiceThread.start();
        statisticsServiceThread.start();

        System.out.println("Services are running on port " + port + "...");
    }

    private static void runCCSFindingService(int port) {
        CCSFindingService ccsFindingService = new CCSFindingService(port);
        ccsFindingService.start();
    }

    private static void runCommunicationService(int port) {
        CommunicationService communicationService = new CommunicationService(port);
        communicationService.start();
    }

    private static void runStatisticsService() {
        StatisticsService statisticsService = new StatisticsService();
        statisticsService.start();
    }
}