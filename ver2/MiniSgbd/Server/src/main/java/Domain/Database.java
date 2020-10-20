package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Database implements Serializable {
    private String databaseName;
    private List<Table> tablesList;

    public Database(String databaseName) {
        this.databaseName = databaseName;
        this.tablesList = new ArrayList<>();
    }

    public Database() {}

    public String getDatabaseName() {
        return databaseName;
    }

    @XmlAttribute(name="databaseName")
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<Table> getTablesList() {
        return tablesList;
    }

    @XmlElement(name="Table")
    public void setTablesList(List<Table> tablesList) {
        this.tablesList = tablesList;
    }

    @Override
    public String toString() {
        return databaseName ;
    }
}
