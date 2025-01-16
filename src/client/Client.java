package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java client.Client <port>");
            return;
        }

        int port;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Port must be integer!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        String message;
        InetAddress serverAddress = null;

        try {
            byte[] buffer = new byte[1024];

            try (DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(3000);

                while (serverAddress == null) {
                    System.out.print("\nEnter message: ");
                    message = sc.nextLine();

                    broadcastMessage(socket, message, port);

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    try {
                        socket.receive(packet);
                        serverAddress = packet.getAddress();

                        String responseContent = new String(packet.getData(), 0, packet.getLength()).trim();
                        System.out.println("Response content: " + responseContent);
                    } catch (SocketTimeoutException e) {
                        System.out.println("No response received within 3 seconds. Please try again!");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error in client: " + e.getMessage());
        }
    }

    private static void broadcastMessage(DatagramSocket socket, String message, int port) throws IOException {

        byte[] messageBytes = message.getBytes();
        DatagramPacket broadcastPacket = new DatagramPacket(
                messageBytes,
                messageBytes.length,
                InetAddress.getByName("255.255.255.255"),
                port
        );

        socket.setBroadcast(true);
        socket.send(broadcastPacket);
        System.out.println("Broadcasted message: " + message);
    }
}

