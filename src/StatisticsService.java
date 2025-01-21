import java.util.Timer;
import java.util.TimerTask;

public class StatisticsService {

    public void start() {
        System.out.println("\nStatistics started!");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                CCSFindingService.setNumberOfConnectedClients(
                        CCSFindingService.getNumberOfConnectedClients() + CCSFindingService.getNumberOfConnectedClientsFromLast10Seconds());
                CommunicationService.setNumberOfComputedOperations(
                        CommunicationService.getNumberOfComputedOperations() + CommunicationService.getNumberOfComputedOperationsFromLast10Seconds());
                CommunicationService.setNumberOfAddOperations(
                        CommunicationService.getNumberOfAddOperations() + CommunicationService.getNumberOfAddOperationsFromLast10Seconds());
                CommunicationService.setNumberOfSubOperations(
                        CommunicationService.getNumberOfSubOperations() + CommunicationService.getNumberOfSubOperationsFromLast10Seconds());
                CommunicationService.setNumberOfMulOperations(
                        CommunicationService.getNumberOfMulOperations() + CommunicationService.getNumberOfMulOperationsFromLast10Seconds());
                CommunicationService.setNumberOfDivOperations(
                        CommunicationService.getNumberOfDivOperations() + CommunicationService.getNumberOfDivOperationsFromLast10Seconds());
                CommunicationService.setNumberOfWrongOperations(
                        CommunicationService.getNumberOfWrongOperations() + CommunicationService.getNumberOfWrongOperationsFromLast10Seconds());
                CommunicationService.setSumOfAllResults(
                        CommunicationService.getSumOfAllResults() + CommunicationService.getSumOfAllResultsFromLast10Seconds());

                System.out.println("\n---");
                System.out.println("Statistics since launch");
                System.out.println("- Number of new connected clients: " + CCSFindingService.getNumberOfConnectedClients());
                System.out.println("- Number of computed operations: " + CommunicationService.getNumberOfComputedOperations());
                System.out.println("- Number of ADD operations: " + CommunicationService.getNumberOfAddOperations());
                System.out.println("- Number of SUB operations: " + CommunicationService.getNumberOfSubOperations());
                System.out.println("- Number of MUL operations: " + CommunicationService.getNumberOfMulOperations());
                System.out.println("- Number of DIV operations: " + CommunicationService.getNumberOfDivOperations());
                System.out.println("- Number of wrong operations: " + CommunicationService.getNumberOfWrongOperations());
                System.out.println("- Sum of all results: " + CommunicationService.getSumOfAllResults());
                System.out.println("\n---");

                System.out.println("\n---");
                System.out.println("Statistics from last 10 seconds");
                System.out.println("- Number of new connected clients: " + CCSFindingService.getNumberOfConnectedClientsFromLast10Seconds());
                System.out.println("- Number of computed operations: " + CommunicationService.getNumberOfComputedOperationsFromLast10Seconds());
                System.out.println("- Number of ADD operations: " + CommunicationService.getNumberOfAddOperationsFromLast10Seconds());
                System.out.println("- Number of SUB operations: " + CommunicationService.getNumberOfSubOperationsFromLast10Seconds());
                System.out.println("- Number of MUL operations: " + CommunicationService.getNumberOfMulOperationsFromLast10Seconds());
                System.out.println("- Number of DIV operations: " + CommunicationService.getNumberOfDivOperationsFromLast10Seconds());
                System.out.println("- Number of wrong operations: " + CommunicationService.getNumberOfWrongOperationsFromLast10Seconds());
                System.out.println("- Sum of all results: " + CommunicationService.getSumOfAllResultsFromLast10Seconds());
                System.out.println("---");

                CCSFindingService.setNumberOfConnectedClientsFromLast10Seconds(0);
                CommunicationService.setNumberOfComputedOperationsFromLast10Seconds(0);
                CommunicationService.setNumberOfAddOperationsFromLast10Seconds(0);
                CommunicationService.setNumberOfSubOperationsFromLast10Seconds(0);
                CommunicationService.setNumberOfMulOperationsFromLast10Seconds(0);
                CommunicationService.setNumberOfDivOperationsFromLast10Seconds(0);
                CommunicationService.setNumberOfWrongOperationsFromLast10Seconds(0);
                CommunicationService.setSumOfAllResultsFromLast10Seconds(0);
            }
        };

        timer.scheduleAtFixedRate(task, 10000, 10000);
    }
}
