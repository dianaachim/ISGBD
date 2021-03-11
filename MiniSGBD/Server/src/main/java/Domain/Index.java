package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

public class Index implements Serializable {
    private String name;
    private String tableName;
//    private String columnName;
    private List<String> columns;
    private String databaseName;
    private Boolean unique;

    public Index(String name, String tableName, List<String> columnName, String databaseName, Boolean unique) {
        this.name = name;
        this.tableName = tableName;
        this.columns = columnName;
//        this.columnName = columnName;
        this.databaseName = databaseName;
        this.unique = unique;
    }

    public Index() {
        this.unique=false;
    }


    public String getName() {
        return name;
    }

    @XmlAttribute(name="name")
    public void setName(String name) {
        this.name = name;
    }

    public String getTableName() {
        return tableName;
    }

    @XmlTransient
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

//    public String getColumnName() {
//        return columnName;
//    }
//
//    @XmlAttribute(name="column")
//    public void setColumnName(String columnName) {
//        this.columnName = columnName;
//    }

    public String getDatabaseName() {
        return databaseName;
    }

    @XmlTransient
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Boolean getUnique() {
        return unique;
    }

    @XmlAttribute(name="isUnique")
    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public List<String> getColumns() {
        return columns;
    }

    @XmlElement(name="IndexAttribute")
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
