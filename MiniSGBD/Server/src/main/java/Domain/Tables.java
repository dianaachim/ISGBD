package Domain;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tables implements Serializable {
    private List<Table> tableList;

    public Tables() {
        this.tableList = new ArrayList<>();
    }

    public Tables (List<Table> tables) {
        this.tableList = tables;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    @XmlElement(name = "Table")
    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    @Override
    public String toString() {
        return "Tables{" +
                "tables=" + this.tableList ;
    }
}
