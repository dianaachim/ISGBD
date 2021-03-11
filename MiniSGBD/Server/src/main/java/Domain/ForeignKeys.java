package Domain;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ForeignKeys implements Serializable {
    private List<ForeignKey> foreignKeyList;

    public ForeignKeys() {
        this.foreignKeyList = new ArrayList<>();
    }

    public List<ForeignKey> getForeignKeyList() {
        return foreignKeyList;
    }

    @XmlElement(name = "ForeignKey")
    public void setForeignKeyList(List<ForeignKey> foreignKeyList) {
        this.foreignKeyList = foreignKeyList;
    }

    @Override
    public String toString() {
        return "Foreign keys{" +
                "fks=" + this.foreignKeyList.toString() ;
    }
}
