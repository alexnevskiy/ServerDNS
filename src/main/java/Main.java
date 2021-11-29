import server.Server;

import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    private static Server server;
    private static final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        try {
            server = new Server();
            server.start();

            while (!server.isInterrupted()) {
                readCommand();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private static void readCommand() {
        if (scanner.hasNext()) {
            String text = scanner.nextLine();
            if (text.trim().length() > 0 && text.trim().equals("-exit")) {
                server.close();
            } else {
                System.out.println("Bad command. Enter \"-exit\" to close server");
            }
        }
    }
}
