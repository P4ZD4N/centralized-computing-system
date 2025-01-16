import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationService extends Thread {

    private final int port;

    public CommunicationService(int port) {
        this.port = port;
    }

    public void start() {

        System.out.println("\nCommunication Service started!");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.out.println("\nError in Communication Service: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {

        private final Socket clientSocket;
        private final String clientAddressAndPort;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            clientAddressAndPort = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            ) {
                String message;

                while ((message = in.readLine()) != null) {
                    String[] dividedMessage = message.split(" ");

                    if (dividedMessage.length != 3) {
                        out.write("usage: OPER <NUM1> <NUM2>");
                        out.newLine();
                        out.flush();
                        System.out.println("\nReceived invalid message: " + message + " from " + clientAddressAndPort);
                        continue;
                    }

                    String operation = dividedMessage[0];
                    int firstNumber;
                    int secondNumber;

                    try {
                        firstNumber = Integer.parseInt(dividedMessage[1]);
                        secondNumber = Integer.parseInt(dividedMessage[2]);
                    } catch (NumberFormatException e) {
                        out.write("Both numbers must be integer");
                        out.newLine();
                        out.flush();
                        System.out.println("\nReceived invalid arguments from " + clientAddressAndPort);
                        continue;
                    }

                    int result;
                    int restFromDivision;

                    switch (operation) {
                        case "ADD":
                            System.out.println("\nReceived valid ADD operation from " + clientAddressAndPort);
                            result = firstNumber + secondNumber;
                        break;

                        case "SUB":
                            System.out.println("\nReceived valid SUB operation from " + clientAddressAndPort);
                            result = firstNumber - secondNumber;
                        break;

                        case "MUL":
                            System.out.println("\nReceived valid MUL operation from " + clientAddressAndPort);
                            result = firstNumber * secondNumber;
                        break;

                        case "DIV":
                            if (secondNumber == 0) {
                                out.write("First number can't be divided by 0");
                                out.newLine();
                                out.flush();
                                System.out.println("\nReceived invalid DIV operation from " + clientAddressAndPort);
                                continue;
                            }

                            System.out.println("\nReceived valid DIV operation from " + clientAddressAndPort);
                            result = firstNumber / secondNumber;
                            restFromDivision = firstNumber % secondNumber;

                            out.write("Result of " + operation + ": " + result + ", Rest from division: " + restFromDivision);
                            out.newLine();
                            out.flush();
                        continue;

                        default:
                            out.write("Invalid operation: " + operation);
                            out.newLine();
                            out.flush();
                            System.out.println("\nReceived invalid operation: " + operation + " from " + clientAddressAndPort);
                        continue;
                    }

                    out.write("Result of " + operation + ": " + result);
                    out.newLine();
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("\nError in Communication Service: " + e.getMessage());
            }
        }
    }
}
