package Domain;

import javafx.scene.control.Button;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabasesNames implements Serializable {

    private String databaseName;
    private List<TablesNames> tablesNamesList;


    public DatabasesNames(String databaseName) {
        this.databaseName = databaseName;
        this.tablesNamesList=new ArrayList<>();

    }

    public DatabasesNames() {
    }

    @XmlAttribute(name="databaseName")
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String toString() {
        return databaseName ;
    }

    @XmlElement(name="Table")
    public List<TablesNames> getTablesNamesList() {
        return tablesNamesList;
    }

    public void setTablesNamesList(List<TablesNames> tablesNamesList) {
        this.tablesNamesList = tablesNamesList;
    }
}
