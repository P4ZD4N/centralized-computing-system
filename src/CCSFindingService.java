import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class CCSFindingService {

    private final int port;

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
                    System.out.println("\nReceived invalid message: " + receivedMessage);
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

                System.out.println("\nReceived valid message. Response sent to " + senderAddress.getHostAddress() + ":" + senderPort);
            }
        } catch (IOException e) {
            System.out.println("Error in CCS Finding Service: " + e.getMessage());
        }
    }
}