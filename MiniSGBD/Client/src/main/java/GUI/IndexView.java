package GUI;


import Domain.Attribute;
import Domain.DTO;
import Domain.Databases;
import Domain.Index;
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

public class IndexView  extends UnicastRemoteObject implements Serializable, Observer {





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




    String tableName;

    public IService service;

    public String databaseName;

    public ObservableList<Attribute> model;
    public IndexView() throws RemoteException {
    }



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
    public void setService(IService service, String databaseName,String tableName) throws Exception {
        this.service=service;
        this.tableName=tableName;
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
    public void createIndex(ActionEvent ev) throws Exception
    {
        try {
            Attribute p = tableView.getSelectionModel().getSelectedItem();
            Index i;
            if (p.getPk()) i = new Index(p.getName(), "clustered",databaseName+"_"+tableName);
            else {i = new Index(p.getName(), "unclustered",databaseName+"_"+tableName+"_"+p.getName());
            for(String s:service.getUk(databaseName,tableName,service.findNr(databaseName,tableName,p.getName())))
                if(this.service.findUkI(databaseName,tableName,p.getName(),s))
                    this.service.updateI(databaseName,tableName,p.getName(),new DTO(s,this.service.getValueByKeyI(databaseName,tableName,p.getName(),s)+service.getPk(databaseName,tableName)+"#"));
                else
                service.addI(databaseName,tableName,p.getName(),new DTO(s,service.getPk(databaseName,tableName)+"#"));

            }
            this.service.addIndex(this.databaseName, this.tableName, i);
            /*Databases dbs1 = new Databases();
            dbs1.setDb(this.service.getNames());
            this.service.write(dbs1);*/
            Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
            errorAlert.setHeaderText("SUCCESS!");
            errorAlert.setContentText("The index was created!");
            errorAlert.showAndWait();
        }catch(Exception e)
        {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("The operation could not be performed!");
            errorAlert.showAndWait();
            e.printStackTrace();
        }


    }
    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
}