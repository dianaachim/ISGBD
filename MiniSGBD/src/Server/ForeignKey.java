package Server;

public class ForeignKey {

    String foreignKeyAttribute;
    String referencedTable;
    String referencedAttribute;

    public ForeignKey(String foreignKeyAttribute, String referencedTable, String referencedAttribute) {
        this.foreignKeyAttribute = foreignKeyAttribute;
        this.referencedTable = referencedTable;
        this.referencedAttribute = referencedAttribute;
    }

    public ForeignKey(String referencedTable, String referencedAttribute) {
        this.referencedTable = referencedTable;
        this.referencedAttribute = referencedAttribute;
    }

    public String getForeignKeyAttribute() {
        return foreignKeyAttribute;
    }

    public void setForeignKeyAttribute(String foreignKeyAttribute) {
        this.foreignKeyAttribute = foreignKeyAttribute;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(String refTable) {
        this.referencedTable = refTable;
    }

    public String getReferencedAttribute() {
        return referencedAttribute;
    }

    public void setReferencedAttribute(String referencedAttribute) {
        this.referencedAttribute = referencedAttribute;
    }

    @Override
    public String toString() {
        return "ForeignKey{" +
                "foreignKeyAttribute='" + foreignKeyAttribute + '\'' +
                ", referencedTable='" + referencedTable + '\'' +
                ", referencedAttribute='" + referencedAttribute + '\'' +
                '}';
    }
}
