import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class CommunicationService extends Thread {

    private final int port;
    private static int numberOfComputedOperations;
    private static int numberOfAddOperations;
    private static int numberOfSubOperations;
    private static int numberOfMulOperations;
    private static int numberOfDivOperations;
    private static int numberOfWrongOperations;
    private static int sumOfAllResults;

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

    public static int getNumberOfComputedOperations() {
        return numberOfComputedOperations;
    }

    public static void setNumberOfComputedOperations(int numberOfComputedOperations) {
        CommunicationService.numberOfComputedOperations = numberOfComputedOperations;
    }

    public static int getNumberOfAddOperations() {
        return numberOfAddOperations;
    }

    public static void setNumberOfAddOperations(int numberOfAddOperations) {
        CommunicationService.numberOfAddOperations = numberOfAddOperations;
    }

    public static int getNumberOfSubOperations() {
        return numberOfSubOperations;
    }

    public static void setNumberOfSubOperations(int numberOfSubOperations) {
        CommunicationService.numberOfSubOperations = numberOfSubOperations;
    }

    public static int getNumberOfMulOperations() {
        return numberOfMulOperations;
    }

    public static void setNumberOfMulOperations(int numberOfMulOperations) {
        CommunicationService.numberOfMulOperations = numberOfMulOperations;
    }

    public static int getNumberOfDivOperations() {
        return numberOfDivOperations;
    }

    public static void setNumberOfDivOperations(int numberOfDivOperations) {
        CommunicationService.numberOfDivOperations = numberOfDivOperations;
    }

    public static int getNumberOfWrongOperations() {
        return numberOfWrongOperations;
    }

    public static void setNumberOfWrongOperations(int numberOfWrongOperations) {
        CommunicationService.numberOfWrongOperations = numberOfWrongOperations;
    }

    public static int getSumOfAllResults() {
        return sumOfAllResults;
    }

    public static void setSumOfAllResults(int sumOfAllResults) {
        CommunicationService.sumOfAllResults = sumOfAllResults;
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

                        numberOfWrongOperations++;
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

                        numberOfWrongOperations++;
                        continue;
                    }

                    int result;
                    int restFromDivision;

                    switch (operation) {
                        case "ADD":
                            System.out.println("\nReceived valid ADD operation from " + clientAddressAndPort);
                            result = firstNumber + secondNumber;
                            numberOfAddOperations++;
                        break;

                        case "SUB":
                            System.out.println("\nReceived valid SUB operation from " + clientAddressAndPort);
                            result = firstNumber - secondNumber;
                            numberOfSubOperations++;
                        break;

                        case "MUL":
                            System.out.println("\nReceived valid MUL operation from " + clientAddressAndPort);
                            result = firstNumber * secondNumber;
                            numberOfMulOperations++;
                        break;

                        case "DIV":
                            if (secondNumber == 0) {
                                out.write("First number can't be divided by 0");
                                out.newLine();
                                out.flush();
                                System.out.println("\nReceived invalid DIV operation from " + clientAddressAndPort);

                                numberOfWrongOperations++;
                                continue;
                            }

                            System.out.println("\nReceived valid DIV operation from " + clientAddressAndPort);
                            result = firstNumber / secondNumber;
                            restFromDivision = firstNumber % secondNumber;

                            out.write("Result of " + operation + ": " + result + ", Rest from division: " + restFromDivision);
                            out.newLine();
                            out.flush();

                            sumOfAllResults += result;
                            numberOfDivOperations++;
                            numberOfComputedOperations++;
                        continue;

                        default:
                            out.write("Invalid operation: " + operation);
                            out.newLine();
                            out.flush();

                            numberOfWrongOperations++;
                            System.out.println("\nReceived invalid operation: " + operation + " from " + clientAddressAndPort);
                        continue;
                    }

                    out.write("Result of " + operation + ": " + result);
                    out.newLine();
                    out.flush();

                    sumOfAllResults += result;
                    numberOfComputedOperations++;
                }
            } catch (IOException e) {
                System.out.println("\nError in Communication Service: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("\nConnection with " + clientAddressAndPort + " closed.");
                } catch (IOException e) {
                    System.out.println("\nError while closing the socket: " + e.getMessage());
                }
            }
        }
    }
}
