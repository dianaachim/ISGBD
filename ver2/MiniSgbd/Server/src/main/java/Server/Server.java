package Server;

import Repository.Repository;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private Service service;

    public Server( Repository repo) {
        service = new Service(repo);
    }

    public void connectToServer() {
        //Try connect to the server on an unused port eg 9991. A successful connection will return a socket
        try(ServerSocket serverSocket = new ServerSocket(9991)) {
            Socket connectionSocket = serverSocket.accept();

            //Create Input&Outputstreams for the connection
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();

            Scanner scanner = new Scanner(inputToServer, "UTF-8");
            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);


            //Have the server take input from the client and echo it back
            //This should be placed in a loop that listens for a terminator text e.g. bye
            boolean done = false;

            serverPrintOut.println("Hello! Enter command: ");

            while(!done && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                serverPrintOut.println("Echo from Diana's Server: " + line);

                if(line.toLowerCase().trim().equals("exit")) {
                    done = true;
                }else {
                    serverPrintOut.println(this.service.checkCommand(line));
//                    this.service.checkCommand(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
