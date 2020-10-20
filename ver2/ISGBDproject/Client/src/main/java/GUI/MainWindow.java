package GUI;

import Domain.Databases;
import Domain.DatabasesNames;
import Service.IService;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MainWindow extends UnicastRemoteObject implements Serializable, Observer {
    @FXML
    private Button CreateDbBtn;

    @FXML
    private TableView<DatabasesNames> DbTableiew;

    @FXML
    private TableColumn dbNameColumn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button createTableBtn;

    public IService service;
    public ObservableList<DatabasesNames> model;

    public MainWindow() throws RemoteException {
    }

    @Override
    public void Notify(Databases databases) throws RemoteException {
        try
        {
            ObservableList<DatabasesNames> observableList= FXCollections.observableArrayList();
            databases.getDb().forEach(f->
            {
                observableList.add(f);
            });
            DbTableiew.setItems(observableList);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @FXML
    public void initialize() {

        dbNameColumn.setCellValueFactory(new PropertyValueFactory<>("databaseName"));

    }

    public void setUp() throws Exception
    {
        model = FXCollections.observableArrayList();
        this.service.getNames().forEach(model::add);
        DbTableiew.setItems(model);

    }

    @FXML
    void loaddata() throws Exception {

        System.out.println(service.getNames());
        ArrayList<DatabasesNames> list = (ArrayList<DatabasesNames>) service.getNames();
        ObservableList<DatabasesNames> model = FXCollections.observableArrayList(list);


        DbTableiew.setItems(model);
    }

    @FXML
    void loaddata1(ActionEvent ev) throws Exception {

        System.out.println(service.getNames());
        ArrayList<DatabasesNames> list = (ArrayList<DatabasesNames>) service.getNames();
        ObservableList<DatabasesNames> model = FXCollections.observableArrayList(list);
        dbNameColumn.setCellValueFactory(new PropertyValueFactory<>("databaseName"));


        DbTableiew.setItems(model);
    }

    @FXML
    public void DeleteDB(ActionEvent ev) throws Exception
    {
        try{
            DatabasesNames p = DbTableiew.getSelectionModel().getSelectedItem();
            this.service.removeDatabasesNames(p.getDatabaseName());
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("SUCCESS!");
            errorAlert.setContentText("The database was deleted!");
            errorAlert.showAndWait();
        }
        catch(Exception e)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The operation could not be performed!");
            errorAlert.showAndWait();
        }
        /*Databases dbs1=new Databases();
        dbs1.setDb(this.service.getNames());
        this.service.write(dbs1);*/

    }

    public void setService(IService service) throws Exception {
        this.service = service;
        try {
            System.out.println(this);

            loaddata();
            //blocks();
            this.service.AddObserver((Observer) this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void OpenCreateDB(ActionEvent ev) throws IOException {
        try{
            //FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("CreateDB.fxml"));
            // FXMLLoader fxmlLoader = new FXMLLoader(CreateDB.class.getResource("../../../resources/CreateDB.fxml"));
            // fxmlLoader.setLocation(getClass().getResource("C:/Users/Raluca/Documents/masterBD/AN1/ISGBD/final/LabISGBD/Client/src/main/resources/CreateDB.fxml"));

            FXMLLoader fxmlLoader=new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("../../resources/CreateDB.fxml"));
            //System.out.println(getClass().getResource("C:\\Users\\Raluca\\Documents\\masterBD\\AN1\\ISGBD\\final\\LabISGBD\\Client\\src\\main\\resources\\CreateDB.fxml"));
            System.out.println(getClass().getResource("../../resources/CreateDB.fxml"));
            Parent root1=(Parent) fxmlLoader.load();
            Stage stage=new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            CreateDB ctrl = fxmlLoader.getController();
            ctrl.setService(service);
        } catch (Exception e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The window could not be opened!");
            errorAlert.showAndWait();
            e.printStackTrace();
        }

    }
}
