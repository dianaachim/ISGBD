package Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        try {
            Socket connectionToTheServer = new Socket("localhost", 9991); // First param: server-address, Second: the port
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
