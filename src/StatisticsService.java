import java.util.Timer;
import java.util.TimerTask;

public class StatisticsService {

    public void start() {
        System.out.println("\nStatistics started!");

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("\n---");
                System.out.println("Statistics from last 10 seconds");
                System.out.println("- Number of new connected clients: " + CCSFindingService.getNumberOfConnectedClients());
                System.out.println("- Number of computed operations: " + CommunicationService.getNumberOfComputedOperations());
                System.out.println("- Number of ADD operations: " + CommunicationService.getNumberOfAddOperations());
                System.out.println("- Number of SUB operations: " + CommunicationService.getNumberOfSubOperations());
                System.out.println("- Number of MUL operations: " + CommunicationService.getNumberOfMulOperations());
                System.out.println("- Number of DIV operations: " + CommunicationService.getNumberOfDivOperations());
                System.out.println("- Number of wrong operations: " + CommunicationService.getNumberOfWrongOperations());
                System.out.println("- Sum of all results: " + CommunicationService.getSumOfAllResults());
                System.out.println("---");

                CCSFindingService.setNumberOfConnectedClients(0);
                CommunicationService.setNumberOfComputedOperations(0);
                CommunicationService.setNumberOfAddOperations(0);
                CommunicationService.setNumberOfSubOperations(0);
                CommunicationService.setNumberOfMulOperations(0);
                CommunicationService.setNumberOfDivOperations(0);
                CommunicationService.setNumberOfWrongOperations(0);
                CommunicationService.setSumOfAllResults(0);
            }
        };

        timer.scheduleAtFixedRate(task, 10000, 10000);
    }
}
