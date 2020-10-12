package Server;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;
    private List<Attribute> attributes;
    private List<IndexFile> indexFiles;

    public Table(String name, List<Attribute> attributes, List<IndexFile> indexFiles) {
        this.name = name;
        this.attributes = attributes != null ? attributes : new ArrayList<>();
        this.indexFiles = indexFiles != null ? indexFiles : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttrToTable(Attribute attr) {
        attributes.add(attr);
    }

    public List<String> getUniqueKeys() {
        List<String> uniqueList = new ArrayList<String>();
        for (Attribute a : attributes) {
            if (a.isUnique()) {
                uniqueList.add(a.getAttrName());
            }
        }
        return uniqueList;
    }

    public List<String> getPrimaryKeys() {
        List<String> pkList = new ArrayList<String>();
        for (Attribute a : attributes) {
            if (a.isPrimaryKey()) {
                pkList.add(a.getAttrName());
            }
        }
        return pkList;
    }

    public List<ForeignKey> getForeignKeys() {
        List<ForeignKey> fkList = new ArrayList<ForeignKey>();
        for (Attribute a : attributes) {
            if (StringUtils.isNotEmpty(a.getReferencedAttribute())) {
                fkList.add(new ForeignKey(a.getAttrName(), a.getReferencedTable(), a.getReferencedAttribute()));
            }
        }
        return fkList;
    }

    public void deleteAttrToTable(String attrName) {
        for (Attribute attr : attributes) {
            if (attr.getAttrName().equals(attrName)) {
                attributes.remove(attr);
            }
        }
    }

    public List<IndexFile> getIndexFiles() {
        return indexFiles;
    }

    public void setIndexFiles(List<IndexFile> indexFiles) {
        this.indexFiles = indexFiles;
    }

    public void addIndexFile(IndexFile indexFile) {
        indexFiles.add(indexFile);
    }


    @Override
    public String toString() {
        return "Table{" + ", name=" + name + ", attributes=" + attributes + ", indexFiles=" + indexFiles + '}';
    }
}
