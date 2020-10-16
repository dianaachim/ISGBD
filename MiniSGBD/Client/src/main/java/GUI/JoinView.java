package GUI;
import Domain.*;
import Service.IService;
import Utils.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JoinView extends UnicastRemoteObject implements Serializable, Observer {

    @FXML
    private ComboBox tabel1Cb;

    @FXML
    private ComboBox joinCb;

    @FXML
    private ComboBox tabel2Cb;

    @FXML
    private ComboBox attr1Cb;

    @FXML
    private ComboBox relCb;

    @FXML
    private ComboBox attr2Cb;

    @FXML
    private Button joinBtn;

    @FXML
    private TableColumn<String, String> columnRes;

    @FXML
    private TableView<String> tableResult;

    public IService service;
    String databaseName;
    public ObservableList<DatabasesNames> model;
    public List<String> tablesList = new ArrayList<>();
    public List<String> onAttrsList = new ArrayList<>();
    public List<String> relList = new ArrayList<>();

    protected JoinView() throws RemoteException {
    }


    public void setService(IService service, String databaseName) throws Exception {
        this.service=service;

        this.databaseName=databaseName;
        loadFromTb();
        loadSelRel();
        joinRel();

    }

    void loadFromTb()
    {
        ArrayList<TablesNames> list = null;
        try {
            list = (ArrayList<TablesNames>) service.getTablesN(this.databaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> listS=new ArrayList<>();
        for(TablesNames t: list) {listS.add(t.getTableName());
            System.out.println(t.getTableName());}
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        listS
                );
        this.tabel1Cb.getItems().addAll(options);

    }
    @FXML
    void loadSelRel()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Normal","MIN","MAX","AVG","COUNT","SUM"
                );
        this.relCb.getItems().addAll(options);

    }

    @FXML
    void joinRel()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "NESTED LOOP JOIN","INNER LOOP JOIN","BLOCK NESTED LOOP JOIN","SORT MERGE JOIN"
                );
        this.joinCb.getItems().addAll(options);

    }
    @FXML
    public void Add(ActionEvent ev) throws Exception {
        if (tabel1Cb.getValue() != null && !tabel1Cb.getValue().toString().isEmpty() && tabel2Cb.getValue() != null && !tabel2Cb.getValue().toString().isEmpty()&& attr1Cb.getValue() != null && !attr1Cb.getValue().toString().isEmpty() && attr2Cb.getValue() != null && !attr2Cb.getValue().toString().isEmpty() && relCb.getValue() != null && !relCb.getValue().toString().isEmpty() && joinCb.getValue() != null && !joinCb.getValue().toString().isEmpty()  )
        {
            tablesList.add(tabel1Cb.getValue().toString());
            tablesList.add(tabel2Cb.getValue().toString());
            relList.add(relCb.getValue().toString());
            onAttrsList.add(attr1Cb.getValue().toString());
            onAttrsList.add(attr2Cb.getValue().toString());
        }
    }
    @FXML
    public void Make(ActionEvent ev) throws Exception {
    if(joinCb.equals("NESTED LOOP JOIN")){
        List<DTO> fr1=this.service.getDate(databaseName,tablesList.get(0));
        List<DTO> fr2=this.service.getDate(databaseName,tablesList.get(1));
        List<String> sel=new ArrayList<>();
        for(DTO d1 :fr1)
            for(DTO d2:fr2)
            {
                Attribute a1=this.service.findAttribute(databaseName,tablesList.get(0),onAttrsList.get(0));
                Attribute a2=this.service.findAttribute(databaseName,tablesList.get(1),onAttrsList.get(1));

                if(a1.getPk())
                {
                    if(a2.getPk()){
                        if (relList.get(0).equals("=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().equals(d2.getKey())) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())==Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals("<")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())<0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())<Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals(">")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())>0)sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())>Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals(">=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())>=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())>=Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals("<=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())<=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())<=Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        }
                    }
                    else{
                        int ci = this.service.findNr(databaseName, tablesList.get(1), onAttrsList.get(1));
                        int count = 0;
                        String[] arrOfStr = d2.getValue().split("#");
                        for (String s : arrOfStr) {
                            if (count == ci) {
                                if (relList.get(0).equals("=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().equals(s)) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())==Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals("<")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)<0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())<Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals(">")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)>0)sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())>Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals(">=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)>=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())>=Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals("<=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)<=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())<=Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                }
                            }
                        }
                    }
                }
                else{
                    if(!a2.getPk()){
                        if (relList.get(0).equals("=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().equals(d2.getKey())) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())==Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals("<")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())<0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())<Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals(">")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())>0)sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())>Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals(">=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())>=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())>=Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        } else  if (relList.get(0).equals("<=")) {
                            if(a1.getType().equals("varchar")){
                                if (d1.getKey().compareTo(d2.getKey())<=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                            else{ if(Float.parseFloat(d1.getKey())<=Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                        }
                    }
                    else{
                        int ci = this.service.findNr(databaseName, tablesList.get(0), onAttrsList.get(0));
                        int count = 0;
                        String[] arrOfStr = d2.getValue().split("#");
                        for (String s : arrOfStr) {
                            if (count == ci) {
                                if (relList.get(0).equals("=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (s.equals(d2.getKey())) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(s)==Float.parseFloat(d2.getKey()))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals("<")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)<0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())<Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals(">")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)>0)sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())>Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals(">=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)>=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())>=Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                } else  if (relList.get(0).equals("<=")) {
                                    if(a1.getType().equals("varchar")){
                                        if (d1.getKey().compareTo(s)<=0) sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                    else{ if(Float.parseFloat(d1.getKey())<=Float.parseFloat(s))sel.add(d1.getKey()+d1.getValue()+d2.getKey()+d2.getValue());}
                                }
                            }
                        }
                    }

                }
            }
    }

    }
    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
}
