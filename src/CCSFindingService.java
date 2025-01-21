import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CCSFindingService {

    private final int port;
    private static int numberOfConnectedClients;
    private static int numberOfConnectedClientsFromLast10Seconds;

    public CCSFindingService(int port) {
        this.port = port;
    }

    public void start() {

        System.out.println("\nCCS Finding Service started!");

        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                InetAddress senderAddress = packet.getAddress();
                int senderPort = packet.getPort();

                String receivedMessage = new String(packet.getData(), 0, packet.getLength()).trim();

                if (!receivedMessage.startsWith("CCS DISCOVER")) {
                    System.out.println(
                            "\nReceived invalid message: " +
                            receivedMessage +
                            " from " +
                            senderAddress.getHostAddress() + ":" + senderPort);
                    continue;
                }

                String response = "CCS FOUND";
                byte[] responseBytes = response.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(
                        responseBytes,
                        responseBytes.length,
                        senderAddress,
                        senderPort
                );

                socket.send(responsePacket);

                System.out.println("\nReceived valid message from " + senderAddress.getHostAddress() + ":" + senderPort);
                numberOfConnectedClientsFromLast10Seconds++;
            }
        } catch (IOException e) {
            System.out.println("\nError in CCS Finding Service: " + e.getMessage());
        }
    }

    public static int getNumberOfConnectedClients() {
        return numberOfConnectedClients;
    }

    public static void setNumberOfConnectedClients(int numberOfConnectedClients) {
        CCSFindingService.numberOfConnectedClients = numberOfConnectedClients;
    }

    public static int getNumberOfConnectedClientsFromLast10Seconds() {
        return numberOfConnectedClientsFromLast10Seconds;
    }

    public static void setNumberOfConnectedClientsFromLast10Seconds(int numberOfConnectedClientsFromLast10Seconds) {
        CCSFindingService.numberOfConnectedClientsFromLast10Seconds = numberOfConnectedClientsFromLast10Seconds;
    }
}
