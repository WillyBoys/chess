package serverfacade;

import websocket.SocketHandler;
import ui.EscapeSequences.*;

import javax.management.Notification;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class Repl extends SocketHandler {

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

    public void notify(Notification notification) {
        System.out.println(SET_TEXT_COLOR_RED + notificaiton.message());
    }

}