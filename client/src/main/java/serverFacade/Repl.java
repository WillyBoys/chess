package serverFacade;

import java.util.Scanner;

public class Repl {

    private final UserInteraction interaction;

    public Repl(String serverUrl) {
        interaction = new UserInteraction(serverUrl);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.nextLine();

            try {
                result = interaction.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }
}