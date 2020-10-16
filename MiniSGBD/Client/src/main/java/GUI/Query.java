package GUI;

import Domain.*;
import Service.IService;
import Utils.Observer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.Attr;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Query extends UnicastRemoteObject implements Serializable, Observer {

    @FXML
    private ComboBox selectCB;

    @FXML
    private ComboBox fromCB;
    //final ComboBox fromCB = new ComboBox();
    @FXML
    private TextField textValue;

    @FXML
    private ComboBox whereCB;

    @FXML
    private ComboBox relationCB;

    @FXML
    private ComboBox selectRelationCB;

    @FXML
    private ComboBox hCB;
    @FXML
    private ComboBox hrCB;
    @FXML
    private ComboBox gbCB;
    @FXML
    private TextField hTF;

    @FXML
    private ComboBox arCB;


    @FXML
    private TableView<String> tableResult;

    @FXML
    private TableColumn<String,String> resultColumn;

    public List<String> tables = new ArrayList<>();

    public List<String> attributes = new ArrayList<>();

    public List<String> relations = new ArrayList<>();

    public List<String> wheres = new ArrayList<>();

    public List<String> values = new ArrayList<>();
    public List<String> gbAttributes=new ArrayList<>();
    public List<String> hAttributes=new ArrayList<>();
    public List<String> hRelations=new ArrayList<>();
    public List<String> hValues=new ArrayList<>();
    public List<String> selectAttrs = new ArrayList<>();
    public List<String> agrRel = new ArrayList<>();

    @FXML
    private Button AddTable;

    public Query() throws RemoteException {
    }

    public IService service;
    String databaseName;
    public ObservableList<DatabasesNames> model;


    public void setService(IService service, String databaseName) throws Exception {
        this.service=service;

        this.databaseName=databaseName;
        loadFromTb();
        loadSelRel();
        loadRel();

    }

    @FXML
    void loadSelRel()
    {
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Normal","MIN","MAX","AVG","COUNT","SUM"
                );
        this.selectRelationCB.getItems().addAll(options);
        this.arCB.getItems().addAll(options);

    }

    @FXML
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
       this.fromCB.getItems().addAll(options);

    }
    @FXML
    void loadRel()
    {

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "=","!=","<","<=",">",">="
                );
        this.relationCB.getItems().addAll(options);
        this.hrCB.getItems().addAll(options);

    }
    @FXML
    void loadSelect(String atr)
    {

        ArrayList<Attribute> list = null;
        try {
            list = (ArrayList<Attribute>) service.getAttributes(this.databaseName,atr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> listS=new ArrayList<>();
        for(Attribute t: list) {listS.add(t.getName());
            System.out.println(t.getName());}
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        listS
                );
        this.selectCB.getItems().addAll(options);
        this.whereCB.getItems().addAll(options);
        this.gbCB.getItems().addAll(options);
        this.hCB.getItems().addAll(options);

    }

    @Override
    public void Notify(Databases databases) throws RemoteException {

    }
    @FXML
    public void AddFrom(ActionEvent ev) throws Exception {
        if (fromCB.getValue() != null && !fromCB.getValue().toString().isEmpty())
        {
            tables.add(fromCB.getValue().toString());
            loadSelect(fromCB.getValue().toString());
        }
    }

    @FXML
    public void AddSelect(ActionEvent ev) throws Exception {
        if (selectCB.getValue() != null && !selectCB.getValue().toString().isEmpty() && selectRelationCB.getValue() != null && !selectRelationCB.getValue().toString().isEmpty() )
        {
            attributes.add(selectCB.getValue().toString());
            selectCB.setValue(null);
            selectAttrs.add(selectRelationCB.getValue().toString());
            selectRelationCB.setValue(null);

        }
    }
    public void AddSelectGB(ActionEvent ev) throws Exception {
        if (gbCB.getValue() != null && !gbCB.getValue().toString().isEmpty())
        {
            gbAttributes.add(gbCB.getValue().toString());
            gbCB.setValue(null);

        }
    }
    @FXML
    public void AddCond(ActionEvent ev) throws Exception {
        if (whereCB.getValue() != null && !whereCB.getValue().toString().isEmpty() && relationCB.getValue() != null && !relationCB.getValue().toString().isEmpty() && textValue.getText() != null && !textValue.getText().toString().isEmpty())
        {
            wheres.add(whereCB.getValue().toString());
            relations.add(relationCB.getValue().toString());
            values.add(textValue.getText());
            whereCB.setValue(null);
            relationCB.setValue(null);
            textValue.clear();
        }
    }
    @FXML
    public void AddCondH(ActionEvent ev) throws Exception {
        if (hCB.getValue() != null && !hCB.getValue().toString().isEmpty() && arCB.getValue() != null && !arCB.getValue().toString().isEmpty()&& hrCB.getValue() != null && !hrCB.getValue().toString().isEmpty() && hTF.getText() != null && !hTF.getText().toString().isEmpty())
        {
            hAttributes.add(hCB.getValue().toString());
            hRelations.add(hrCB.getValue().toString());
            hValues.add(hTF.getText());
            hCB.setValue(null);
            hrCB.setValue(null);
            hTF.clear();
            agrRel.add(arCB.getValue().toString());
            arCB.setValue(null);
        }
    }
    @FXML
    public void Delete(ActionEvent ev) throws Exception {
        System.out.println(attributes.size());
        tables.removeAll(tables);
        attributes.removeAll(attributes);
        wheres.removeAll(wheres);
        relations.removeAll(relations);
        values.removeAll(values);
        selectCB.getItems().removeAll(selectCB.getItems());
        whereCB.getItems().removeAll(whereCB.getItems());
        //relationCB.getItems().removeAll(relationCB.getItems());
        //selectRelationCB.getItems().removeAll(selectRelationCB.getItems());
        hCB.getItems().removeAll(hCB.getItems());
        //hrCB.getItems().removeAll(hrCB.getItems());
        gbCB.getItems().removeAll(gbCB.getItems());
        //arCB.getItems().removeAll(arCB.getItems());
        fromCB.setValue(null);
        hRelations.removeAll(hRelations);
        gbAttributes.removeAll(gbAttributes);
        hAttributes.removeAll(hAttributes);
        hValues.removeAll(hValues);
        selectAttrs.removeAll(selectAttrs);
        agrRel.removeAll(agrRel);


        System.out.println(attributes.size());
    }
    List<String> intersect(List<String> str,List<String> str2)
    {
        List<String> rez=new ArrayList<>();
        for(String s:str)
            if(str2.contains(s)) rez.add(s);
        return rez;
    }

    public List<String> Select(List<DTO> result_query_list) throws Exception {
        List<String> sel = new ArrayList<String>();
        for(String s: this.attributes)
        {
            int i=0;
            Attribute a=this.service.findAttribute(databaseName,tables.get(0),s);
            if(a.getPk()) for(DTO k: result_query_list){
                System.out.println("sel"+sel.size());
                System.out.println(this.service.getKeys(databaseName,tables.get(0)).size());
                if (sel.size() == result_query_list.size())  {
                    System.out.println("k1-"+sel.get(i));
                    sel.set(i, sel.get(i) + " " + k.getKey());
                    System.out.println("k1--"+sel.get(i));
                    i++;
                }
                else
                    sel.add(k.getKey());
            }
            else {
                i=0;
                int ci=this.service.findNr(databaseName,tables.get(0),s);
                for(DTO k: result_query_list){
                    int count = 0;

                    String[] arrOfStr = k.getValue().split("#");
                    for (String s2 : arrOfStr) {
                        if (count == ci) {
                            if (sel.size() == result_query_list.size()) {
                                System.out.println("v-"+sel.get(i));
                                System.out.println("-s2-"+s2);
                                sel.set(i, sel.get(i) + " " + s2);
                                System.out.println("v--" + sel.get(i));
                                i++;
                            } else sel.add(s2);
                        }
                        count++;


                    }
                }

            }

        }
        return sel;
    }


    public  HashMap<String, List<String>> GroupBy(List<DTO> array, String group_by) throws Exception {
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
        for (DTO d : array) {
            int count = 0;
            String[] arrOfStr = d.getValue().split("#");
            for (String s2 : arrOfStr) {
                if (count == this.service.findNr(databaseName, tables.get(0), group_by)) {
                    if (!hashMap.containsKey(s2)) {
                        List<String> list = new ArrayList<String>();
                        list.add(d.getKey());
                        hashMap.put(s2, list);
                    } else {
                        hashMap.get(s2).add(d.getKey());
                    }
                    }
                    count++;



            }

        }
        return hashMap;
    }


    //public Boolean Has_Attribute_Index()


    public List<DTO> Where() throws Exception {
        List<DTO> dates=new ArrayList<>();
            List<String> whereI=new ArrayList<>();
            List<String> whereNI=new ArrayList<>();
            List<String> relationsI=new ArrayList<>();
            List<String> relationsNI=new ArrayList<>();
            List<String> valuesI=new ArrayList<>();
            List<String> valuesNI=new ArrayList<>();
            int j=0;
            while (j<wheres.size())
            {if(this.service.findIndex(databaseName,tables.get(0),wheres.get(j))!=null){whereI.add(wheres.get(j));
                relationsI.add(relations.get(j));
                valuesI.add(values.get(j));}
            else{whereNI.add(wheres.get(j));
                relationsNI.add(relations.get(j));
                valuesNI.add(values.get(j));}
                j++;}
            if(whereI.size()!=0 && whereNI.size()!=0){
            List<DTO> dateI=this.service.getDtoIndex(databaseName,tables.get(0),whereI.get(0));
            List<String> rez=new ArrayList<>();
            String type=this.service.findAttribute(databaseName,tables.get(0),whereI.get(0)).getType();
            for(DTO k: dateI)
                if(relationsI.get(0).equals("=")) {
                if(type.equals("varchar")){
                if(k.getKey().equals(valuesI.get(0))){
                    String[] arrOfStr = k.getValue().split("#");
                    for (String s : arrOfStr) {

                        rez.add(s);

                    }
                }}
                else{
                    if(Float.parseFloat(k.getKey())==Float.parseFloat(valuesI.get(0)))
                    {
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez.add(s);

                        }
                    }
                }

            }
                else  if(relationsI.get(0).equals("<")) {
                    if(type.equals("varchar")){
                if(k.getKey().compareTo(valuesI.get(0))<0) {
                    String[] arrOfStr = k.getValue().split("#");
                    for (String s : arrOfStr) {

                        rez.add(s);

                    }
                }}else if(Float.parseFloat(k.getKey())<Float.parseFloat(valuesI.get(0))){
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez.add(s);

                        }
                    }

            }
                else  if(relationsI.get(0).equals(">")) {
                    if(type.equals("varchar")){
                        if(k.getKey().compareTo(valuesI.get(0))>0) {
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }
                    }
                    else if(Float.parseFloat(k.getKey())>Float.parseFloat(valuesI.get(0))){
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez.add(s);

                        }
                    }
            }
                else if(relationsI.get(0).equals(">=")) {
                    if(type.equals("varchar")){
                        if(k.getKey().compareTo(valuesI.get(0))>=0) {
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }
                    }
            else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(0))){
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez.add(s);

                        }
                    }}
                else  if(relationsI.get(0).equals("<=")) {
                    if(type.equals("varchar")){
                        if(k.getKey().compareTo(valuesI.get(0))<=0) {
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }
                    }
            else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(0))){String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez.add(s);

                        }}}


            int i=1;
            while(i<whereI.size())
            {   dateI.removeAll(dateI);
                dateI=this.service.getDtoIndex(databaseName,tables.get(0),whereI.get(i));
                List<String> rez1=new ArrayList<>();
                type=this.service.findAttribute(databaseName,tables.get(0),whereI.get(i)).getType();
               /* for(DTO k: dateI)
                    if(relationsI.get(i).equals("=")) {if(k.getKey().equals(valuesI.get(i))){
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez1.add(s);

                        }
                    }}
                    else  if(relationsI.get(i).equals("<")) {if(k.getKey().compareTo(valuesI.get(i))<0) {
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez1.add(s);

                        }
                    }}
                    else  if(relationsI.get(i).equals(">")) {if(k.getKey().compareTo(valuesI.get(i))>0) {
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez1.add(s);

                        }
                    }}
                    else if(relationsI.get(i).equals(">=")) {if(k.getKey().compareTo(valuesI.get(i))>=0) {
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez1.add(s);

                        }
                    }}
                    else  if(relationsI.get(i).equals("<=")) {if(k.getKey().compareTo(valuesI.get(i))<=0) {
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {

                            rez1.add(s);

                        }
                    }}*/
                for(DTO k: dateI)
                    if(relationsI.get(i).equals("=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().equals(valuesI.get(i))){
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }}
                        else{
                            if(Float.parseFloat(k.getKey())==Float.parseFloat(valuesI.get(i)))
                            {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }

                    }
                    else  if(relationsI.get(i).equals("<")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(i))<0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }}else if(Float.parseFloat(k.getKey())<Float.parseFloat(valuesI.get(i))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }

                    }
                    else  if(relationsI.get(i).equals(">")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(i))>0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>Float.parseFloat(valuesI.get(i))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }
                    }
                    else if(relationsI.get(i).equals(">=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(i))>=0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(i))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }}
                    else  if(relationsI.get(i).equals("<=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(i))<=0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(i))){String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }}}
                List<String> intr=new ArrayList<>();
                intr=intersect(rez,rez1);
                rez.removeAll(rez);
                for(String r:intr)rez.add(r);
                intr.removeAll(intr);
                i++;



            }
            List<DTO> datesNi=new ArrayList<>();
            List<DTO> rezNi=new ArrayList<>();
            for(String key:rez) datesNi.add(new DTO(key, this.service.getValueByKey1(databaseName,tables.get(0),key)));
            i=0;
            while(i<whereNI.size()) {
                System.out.println("rel " + relations.get(i));
                Attribute a = this.service.findAttribute(databaseName, tables.get(0), whereNI.get(i));
                 type=this.service.findAttribute(databaseName,tables.get(0),whereNI.get(i)).getType();
                if (a.getPk()) {
                    for (DTO k : datesNi)
                        if (relationsNI.get(i).equals("=")) {
                            if(type.equals("varchar")){
                                if (k.getKey().equals(values.get(i))) rezNi.add(k);}
                            else{ if(Float.parseFloat(k.getKey())==Float.parseFloat(values.get(i)))rezNi.add(k);}
                        } else if (relationsNI.get(i).equals("<")) {
                            if(type.equals("varchar")){
                                if (k.getKey().compareTo(values.get(i))<0) rezNi.add(k);}
                            else{ if(Float.parseFloat(k.getKey())<Float.parseFloat(values.get(i)))rezNi.add(k);}
                        } else if (relationsNI.get(i).equals(">")) {
                            if(type.equals("varchar")){
                                if (k.getKey().compareTo(values.get(i))>0) rezNi.add(k);}
                            else{ if(Float.parseFloat(k.getKey())>Float.parseFloat(values.get(i)))rezNi.add(k);}
                        } else if (relationsNI.get(i).equals(">=")) {
                            if(type.equals("varchar")){
                                if (k.getKey().compareTo(values.get(i))>=0) rezNi.add(k);}
                            else{ if(Float.parseFloat(k.getKey())>=Float.parseFloat(values.get(i)))rezNi.add(k);}
                        } else if (relationsNI.get(i).equals("<=")) {
                            if(type.equals("varchar")){
                                if (k.getKey().compareTo(values.get(i))<=0) rezNi.add(k);}
                            else{ if(Float.parseFloat(k.getKey())<=Float.parseFloat(values.get(i)))rezNi.add(k);}
                        }
                } else {
                    int ci = this.service.findNr(databaseName, tables.get(0), whereNI.get(i));
                    for (DTO k : datesNi) {
                        int count = 0;
                        String[] arrOfStr = k.getValue().split("#");
                        for (String s : arrOfStr) {
                            if (count == ci) {
                                if (relationsNI.get(i).equals("=")) {
                                    if(type.equals("varchar")){
                                        if (s.equals(valuesNI.get(i)) ) rezNi.add(k);}
                                    else{ if(Float.parseFloat(s)==Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                } else if (relationsNI.get(i).equals("<")) {
                                    if(type.equals("varchar")){
                                        if (s.compareTo(valuesNI.get(i)) < 0) rezNi.add(k);}
                                    else{ if(Float.parseFloat(s)<Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                } else if (relationsNI.get(i).equals(">")) {
                                    if(type.equals("varchar")){
                                        if (s.compareTo(valuesNI.get(i)) > 0) rezNi.add(k);}
                                    else{ if(Float.parseFloat(s)>Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                } else if (relationsNI.get(i).equals(">=")) {
                                    if(type.equals("varchar")){
                                        if (s.compareTo(valuesNI.get(i)) >= 0) rezNi.add(k);}
                                    else{ if(Float.parseFloat(s)>=Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                } else if (relationsNI.get(i).equals("<=")) {
                                    if(type.equals("varchar")){
                                        if (s.compareTo(valuesNI.get(i)) <= 0) rezNi.add(k);}
                                    else{ if(Float.parseFloat(s)<=Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                }
                            }
                            count++;


                        }
                    }
                }
                datesNi.removeAll(datesNi);
                for (DTO dto : rezNi) datesNi.add(dto);
                rez.removeAll(rez);
                for (DTO sto : datesNi) System.out.println(sto.getKey() + sto.getValue());


                i++;
            }
            return datesNi;}
            else if(whereI.size()!=0 && whereNI.size()==0){
                List<DTO> dateI=this.service.getDtoIndex(databaseName,tables.get(0),whereI.get(0));
                List<String> rez=new ArrayList<>();
                String type=this.service.findAttribute(databaseName,tables.get(0),whereI.get(0)).getType();
                for(DTO k: dateI)
                    if(relationsI.get(0).equals("=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().equals(valuesI.get(0))){
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }}
                        else{
                            if(Float.parseFloat(k.getKey())==Float.parseFloat(valuesI.get(0)))
                            {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }

                    }
                    else  if(relationsI.get(0).equals("<")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(0))<0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }}else if(Float.parseFloat(k.getKey())<Float.parseFloat(valuesI.get(0))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }

                    }
                    else  if(relationsI.get(0).equals(">")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(0))>0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>Float.parseFloat(valuesI.get(0))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }
                    }
                    else if(relationsI.get(0).equals(">=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(0))>=0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(0))){
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }
                        }}
                    else  if(relationsI.get(0).equals("<=")) {
                        if(type.equals("varchar")){
                            if(k.getKey().compareTo(valuesI.get(0))<=0) {
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(0))){String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {

                                rez.add(s);

                            }}}


                int i=1;
                while(i<whereI.size())
                {   dateI.removeAll(dateI);
                    dateI=this.service.getDtoIndex(databaseName,tables.get(0),whereI.get(i));
                    List<String> rez1=new ArrayList<>();
                    type=this.service.findAttribute(databaseName,tables.get(0),whereI.get(i)).getType();
                    for(DTO k: dateI)
                        if(relationsI.get(i).equals("=")) {
                            if(type.equals("varchar")){
                                if(k.getKey().equals(valuesI.get(i))){
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }}
                            else{
                                if(Float.parseFloat(k.getKey())==Float.parseFloat(valuesI.get(i)))
                                {
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }
                            }

                        }
                        else  if(relationsI.get(i).equals("<")) {
                            if(type.equals("varchar")){
                                if(k.getKey().compareTo(valuesI.get(i))<0) {
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }}else if(Float.parseFloat(k.getKey())<Float.parseFloat(valuesI.get(i))){
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }

                        }
                        else  if(relationsI.get(i).equals(">")) {
                            if(type.equals("varchar")){
                                if(k.getKey().compareTo(valuesI.get(i))>0) {
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }
                            }
                            else if(Float.parseFloat(k.getKey())>Float.parseFloat(valuesI.get(i))){
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }
                        }
                        else if(relationsI.get(i).equals(">=")) {
                            if(type.equals("varchar")){
                                if(k.getKey().compareTo(valuesI.get(i))>=0) {
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }
                            }
                            else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(i))){
                                String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }
                            }}
                        else  if(relationsI.get(i).equals("<=")) {
                            if(type.equals("varchar")){
                                if(k.getKey().compareTo(valuesI.get(i))<=0) {
                                    String[] arrOfStr = k.getValue().split("#");
                                    for (String s : arrOfStr) {

                                        rez.add(s);

                                    }
                                }
                            }
                            else if(Float.parseFloat(k.getKey())>=Float.parseFloat(valuesI.get(i))){String[] arrOfStr = k.getValue().split("#");
                                for (String s : arrOfStr) {

                                    rez.add(s);

                                }}}
                    List<String> intr=new ArrayList<>();
                    intr=intersect(rez,rez1);
                    rez.removeAll(rez);
                    for(String r:intr)rez.add(r);
                    intr.removeAll(intr);
                    i++;



                }
                dateI.removeAll(dateI);

                for(String key:rez) dateI.add(new DTO(key, this.service.getValueByKey1(databaseName,tables.get(0),key)));
                return dateI;
            }
            else if(whereNI.size()!=0 && whereI.size()==0)
            {
                List<DTO> datesNi=this.service.getDate(this.databaseName,tables.get(0));
                List<DTO> rezNi=new ArrayList<>();

                int i=0;
                while(i<whereNI.size()) {
                    System.out.println("rel " + relations.get(i));
                    Attribute a = this.service.findAttribute(databaseName, tables.get(0), whereNI.get(i));
                    String type=this.service.findAttribute(databaseName,tables.get(0),whereNI.get(i)).getType();
                    if (a.getPk()) {
                        for (DTO k : datesNi)
                            if (relationsNI.get(i).equals("=")) {
                                if(type.equals("varchar")){
                                    if (k.getKey().equals(values.get(i))) rezNi.add(k);}
                                else{ if(Float.parseFloat(k.getKey())==Float.parseFloat(values.get(i)))rezNi.add(k);}
                            } else if (relationsNI.get(i).equals("<")) {
                                if(type.equals("varchar")){
                                    if (k.getKey().compareTo(values.get(i))<0) rezNi.add(k);}
                                else{ if(Float.parseFloat(k.getKey())<Float.parseFloat(values.get(i)))rezNi.add(k);}
                            } else if (relationsNI.get(i).equals(">")) {
                                if(type.equals("varchar")){
                                    if (k.getKey().compareTo(values.get(i))>0) rezNi.add(k);}
                                else{ if(Float.parseFloat(k.getKey())>Float.parseFloat(values.get(i)))rezNi.add(k);}
                            } else if (relationsNI.get(i).equals(">=")) {
                                if(type.equals("varchar")){
                                    if (k.getKey().compareTo(values.get(i))>=0) rezNi.add(k);}
                                else{ if(Float.parseFloat(k.getKey())>=Float.parseFloat(values.get(i)))rezNi.add(k);}
                            } else if (relationsNI.get(i).equals("<=")) {
                                if(type.equals("varchar")){
                                    if (k.getKey().compareTo(values.get(i))<=0) rezNi.add(k);}
                                else{ if(Float.parseFloat(k.getKey())<=Float.parseFloat(values.get(i)))rezNi.add(k);}
                            }
                    } else {
                        int ci = this.service.findNr(databaseName, tables.get(0), whereNI.get(i));

                        for (DTO k : datesNi) {
                            int count = 0;
                            String[] arrOfStr = k.getValue().split("#");
                            for (String s : arrOfStr) {
                                if (count == ci) {
                                    if (relationsNI.get(i).equals("=")) {
                                        if(type.equals("varchar")){
                                            if (s.equals(valuesNI.get(i)) ) rezNi.add(k);}
                                        else{ if(Float.parseFloat(s)==Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                    } else if (relationsNI.get(i).equals("<")) {
                                        if(type.equals("varchar")){
                                            if (s.compareTo(valuesNI.get(i)) < 0) rezNi.add(k);}
                                        else{ if(Float.parseFloat(s)<Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                    } else if (relationsNI.get(i).equals(">")) {
                                        if(type.equals("varchar")){
                                            if (s.compareTo(valuesNI.get(i)) > 0) rezNi.add(k);}
                                        else{ if(Float.parseFloat(s)>Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                    } else if (relationsNI.get(i).equals(">=")) {
                                        if(type.equals("varchar")){
                                            if (s.compareTo(valuesNI.get(i)) >= 0) rezNi.add(k);}
                                        else{ if(Float.parseFloat(s)>=Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                    } else if (relationsNI.get(i).equals("<=")) {
                                        if(type.equals("varchar")){
                                            if (s.compareTo(valuesNI.get(i)) <= 0) rezNi.add(k);}
                                        else{ if(Float.parseFloat(s)<=Float.parseFloat(valuesNI.get(i)))rezNi.add(k);}
                                    }
                                }
                                count++;


                            }
                        }
                    }
                    datesNi.removeAll(datesNi);
                    for (DTO dto : rezNi) datesNi.add(dto);

                    for (DTO sto : datesNi) System.out.println(sto.getKey() + sto.getValue());


                    i++;
                }
                return datesNi;
            }
            return null;


    }
    public Boolean is_normal()
    {
        for(String s:selectAttrs)
            if(!s.equals("Normal")) return false;
        return true;
    }

    public Boolean is_group_by_and_select()
    {

        for(int g=0;g< gbAttributes.size();g++ )
        {   if(selectAttrs.get(g).equals("Normal")){int ok=0;
            for(String gb:attributes)
                if(gbAttributes.get(g).equals(gb))ok=1;
            if(ok==1)
                return true;
            else return false;}
            return true;
        }
        return true;
    }

    public Boolean having_and_select()
    {
        for(int i = 0; i < attributes.size(); i++)
        {
            if ( attributes.get(i).equals(hAttributes.get(0)) && selectAttrs.get(i).equals(agrRel.get(0)))
            {
                return true;
            }
        }
        return false;
    }
    @FXML
    public void QueryResult(ActionEvent ev) throws Exception {
        if(wheres.size()==0 && gbAttributes.size()==0 && hAttributes.size()==0)
        {
            //select si from
            List<String> sel=new ArrayList<>();
            List<DTO> fr=this.service.getDate(databaseName,tables.get(0));
            String c="";
            for(String s: this.attributes)
            {
                c=c+s+" ";}
            sel=Select(fr);
            resultColumn.setText(c);
            resultColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            tableResult.setItems(FXCollections.observableArrayList(sel));
        }
        else if(wheres.size()!=0 && gbAttributes.size()==0 && hAttributes.size()==0)
        {
            //select from where
            List<String> sel=new ArrayList<>();
            List<DTO> fr=Where();
            String c="";
            for(String s: this.attributes)
            {
                c=c+s+" ";}
            sel=Select(fr);
            resultColumn.setText(c);
            resultColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            tableResult.setItems(FXCollections.observableArrayList(sel));
        }
        else if ((gbAttributes.size() != 0 && is_group_by_and_select())||(gbAttributes.size() != 0 && is_group_by_and_select() && hAttributes.size()!=0 && having_and_select()))
        {//group by
            List<DTO> fr=new ArrayList<>();
            List<String> sel=new ArrayList<>();
            String c="";
            for(int si=0;si< this.attributes.size();si++)
            {
                if(selectAttrs.get(si).equals("Normal"))
                    c=c+attributes.get(si)+" ";

                else c=c+selectAttrs.get(si)+"("+attributes.get(si)+")"+" ";
            }
            if(wheres.size()!=0)
            {
                fr=Where();
            }
            else fr=this.service.getDate(databaseName,tables.get(0));

            HashMap<String,List<String>> rezGB=GroupBy(fr,gbAttributes.get(0));
            //if nu exista attribute de alt tip(doar normale) atunci adaugam cheia de cate valori avem ori
            int i=0;
            for (Map.Entry mapElement : rezGB.entrySet()) {
                int j=0;
                while(j<selectAttrs.size()){
                if(selectAttrs.get(j).equals("Normal")){
                    if(hAttributes.size()!=0){
                    if(hAttributes.get(0).equals(attributes.get(j)) && selectAttrs.get(j).equals(agrRel.get(0)))
                    {   String type=this.service.findAttribute(databaseName,tables.get(0),hAttributes.get(0)).getType();
                        if (hRelations.get(0).equals("=")) {
                            if(type.equals("varchar")){
                                if (mapElement.getKey().toString().equals(hValues.get(0))) if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                            else{ if(Float.parseFloat(mapElement.getKey().toString())==Float.parseFloat(hValues.get(0)))if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                        } else if (hRelations.get(0).equals("<")) {
                            if(type.equals("varchar")){
                                if (mapElement.getKey().toString().compareTo(hValues.get(0))<0)if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                            else{ if(Float.parseFloat(mapElement.getKey().toString())<Float.parseFloat(hValues.get(0)))if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                        } else if (hRelations.get(0).equals(">")) {
                            if(type.equals("varchar")){
                                if (mapElement.getKey().toString().compareTo(hValues.get(0))>0)if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                            else{ if(Float.parseFloat(mapElement.getKey().toString())>Float.parseFloat(hValues.get(0)))if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                        } else if (hRelations.get(0).equals(">=")) {
                            if(type.equals("varchar")){
                                if (mapElement.getKey().toString().compareTo(hValues.get(0))>=0)if(j>0)sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); else sel.add(mapElement.getKey().toString());}
                            else{ if(Float.parseFloat(mapElement.getKey().toString())>=Float.parseFloat(hValues.get(0)))if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                        } else if (hRelations.get(0).equals("<=")) {
                            if(type.equals("varchar")){
                                if (mapElement.getKey().toString().compareTo(hValues.get(0))<=0)if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                            else{ if(Float.parseFloat(mapElement.getKey().toString())<=Float.parseFloat(hValues.get(0)))if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else sel.add(mapElement.getKey().toString());}
                        }
                    }
                    else {if(attributes.get(j).equals(gbAttributes.get(0))){
                        if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else {
                            if(is_normal())
                            { List<String> aux= (List<String>) mapElement.getValue();
                                for(int kgh=0;kgh<aux.size();kgh++)
                                    sel.add(mapElement.getKey().toString());
                            }else sel.add(mapElement.getKey().toString());}}
                    else {
                        int ci=this.service.findNr(databaseName,tables.get(0),attributes.get(j));
                        List<DTO> rez=new ArrayList<>();
                        List<String> aux= (List<String>) mapElement.getValue();
                        for(String s:aux)rez.add(new DTO(s,this.service.getValueByKey1(databaseName,tables.get(0),s)));
                        for (DTO r : rez) {
                            int count = 0;

                            String[] arrOfStr = r.getValue().split("#");
                            for (String s2 : arrOfStr) {
                                if (count == ci) if(j>0){sel.set(i, sel.get(i) + " " + s2); i++;} else sel.add(s2);
                                count++;


                            }
                        }
                    }}}
                    else {if(attributes.get(j).equals(gbAttributes.get(0))){
                        if(j>0){sel.set(i, sel.get(i) + " " + mapElement.getKey().toString()); i++;} else {
                            if(is_normal())
                            { List<String> aux= (List<String>) mapElement.getValue();
                            for(int kgh=0;kgh<aux.size();kgh++)
                                sel.add(mapElement.getKey().toString());
                            }else sel.add(mapElement.getKey().toString());}}
                        else {
                        int ci=this.service.findNr(databaseName,tables.get(0),attributes.get(j));
                        List<DTO> rez=new ArrayList<>();
                        List<String> aux= (List<String>) mapElement.getValue();
                        for(String s:aux)rez.add(new DTO(s,this.service.getValueByKey1(databaseName,tables.get(0),s)));
                        for (DTO r : rez) {
                            int count = 0;

                            String[] arrOfStr = r.getValue().split("#");
                            for (String s2 : arrOfStr) {
                                if (count == ci) if(j>0){sel.set(i, sel.get(i) + " " + s2); i++;} else sel.add(s2);
                                count++;


                            }
                        }
                    }}

                System.out.println("kh--"+mapElement.getKey());}
                else{
                List<String> aux= (List<String>) mapElement.getValue();

                List<DTO> rez=new ArrayList<>();
                for(String s:aux)rez.add(new DTO(s,this.service.getValueByKey1(databaseName,tables.get(0),s)));
                int ci=this.service.findNr(databaseName,tables.get(0),attributes.get(j));
                    if(hAttributes.size()!=0) {
                        if (hAttributes.get(0).equals(attributes.get(j))) {
                            int ok = 0;
                            if (selectAttrs.get(j).equals("COUNT")) {
                                if (hRelations.get(0).equals("=")) {
                                    if (rez.size() == Float.parseFloat(hValues.get(0))) {
                                        sel.set(i, sel.get(i) + " " + rez.size());
                                        if(j>0){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<")) {
                                    if (rez.size() < Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">")) {
                                    if (rez.size() > Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">=")) {
                                    if (rez.size() >= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<=")) {
                                    if (rez.size() <= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                                        ok = 1;
                                    }
                                }
                                if (ok == 0) {
                                    sel.remove(i);
                                    i--;
                                }
                            } else if (selectAttrs.get(j).equals("SUM")) {
                                float sum = 0;
                                for (DTO r : rez) {
                                    int count = 0;

                                    String[] arrOfStr = r.getValue().split("#");
                                    for (String s2 : arrOfStr) {
                                        if (count == ci) {
                                            sum += Float.parseFloat(s2);
                                        }
                                        count++;


                                    }
                                }
                                if (hRelations.get(0).equals("=")) {
                                    if (sum == Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<")) {
                                    if (sum < Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">")) {
                                    if (sum > Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">=")) {
                                    if (sum >= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<=")) {
                                    if (sum <= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                }
                                if (ok == 0) {
                                    sel.remove(i);
                                    i--;
                                }

                            } else if (selectAttrs.get(j).equals("AVG")) {
                                float sum = 0;
                                for (DTO r : rez) {
                                    int count = 0;

                                    String[] arrOfStr = r.getValue().split("#");
                                    for (String s2 : arrOfStr) {
                                        if (count == ci) {
                                            sum += Float.parseFloat(s2);
                                        }
                                        count++;


                                    }
                                }
                                if (hRelations.get(0).equals("=")) {
                                    if (sum / aux.size() == Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum/aux.size()); } else sel.add(String.valueOf(sum));

                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<")) {
                                    if (sum / aux.size() < Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum/aux.size()); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">")) {
                                    if (sum / aux.size() > Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum/aux.size()); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">=")) {
                                    if (sum / aux.size() >= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum/aux.size()); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<=")) {
                                    if (sum / aux.size() <= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + sum/aux.size()); } else sel.add(String.valueOf(sum));
                                        ok = 1;
                                    }
                                }
                                if (ok == 0) {
                                    sel.remove(i);
                                    i--;
                                }
                                sel.set(i, sel.get(i) + " " + sum / aux.size());
                            } else if (selectAttrs.get(j).equals("MIN")) {

                                int count = 0;
                                float min = 0;

                                String[] arrOfStr = rez.get(0).getValue().split("#");
                                for (String s2 : arrOfStr) {
                                    if (count == ci) {
                                        min = Float.parseFloat(s2);
                                    }
                                    count++;
                                }
                                for (int ri = 1; ri < rez.size(); ri++) {
                                    count = 0;

                                    arrOfStr = rez.get(ri).getValue().split("#");
                                    for (String s3 : arrOfStr) {
                                        if (count == ci) {
                                            if (Float.parseFloat(s3) < min)

                                                min = Float.parseFloat(s3);
                                        }
                                        count++;


                                    }
                                }
                                if (hRelations.get(0).equals("=")) {
                                    if (min == Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<")) {
                                    if (min < Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">")) {
                                    if (min > Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">=")) {
                                    if (min >= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<=")) {
                                    if (min <= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                                        ok = 1;
                                    }
                                }
                                if (ok == 0) {
                                    sel.remove(i);
                                    i--;
                                }


                            } else if (selectAttrs.get(j).equals("MAX")) {

                                int count = 0;
                                float max = 0;

                                String[] arrOfStr = rez.get(0).getValue().split("#");
                                for (String s2 : arrOfStr) {
                                    if (count == ci) {
                                        max = Float.parseFloat(s2);
                                    }
                                    count++;
                                }
                                for (int ri = 1; ri < rez.size(); ri++) {
                                    count = 0;

                                    arrOfStr = rez.get(ri).getValue().split("#");
                                    for (String s3 : arrOfStr) {
                                        if (count == ci) {
                                            if (Float.parseFloat(s3) > max)

                                                max = Float.parseFloat(s3);
                                        }
                                        count++;


                                    }
                                }
                                if (hRelations.get(0).equals("=")) {
                                    if (max == Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<")) {
                                    if (max < Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">")) {
                                    if (max > Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals(">=")) {
                                    if (max >= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                                        ok = 1;
                                    }
                                } else if (hRelations.get(0).equals("<=")) {
                                    if (max <= Float.parseFloat(hValues.get(0))) {
                                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                                        ok = 1;
                                    }
                                }
                                if (ok == 0) {
                                    sel.remove(i);
                                    i--;
                                }

                            }
                        }
                    }
                else {
                    if (selectAttrs.get(j).equals("COUNT")) {
                        if(i<sel.size()){sel.set(i, sel.get(i) + " " + rez.size()); } else sel.add(String.valueOf(rez.size()));
                    } else if (selectAttrs.get(j).equals("SUM")) {
                        float sum = 0;
                        for (DTO r : rez) {
                            int count = 0;

                            String[] arrOfStr = r.getValue().split("#");
                            for (String s2 : arrOfStr) {
                                if (count == ci) {
                                    sum += Float.parseFloat(s2);
                                }
                                count++;


                            }
                        }
                        if(i<sel.size()){sel.set(i, sel.get(i) + " " + sum); } else sel.add(String.valueOf(sum));
                    } else if (selectAttrs.get(j).equals("AVG")) {
                        float sum = 0;
                        for (DTO r : rez) {
                            int count = 0;

                            String[] arrOfStr = r.getValue().split("#");
                            for (String s2 : arrOfStr) {
                                if (count == ci) {
                                    sum += Float.parseFloat(s2);
                                }
                                count++;


                            }
                        }
                        if(j>0){sel.set(i, sel.get(i) + " " + sum/rez.size()); } else sel.add(String.valueOf(sum/rez.size()));
                    } else if (selectAttrs.get(j).equals("MIN")) {

                        int count = 0;
                        float min = 0;

                        String[] arrOfStr = rez.get(0).getValue().split("#");
                        for (String s2 : arrOfStr) {
                            if (count == ci) {
                                min = Float.parseFloat(s2);
                            }
                            count++;
                        }
                        for (int ri = 1; ri < rez.size(); ri++) {
                            count = 0;

                            arrOfStr = rez.get(ri).getValue().split("#");
                            for (String s3 : arrOfStr) {
                                if (count == ci) {
                                    if (Float.parseFloat(s3) < min)

                                        min = Float.parseFloat(s3);
                                }
                                count++;


                            }
                        }
                        if(j>0){sel.set(i, sel.get(i) + " " + min); } else sel.add(String.valueOf(min));
                    } else if (selectAttrs.get(j).equals("MAX")) {

                        int count = 0;
                        float max = 0;

                        String[] arrOfStr = rez.get(0).getValue().split("#");
                        for (String s2 : arrOfStr) {
                            if (count == ci) {
                                max = Float.parseFloat(s2);
                            }
                            count++;
                        }
                        for (int ri = 1; ri < rez.size(); ri++) {
                            count = 0;

                            arrOfStr = rez.get(ri).getValue().split("#");
                            for (String s3 : arrOfStr) {
                                if (count == ci) {
                                    if (Float.parseFloat(s3) > max)

                                        max = Float.parseFloat(s3);
                                }
                                count++;


                            }
                        }
                        if(j>0){sel.set(i, sel.get(i) + " " + max); } else sel.add(String.valueOf(max));
                    }
                }
                i++;

            }j++;}}



            //sel=Select(rez);
            resultColumn.setText(c);
            resultColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            tableResult.setItems(FXCollections.observableArrayList(sel));
        }


    }



}
