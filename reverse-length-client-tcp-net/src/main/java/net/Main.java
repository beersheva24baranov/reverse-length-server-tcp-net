package net;
import java.io.*;
import view.*;

public class Main {
    static TcpClient client;

    public static void main(String[] args) {
        Menu menu = new Menu("TCP Network application",
                Item.of("Start session", Main::startSession),
                Item.of("Exit", Main::exit, true));
        menu.perform(new StandardInputOutput());
    }

    static void startSession(InputOutput io) {
        String host = io.readString("Enter hostname");
        int port = io.readNumberRange("Enter port (9000-64000)", "Wrong port", 9000, 64000).intValue();
        tryingToClose();
        client = new TcpClient(host, port);
        Menu menu = new Menu("Run Session",
                Item.of("Enter command and data", Main::stringProcessing),
                Item.ofExit());
        menu.perform(io);
    }

    static void stringProcessing(InputOutput io) {
        String requestType = io.readString("Enter your command (reverse, lenght)");
        String requestData = io.readString("Enter your data");
        String response = client.sendAndReceive(requestType, requestData);
        io.writeLine(response);
    }

    static void exit(InputOutput io) {
        tryingToClose();
    }

    private static void tryingToClose() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}