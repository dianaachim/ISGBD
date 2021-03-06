package Domain;

import org.w3c.dom.Attr;

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
    private PrimaryKeys pks;
    private UniqueKeys uks;
    private ForeignKeys fks;

    public Table(String tableName, String databaseName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.attributeList=new ArrayList<>();
        this.indexList=new ArrayList<>();
        this.pks = new PrimaryKeys();
        this.uks = new UniqueKeys();
        this.fks = new ForeignKeys();
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
        StringBuilder attributes = new StringBuilder();
        for(Attribute attrs: this.attributeList) {
            attributes.append(attrs.toString()).append(" | ");
        }
        return tableName + "[" + attributes + " ] " + "\n";
    }

    public String getDatabaseName() {
        return databaseName;
    }

    @XmlTransient
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public PrimaryKeys getPks() {
        return pks;
    }

    @XmlElement(name="PrimaryKeys")
    public void setPks(PrimaryKeys pks) {
        this.pks = pks;
    }

    public UniqueKeys getUks() {
        return uks;
    }

    @XmlElement(name="UniqueKeys")
    public void setUks(UniqueKeys uks) {
        this.uks = uks;
    }

    public ForeignKeys getFks() {
        return fks;
    }

    @XmlElement(name="ForeignKeys")
    public void setFks(ForeignKeys fks) {
        this.fks = fks;
    }
}
