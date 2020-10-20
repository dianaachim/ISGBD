package Server;

import org.apache.catalina.Server;

public class StartServer {

    public static void main(String[] args) {
        MyServer server = new MyServer();
        server.connectToServer();
    }
}
