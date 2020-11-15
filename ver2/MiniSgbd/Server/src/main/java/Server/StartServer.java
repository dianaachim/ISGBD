package Server;

import Repository.*;
import Repository.ConvertXML;
import Server.Server;

import javax.xml.bind.JAXBException;

public class StartServer {

    public static void main(String[] args) {
        try {
            ConvertXML converter = new ConvertXML();
            Repository repo = new Repository(converter);
//            MyServer server = new MyServer(repo);
            Server server = new Server(repo);
            server.connectToServer();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
