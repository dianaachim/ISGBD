package GUI;

import Domain.Attribute;
import Domain.Databases;
import Domain.TablesNames;
import Service.IService;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class CreateTable extends UnicastRemoteObject implements Serializable, Observer {
    @FXML
    private TextField TableNameField;

    @FXML
    private TableView<Attribute> tableView;

    @FXML
    private TableColumn nameC;
    @FXML
    private TableColumn typeC;

    @FXML
    private TableColumn countC;

    @FXML
    private TableColumn pkC;

    @FXML
    private TableColumn ukC;

    @FXML
    private TableColumn fkC;

    @FXML
    private TableColumn referenceC;

    @FXML
    private Button saveTnBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField countField;

    @FXML
    private Button newBtn;

    @FXML
    private Button saveAttributeBtn;

    @FXML
    private CheckBox pkCB;

    @FXML
    private CheckBox fkCB;

    @FXML
    private TextField referenceField;

    @FXML
    private CheckBox ukCB;

    String tableName="";
    public CreateTable() throws RemoteException {
    }
    public IService service;

    public String databaseName;

    public ObservableList<Attribute> model;



    @FXML
    public void initialize() {

        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeC.setCellValueFactory(new PropertyValueFactory<>("type"));
        countC.setCellValueFactory(new PropertyValueFactory<>("count"));
        pkC.setCellValueFactory(new PropertyValueFactory<>("pk"));
        ukC.setCellValueFactory(new PropertyValueFactory<>("uk"));
        fkC.setCellValueFactory(new PropertyValueFactory<>("fk"));
        referenceC.setCellValueFactory(new PropertyValueFactory<>("reference"));





    }
    public void setUp() throws Exception
    {
        model = FXCollections.observableArrayList();
        if(!this.tableName.equals(""))
        this.service.getAttributes(this.databaseName,this.tableName).forEach(model::add);
        tableView.setItems(model);

    }


    @FXML
    void loaddata() throws Exception {

        if(!this.tableName.equals("")){
        ArrayList<Attribute> list = (ArrayList<Attribute>) service.getAttributes(this.databaseName,this.tableName);
        ObservableList<Attribute> model = FXCollections.observableArrayList(list);


        tableView.setItems(model);}
    }
    @FXML
    void loaddata1(ActionEvent ev) throws Exception {

        System.out.println(service.getNames());
        ArrayList<Attribute> list = (ArrayList<Attribute>) service.getAttributes(this.databaseName,this.tableName);
        ObservableList<Attribute> model = FXCollections.observableArrayList(list);
        nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeC.setCellValueFactory(new PropertyValueFactory<>("type"));
        countC.setCellValueFactory(new PropertyValueFactory<>("count"));
        pkC.setCellValueFactory(new PropertyValueFactory<>("pk"));
        ukC.setCellValueFactory(new PropertyValueFactory<>("uk"));
        fkC.setCellValueFactory(new PropertyValueFactory<>("fk"));
        referenceC.setCellValueFactory(new PropertyValueFactory<>("reference"));


        tableView.setItems(model);
    }
    public void setService(IService service, String databaseName) throws Exception {
        this.service=service;
        try {
            System.out.println(this);

            loaddata();
            //blocks();
           // this.service.AddObserver((Observer) this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        this.databaseName=databaseName;
    }
    @FXML
    public void CreateT(ActionEvent ev) throws Exception
    {
        try {
            String dbName = TableNameField.getText();
            if (dbName.equals(" ") || dbName.equals("")) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The table name can not be empty!");
                errorAlert.showAndWait();
            }
            else {
                TablesNames db = new TablesNames(dbName,databaseName+"_"+dbName);
                this.service.createCollection(databaseName,dbName);
                this.service.addTableNames(db, this.databaseName);

/*
                Databases dbs1 = new Databases();
                dbs1.setDb(this.service.getNames());
                this.service.write(dbs1);*/
                this.tableName = dbName;
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("SUCCESS!");
                errorAlert.setContentText("The table with the name: " +tableName+" was created!");
                errorAlert.showAndWait();
            }
        } catch(Exception e)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The operation could not be performed!");
            errorAlert.showAndWait();
            e.printStackTrace();
        }


    }

    @FXML
    public void AddAttribute(ActionEvent ev) throws Exception
    {

        try {
            String tName = nameField.getText();
            String type = typeField.getText();
            Integer count = 0;
            if (!(countField.getText().equals("")) && !(countField.getText().equals(" "))) {
                count = Integer.parseInt(countField.getText());
            }
            String reference = referenceField.getText();
            Boolean pk = pkCB.isSelected();
            Boolean uk = ukCB.isSelected();
            Boolean fk = fkCB.isSelected();
            if(tName.equals("") || tName.equals(" ") || type.equals("") || type.equals(" "))
            {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText("The table name, type and count fields can not be empty!");
                errorAlert.showAndWait();
            }
            else {
                Attribute atr = new Attribute(tName, type, count, pk, fk, uk, reference);
                this.service.addAttribute(atr, this.databaseName, this.tableName);
                Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                errorAlert.setHeaderText("SUCCESS!");
                errorAlert.setContentText("The Attribute was created!");
                errorAlert.showAndWait();
               /* Databases dbs1 = new Databases();
                dbs1.setDb(this.service.getNames());
                this.service.write(dbs1);*/
            }
        } catch (Exception e)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The operation could not be performed!");
            errorAlert.showAndWait();
            e.printStackTrace();
        }


    }
    @FXML private void clearFields() throws Exception {
        nameField.setText("");
        countField.setText("");
        typeField.setText("");
        referenceField.setText("");
        pkCB.setSelected(false);
        fkCB.setSelected(false);
        ukCB.setSelected(false);
        loaddata();
    }
    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
}
