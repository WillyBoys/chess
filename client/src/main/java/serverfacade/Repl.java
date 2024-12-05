package serverfacade;

import websocket.NotificationHandler;
import websocket.commands.UserGameCommand;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class Repl implements NotificationHandler {

    private final UserInteraction interaction;
    private final GamingInteraction gamingInteraction;

    public Repl(String serverUrl) {
        interaction = new UserInteraction(serverUrl, this);
        gamingInteraction = new GamingInteraction(serverUrl, this);
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