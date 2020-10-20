package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {
    private String tableName;
    private String databaseName;
    private List<Attribute> attributeList;
    private List<Index> indexList;


    public Table(String tableName, String databaseName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.attributeList=new ArrayList<>();
        this.indexList=new ArrayList<>();
    }

    public Table() {}

    public String getTableName() {
        return tableName;
    }

    @XmlAttribute(name="TableName")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    @XmlElement(name="Attribute")
    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    public List<Index> getIndexList() {
        return indexList;
    }

    @XmlElement(name="Index")
    public void setIndexList(List<Index> indexList) {
        this.indexList = indexList;
    }

    @Override
    public String toString() {
        return tableName + "(" + attributeList.toString() + ")";
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @XmlTransient
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
