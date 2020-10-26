package Server;

import Domain.Attribute;
import Domain.Databases;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyServer {
    private Service service;

    public MyServer() {
        Databases dbs = new Databases();
        service = new Service(dbs);
    }

//    public static void main(String[] args) {
//        connectToServer();
//    }

    public static void printMenu(PrintWriter serverPrintOut) {
        serverPrintOut.println("  Menu  ");
        serverPrintOut.println("1. Create database");
        serverPrintOut.println("2. Drop database");
        serverPrintOut.println("3. Create table");
        serverPrintOut.println("4. Drop table");
        serverPrintOut.println("4. Create index");
        serverPrintOut.println("0. Print menu");
        serverPrintOut.println("'done' for exit");
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

//            serverPrintOut.println("Hello World! Enter Peace to exit.");
            printMenu(serverPrintOut);

            //Have the server take input from the client and echo it back
            //This should be placed in a loop that listens for a terminator text e.g. bye
            boolean done = false;

            while(!done && scanner.hasNextLine()) {
                String line = scanner.nextLine();
//                serverPrintOut.println("Echo from Diana's Server: " + line);

                if(line.toLowerCase().trim().equals("done")) {
                    done = true;
                } else if (line.toLowerCase().trim().equals("1")) {
                    createDB(connectionSocket);
                    serverPrintOut.println("Next command...");
                } else if (line.toLowerCase().trim().equals("2")) {
                    dropDB(connectionSocket);
                    serverPrintOut.println("Next command...");
                } else if (line.toLowerCase().trim().equals("3")) {
                    createTable(connectionSocket);
                    serverPrintOut.println("Next command...");
                } else if (line.toLowerCase().trim().equals("4")) {
                    dropTable(connectionSocket);
                    serverPrintOut.println("Next command...");
                } else if (line.toLowerCase().trim().equals("5")) {
                    createIndex(connectionSocket);
                    serverPrintOut.println("Next command...");
                } else if (line.toLowerCase().trim().equals("0")) {
                    printMenu(serverPrintOut);
                }else {
                    serverPrintOut.println("Wrong command");
                }
            }
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public void createDB(Socket connectionSocket) throws IOException, JAXBException {
        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Database name: ");

        String line = scanner.nextLine();
        serverPrintOut.println(this.service.createDB(line));
    }

    public void dropDB(Socket connectionSocket) throws IOException, JAXBException {
        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Database name:");
        String line = scanner.nextLine();
        serverPrintOut.println(this.service.dropDB(line));

    }

    public void createTable(Socket connectionSocket) throws IOException, JAXBException {
        List<Attribute> attributeList = new ArrayList<>();

        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Table name: ");

        String tableName = scanner.nextLine();

        serverPrintOut.println("Database name: ");

        String databaseName = scanner.nextLine();

        serverPrintOut.println("Echo from Diana's Server: " + tableName + " " + databaseName);

        serverPrintOut.println("Now enter table attributes. When done, enter 'done'.");

        boolean done = false;

        serverPrintOut.println("Done? ");

        while(!done && scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if(line.toLowerCase().trim().equals("done")) {
                done = true;
            } else {
                serverPrintOut.println("name: ");
                String name = scanner.nextLine();
                serverPrintOut.println("type: ");
                String type = scanner.nextLine();
                serverPrintOut.println("length: ");
                String length = scanner.nextLine();
                serverPrintOut.println("not null(y/n): ");
                String notNull = scanner.nextLine();
                serverPrintOut.println("primary key(y/n): ");
                String pk = scanner.nextLine();
                serverPrintOut.println("unique key(y/n): ");
                String uk = scanner.nextLine();
                serverPrintOut.println("foreign key(y/n): ");
                String fk = scanner.nextLine();
                serverPrintOut.println("reference: ");
                String reference = scanner.nextLine();

                boolean not_null;
                boolean primary_key;
                boolean unique_key;
                boolean foreign_key;

                not_null = notNull.equals("y");
                primary_key = pk.equals("y");
                unique_key = uk.equals("y");
                foreign_key = fk.equals("y");

                Attribute attribute= new Attribute(name, type, Integer.parseInt(length), not_null, primary_key, unique_key, foreign_key, reference);
                attributeList.add(attribute);
                serverPrintOut.println("Done? ");
            }
        }
        serverPrintOut.println(this.service.createTable(tableName, databaseName, attributeList));

    }

    public void dropTable(Socket connectionSocket) throws IOException, JAXBException {
        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Table name: ");

        String tableName = scanner.nextLine();

        serverPrintOut.println("Database name: ");

        String databaseName = scanner.nextLine();

        serverPrintOut.println(this.service.dropTable(tableName, databaseName));

    }

    public void createIndex(Socket connectionSocket) throws IOException, JAXBException {

        InputStream inputToServer = connectionSocket.getInputStream();
        OutputStream outputFromServer = connectionSocket.getOutputStream();

        Scanner scanner = new Scanner(inputToServer, "UTF-8");
        PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

        serverPrintOut.println("Index name: ");
        String name = scanner.nextLine();

        serverPrintOut.println("ON table: ");
        String table = scanner.nextLine();

        serverPrintOut.println("From database: ");
        String database = scanner.nextLine();

        serverPrintOut.println("On column: ");
        String column = scanner.nextLine();

        serverPrintOut.println("Is Unique(y/n): ");
        String isUnique = scanner.nextLine();

        boolean unique = false;

        if (isUnique.equals("y"))
            unique = true;

        String message = this.service.createIndex(name, table, column, database, unique);
        serverPrintOut.println(message);
    }
}
