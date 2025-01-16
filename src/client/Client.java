package client;

import java.io.*;
import java.net.*;
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

        InetAddress serverAddress = findServerAddress(port);
        sendOperationsToServer(serverAddress, port);

    }
    private static InetAddress findServerAddress(int port) {
        Scanner sc = new Scanner(System.in);
        String message;
        InetAddress serverAddress = null;

        try {
            byte[] buffer = new byte[1024];

            try (DatagramSocket udpSocket = new DatagramSocket()) {
                udpSocket.setSoTimeout(3000);

                while (serverAddress == null) {
                    System.out.print("\nEnter message: ");
                    message = sc.nextLine();

                    broadcastMessage(udpSocket, message, port);

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    try {
                        udpSocket.receive(packet);
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

        return serverAddress;
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

    private static void sendOperationsToServer(InetAddress serverAddress, int port) {
        System.out.println("Connected with server on address: " + serverAddress.getHostAddress() + ":" + port);

        Scanner sc = new Scanner(System.in);
        String operation;

        try (Socket tcpSocket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream()));
        ) {
            tcpSocket.setSoTimeout(3000);

            while (true) {
                System.out.print("\nEnter operation: ");
                operation = sc.nextLine();

                try {
                    out.write(operation);
                    out.newLine();
                    out.flush();

                    String serverResponse = in.readLine();
                    if (serverResponse != null) System.out.println(serverResponse);
                } catch (SocketTimeoutException e) {
                    System.out.println("No response received within 3 seconds. Please try again!");
                }
            }
        } catch (IOException e) {
            System.out.println("\nError in client: " + e.getMessage());
        }
    }
}

