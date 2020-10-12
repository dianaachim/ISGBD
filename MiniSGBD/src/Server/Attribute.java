package Server;

public class Attribute {

    private String attrName;
    private String attrType;
    private boolean isPrimaryKey;
    private boolean isUnique;
    private boolean isNull;
    private String referencedTable;
    private String referencedAttribute;

    public Attribute(String attrName, String attrType, boolean isPrimaryKey, boolean isUnique, boolean isNull,
                     String referencedTable, String referencedAttribute) {
        this.attrName = attrName;
        this.attrType = attrType;
        this.isPrimaryKey = isPrimaryKey;
        this.isUnique = isUnique;
        this.isNull = isNull;
        this.referencedTable = referencedTable;
        this.referencedAttribute = referencedAttribute;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
 /*
    public boolean isForeignKey() {
        return StringUtils.isNotEmpty(referencedAttribute);
    }*/

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(String referencedTable) {
        this.referencedTable = referencedTable;
    }

   public String getReferencedAttribute() {
        return referencedAttribute;
    }

    public void setReferencedAttribute(String referencedAttribute) {
        this.referencedAttribute = referencedAttribute;
    }


    @Override
    public String toString() {
        return "Attribute{" + "attrName='" + attrName + '\'' + ", attrType='" + attrType + '\'' + ", isPrimaryKey="
                + isPrimaryKey + ", isUnique=" + isUnique + ", isNull=" + isNull + ", referencedTable='"
                + referencedTable + '\'' + ", referencedAttribute='" + referencedAttribute + '\'' + '}';
    }
}
