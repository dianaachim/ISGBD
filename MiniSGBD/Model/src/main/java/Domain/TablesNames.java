package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TablesNames implements Serializable
{

        private String tableName;
        private List<Attribute> attributeList;
        private List<Index> indexList;
        private String file;
    public TablesNames(String tableName,String file) {
        this.tableName = tableName;
        this.file=file;
        this.attributeList=new ArrayList<>();
        this.indexList=new ArrayList<>();
    }
public TablesNames(){}
    @XmlAttribute(name="TableName")
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return tableName;
    }

    @XmlElement(name="Attribute")
    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }
    @XmlElement(name="Index")
    public List<Index> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<Index> indexList) {
        this.indexList = indexList;
    }
    @XmlAttribute(name="File")
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
