package Server;

import java.util.ArrayList;
import java.util.List;

public class IndexFile {

    private String indexType;
    private Boolean isUnique;
    private String indexName;
    private List<String> attributes;

    public IndexFile(String indexType, Boolean isUnique, String indexName, List<String> attributes) {
        this.indexType = indexType;
        this.isUnique = isUnique;
        this.indexName = indexName;
        this.attributes = attributes != null ? attributes : new ArrayList<>();
    }

    public IndexFile(String indexName) {

        this.indexName = indexName;
    }
    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public Boolean getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void addAttrNameToIndex(String attrName) {
        attributes.add(attrName);
    }

    public void deleteAttrNameFromIndex(String attrName) {
        attributes.remove(attrName);
    }

    @Override
    public String toString() {
        return "IndexFile{" + "indexType=" + indexType + ", isUnique=" + isUnique + ", indexName=" + indexName + ", attributes=" + attributes + '}';
    }

}
