package GUI;

import Domain.Databases;
import Domain.DatabasesNames;
import Service.IService;
import Utils.Observer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CreateDB extends UnicastRemoteObject implements Serializable, Observer {
    @FXML
    private AnchorPane root;

    @FXML
    private TextField DBNameField;

    @FXML
    private Button SaveDbBtn;

    public CreateDB() throws RemoteException {
    }

    public IService service;
    public ObservableList<DatabasesNames> model;


    public void setService(IService service) throws Exception {
        this.service=service;
    }

    @Override
    public void Notify(Databases databases) throws RemoteException {

    }

    @FXML
    public void CreateDB(ActionEvent ev) throws Exception
    {
        try {
            String dbName = DBNameField.getText();
            if (dbName.equals(" ") || dbName.equals("")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The database name can not be empty!");
                errorAlert.showAndWait();
            }
            else {
                DatabasesNames db = new DatabasesNames(dbName);
                this.service.addDatabaseNames(db);
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("SUCCESS!");
                errorAlert.setContentText("The database was created!");
                errorAlert.showAndWait();
              /*  Databases dbs1 = this.service.read();
                dbs1.setDb(this.service.getNames());
                this.service.write(dbs1);
                List<DatabasesNames> dbn = this.service.getNames();
                for (DatabasesNames db1 : dbn) {
                    System.out.println(db1.toString());
                }*/
            }
        }
        catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The database could not be created!");
            errorAlert.showAndWait();
        }

    }
}
