package GUI;


import Domain.*;
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

public class DataView  extends UnicastRemoteObject implements Serializable, Observer {

int i=0;
String key="";
String value="";
String indexUK="";
String indexFK="";
    @FXML
    private Button nextBtn;
    @FXML
    private Button updBtn;
    @FXML
    private TableView<DTO> tableView;

    @FXML
    private TableColumn keyC;
    @FXML
    private TableColumn valueC;
    @FXML
    private Label label;
    @FXML
    private TextField textField;
    @FXML
    private TextField deleteField;


    String tableName;

    public IService service;

    public String databaseName;

    public ObservableList<DTO> model;

    public DataView() throws RemoteException, Exception {
    }


    @FXML
    public void initialize() throws Exception {
      /*  for(int i=0;i<this.service.getSize(databaseName,tableName);i++)
        {
            Label l=new Label(this.service.getAttributes(databaseName,tableName).get(i).toString());
            TextField textField=new TextField("txt"+i);
        }*/

       keyC.setCellValueFactory(new PropertyValueFactory<>("key"));
       valueC.setCellValueFactory(new PropertyValueFactory<>("value"));

    }

    public void setUp() throws Exception {
        model = FXCollections.observableArrayList();
        if (!this.tableName.equals(""))
            this.service.getDate(databaseName,tableName).forEach(model::add);
        tableView.setItems(model);


    }


    @FXML
    void loaddata() throws Exception {

        model = FXCollections.observableArrayList();
        if (!this.tableName.equals(""))
            this.service.getDate(databaseName,tableName).forEach(model::add);
        tableView.setItems(model);
    }

    @FXML
    void loaddata1(ActionEvent ev) throws Exception {

        System.out.println(service.getNames());
        ArrayList<DTO> list = (ArrayList<DTO>) this.service.getDate(databaseName,tableName);
        ObservableList<DTO> model = FXCollections.observableArrayList(list);
        keyC.setCellValueFactory(new PropertyValueFactory<>("key"));
        valueC.setCellValueFactory(new PropertyValueFactory<>("value"));


        tableView.setItems(model);
    }

    @FXML
    public void setService(IService service, String databaseName, String tableName) throws Exception {
        this.service = service;
        this.tableName = tableName;
        this.databaseName = databaseName;
        try {
            System.out.println(this);

            loaddata();
            //blocks();
            // this.service.AddObserver((Observer) this);
           // for(int i=0;i<this.service.getSize(databaseName,tableName);i++)
          //  {

                // TextField textField=new TextField("txt"+i);
           // }
            //label = new Label();
            label.setText(service.getAttributes(databaseName,tableName).get(0).getName());
            System.out.println(service.getAttributes(databaseName,tableName).get(0).getName());
            System.out.println(label.getText());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    @FXML
    public void Next(ActionEvent ev) throws Exception
    {

        if (i == service.getSize(databaseName, tableName) - 1) {
            if (service.getAttributes(databaseName, tableName).get(i).getPk()) {
                    if(!service.findPk(databaseName,tableName,textField.getText()))
                    {key = key+textField.getText();
                        DTO dto=new DTO(key,value);
                        this.service.insert(databaseName,tableName,dto);
                        nextBtn.setDisable(true);}
                    else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("ERROR!");
                        errorAlert.setContentText("Operation failed! The primary key already exists!");
                        errorAlert.showAndWait();

                    }


            } else {
                if(service.getAttributes(databaseName, tableName).get(i).getFk())
                {if(service.findPk(databaseName,service.getAttributes(databaseName, tableName).get(i).getReference(),textField.getText()))
                {value = value+textField.getText();
                    if(this.service.findUkI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText()))
                        this.service.updateI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),this.service.getValueByKeyI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText())+key+"#"));
                    else this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key+"#"));
                    DTO dto=new DTO(key,value);
                    this.service.insert(databaseName,tableName,dto);
                    nextBtn.setDisable(true);}
                    else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("ERROR!");
                        errorAlert.setContentText("Operation failed! The foreign key does not exist !");
                        errorAlert.showAndWait();

                    }}

                    else if(service.getAttributes(databaseName,tableName).get(i).getUk())
                {
                    if (!service.findUkI(databaseName, tableName, service.getAttributes(databaseName, tableName).get(i).getName(), textField.getText())) {
                        value = value + textField.getText();
                        DTO dto = new DTO(key, value);
                        this.service.insert(databaseName, tableName, dto);
                        this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key));
                        nextBtn.setDisable(true);
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("ERROR!");
                        errorAlert.setContentText("Operation failed! The unique key already  exists !");
                        errorAlert.showAndWait();
                    }


                }
                else{ if(this.service.findIndex(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName())!=null)
                { if(this.service.findUkI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText()))
                    this.service.updateI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),this.service.getValueByKeyI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText())+key+"#"));
                else this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key+"#"));
                    }
                    value = value + textField.getText();
                DTO dto = new DTO(key, value);
                this.service.insert(databaseName, tableName, dto);

                nextBtn.setDisable(true);}
            }


            textField.clear();
        } else{ if (service.getAttributes(databaseName, tableName).get(i).getPk()) {
            if(!service.findPk(databaseName,tableName,textField.getText()))
            { key = key+textField.getText();
                i++;
                label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
                textField.clear();}
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR!");
                errorAlert.setContentText("Operation failed! The primary key already exists!");
                errorAlert.showAndWait();

            }

        } else {
            if(service.getAttributes(databaseName, tableName).get(i).getFk())
            {if(service.findPk(databaseName,service.getAttributes(databaseName, tableName).get(i).getReference(),textField.getText()))
            {value = value+textField.getText()+"#";
                if(this.service.findUkI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText()))
                    this.service.updateI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),this.service.getValueByKeyI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText())+"#"+key));
                else this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key+"#"));
                i++;
                label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
                textField.clear();}
            else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR!");
                errorAlert.setContentText("Operation failed! The foreign key does not exist !");
                errorAlert.showAndWait();

            }}
            else /* {value = value+textField.getText()+"#";
            i++;
            label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
            textField.clear();}*/
                if(service.getAttributes(databaseName,tableName).get(i).getUk())
                {
                    if (!service.findUkI(databaseName, tableName, service.getAttributes(databaseName, tableName).get(i).getName(), textField.getText())) {
                        value = value+textField.getText()+"#";
                        this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key));
                        i++;
                        label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
                        textField.clear();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setHeaderText("ERROR!");
                        errorAlert.setContentText("Operation failed! The unique key already  exists !");
                        errorAlert.showAndWait();
                    }
                }


                else{if(this.service.findIndex(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName())!=null)
                { if(this.service.findUkI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText()))
                    this.service.updateI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),this.service.getValueByKeyI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),textField.getText())+key+"#"));
                else this.service.addI(databaseName,tableName,service.getAttributes(databaseName,tableName).get(i).getName(),new DTO(textField.getText(),key+"#"));}
                    value = value+textField.getText()+"#";
                    i++;
                    label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
                    textField.clear();}

        }

        }



    }

    @FXML
    public void update(ActionEvent ev) throws Exception
    {

        if (i == service.getSize(databaseName, tableName) - 1) {
            if (service.getAttributes(databaseName, tableName).get(i).getPk()) {
                key = key+textField.getText();

            } else {
                value = value+textField.getText();

            }
            DTO dto=new DTO(key,value);
            System.out.println(key+"-sdhfsd- "+value);
            this.service.update(databaseName,tableName,dto);

            updBtn.setDisable(true);

            textField.clear();
        } else{ if (service.getAttributes(databaseName, tableName).get(i).getPk()) {
            key =key+ textField.getText();

        } else {
            value = value+textField.getText() + "#";

        }
            i++;
            label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
            textField.clear();
        }



    }

    @FXML
    public void Insert(ActionEvent ev) throws Exception {

        i=0;
        label.setText(service.getAttributes(databaseName,tableName).get(i).getName());
        textField.clear();
        updBtn.setDisable(false);
        nextBtn.setDisable(false);
        key="";
        value="";


    }
    @FXML
    public void delete(ActionEvent ev) throws Exception {
        String key1=deleteField.getText();
        if(this.service.findAttributeRef(databaseName,tableName)!=null){
        TablesNames tbN=this.service.findAttributeRef(databaseName,tableName);
        String atr=this.service.findAttributeRefI(databaseName,tableName);
        if(this.service.findUkI(databaseName,tbN.getTableName(),atr,key1)){ Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("Operation failed! delete the children firs !");
            errorAlert.showAndWait();}
        else {
            if (service.findPk(databaseName, tableName, key1)) {
                List<String> values = this.service.getValueByKey(databaseName, tableName, key1);
                TablesNames tb = this.service.findTable(databaseName, tableName);
                int nr = 0;
                for (int k = 0; k < tb.getAttributeList().size(); k++) {

                    if (tb.getAttributeList().get(k).getUk())
                        this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                    else if (tb.getAttributeList().get(k).getFk()) {
                        String keyF = this.service.getKeyByValueI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                        this.service.updateI(databaseName, tableName, tb.getAttributeList().get(k).getName(), new DTO(keyF, this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).replace(key1 + "#", "")));
                        //this.service.deleteI(databaseName,tableName,tb.getAttributeList().get(k).getName(),key1);
                        if (this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).compareTo("") == 0) {

                            this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), "");
                        }
                    } else {
                        if (this.service.findIndex(databaseName, tableName, tb.getAttributeList().get(k).getName()) != null && !tb.getAttributeList().get(k).getPk())
                        //  this.service.deleteI(databaseName,tableName,tb.getAttributeList().get(k).getName(),key1);
                        {
                            String keyF = this.service.getKeyByValueI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                            this.service.updateI(databaseName, tableName, tb.getAttributeList().get(k).getName(), new DTO(keyF, this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).replace(key1 + "#", "")));
                            if (this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).compareTo("") == 0) {

                                this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), "");
                            }
                        }
                    }
                }
                this.service.delete(databaseName, tableName, key1);
                deleteField.clear();

            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("ERROR!");
                errorAlert.setContentText("Operation failed! The primary key does not  exist !");
                errorAlert.showAndWait();
            }
        }}
        else if (service.findPk(databaseName, tableName, key1)) {
            List<String> values = this.service.getValueByKey(databaseName, tableName, key1);
            TablesNames tb = this.service.findTable(databaseName, tableName);
            int nr = 0;
            for (int k = 0; k < tb.getAttributeList().size(); k++) {

                if (tb.getAttributeList().get(k).getUk())
                    this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                else if (tb.getAttributeList().get(k).getFk()) {
                    String keyF = this.service.getKeyByValueI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                    this.service.updateI(databaseName, tableName, tb.getAttributeList().get(k).getName(), new DTO(keyF, this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).replace(key1 + "#", "")));
                    //this.service.deleteI(databaseName,tableName,tb.getAttributeList().get(k).getName(),key1);
                    if (this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).compareTo("") == 0) {

                        this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), "");
                    }
                } else {
                    if (this.service.findIndex(databaseName, tableName, tb.getAttributeList().get(k).getName()) != null && !tb.getAttributeList().get(k).getPk())
                    //  this.service.deleteI(databaseName,tableName,tb.getAttributeList().get(k).getName(),key1);
                    {
                        String keyF = this.service.getKeyByValueI(databaseName, tableName, tb.getAttributeList().get(k).getName(), key1);
                        this.service.updateI(databaseName, tableName, tb.getAttributeList().get(k).getName(), new DTO(keyF, this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).replace(key1 + "#", "")));
                        if (this.service.getValueByKeyI(databaseName, tableName, tb.getAttributeList().get(k).getName(), keyF).compareTo("") == 0) {

                            this.service.deleteI(databaseName, tableName, tb.getAttributeList().get(k).getName(), "");
                        }
                    }
                }
            }
            this.service.delete(databaseName, tableName, key1);
            deleteField.clear();

        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("ERROR!");
            errorAlert.setContentText("Operation failed! The primary key does not  exist !");
            errorAlert.showAndWait();
        }
    }



    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
}
