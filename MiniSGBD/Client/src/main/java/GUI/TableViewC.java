package GUI;

import Domain.Databases;
import Domain.DatabasesNames;
import Domain.TablesNames;
import Service.IService;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class TableViewC extends UnicastRemoteObject implements Serializable, Observer {

    String databaseName;


    public IService service;
    @FXML
    private Button deleteBtn;

    @FXML
    private Button indexBtn;

    @FXML
    private TableView<TablesNames> tableView;

    @FXML
    private TableColumn nameC;

    public ObservableList<TablesNames> model;


    public TableViewC() throws RemoteException {
    }



    @FXML
    public void initialize() {

        nameC.setCellValueFactory(new PropertyValueFactory<>("tableName"));



    }
    public void setUp() throws Exception
    {
        model = FXCollections.observableArrayList();
        this.service.getTablesN(this.databaseName).forEach(model::add);
        tableView.setItems(model);

    }

    @FXML
    void loaddata() throws Exception {

        System.out.println(service.getNames());
        ArrayList<TablesNames> list = (ArrayList<TablesNames>) service.getTablesN(this.databaseName);
        ObservableList<TablesNames> model = FXCollections.observableArrayList(list);


        tableView.setItems(model);
    }
    @FXML
    void loaddata1(ActionEvent ev) throws Exception {

        System.out.println(service.getNames());
        ArrayList<TablesNames> list = (ArrayList<TablesNames>) service.getTablesN(this.databaseName);
        ObservableList<TablesNames> model = FXCollections.observableArrayList(list);

        nameC.setCellValueFactory(new PropertyValueFactory<>("tableName"));


        tableView.setItems(model);
    }


    public void setService(IService service, String databaseName) throws Exception {
        this.service=service;
        this.databaseName=databaseName;
        try {
            System.out.println(this);

            loaddata();
            //blocks();
            // this.service.AddObserver((Observer) this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
    @FXML
    public void OpenCreateIndex(ActionEvent ev) throws IOException {
        try{
            //FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreateDB.fxml"));
            // FXMLLoader fxmlLoader = new FXMLLoader(CreateDB.class.getResource("../../../resources/CreateDB.fxml"));
            // fxmlLoader.setLocation(getClass().getResource("C:/Users/Raluca/Documents/masterBD/AN1/ISGBD/final/LabISGBD/Client/src/main/resources/CreateDB.fxml"));
            TablesNames p = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader=new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("../../resources/IndexView.fxml"));
            //System.out.println(getClass().getResource("C:\\Users\\Raluca\\Documents\\masterBD\\AN1\\ISGBD\\final\\LabISGBD\\Client\\src\\main\\resources\\CreateDB.fxml"));
            System.out.println(getClass().getResource("../../resources/IndexView.fxml"));
            Parent root1=(Parent) fxmlLoader.load();
            Stage stage=new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            IndexView ctrl = fxmlLoader.getController();
            ctrl.setService(service,this.databaseName,p.getTableName());
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The window could not be opened!");
            errorAlert.showAndWait();
            e.printStackTrace();
            e.printStackTrace();
        }


    }
    @FXML
    public void OpenDataView(ActionEvent ev) throws IOException {
        try {

            TablesNames p = tableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("../../resources/DataView.fxml"));

            System.out.println(getClass().getResource("../../resources/DataView.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            DataView ctrl = fxmlLoader.getController();
            ctrl.setService(service, this.databaseName, p.getTableName());
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The window could not be opened!");
            errorAlert.showAndWait();
            e.printStackTrace();
            e.printStackTrace();
        }
    }

    @FXML
    public void DeleteTable(ActionEvent ev) throws Exception
    {

        try {
            TablesNames p = tableView.getSelectionModel().getSelectedItem();
            System.out.println(this.databaseName);
            System.out.println(p.getTableName());
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("SUCCESS!");
            errorAlert.setContentText("The table was deleted!");
            errorAlert.showAndWait();

            this.service.removeTablesNames(this.databaseName, p.getTableName());
        }catch(Exception e)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("Operation failed! The table could not be deleted!");
            errorAlert.showAndWait();
        }


    }
    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
}

