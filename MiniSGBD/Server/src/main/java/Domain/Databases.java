package Domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "Databases")
public class Databases implements Serializable {
    private List<Database> dbList;

    public Databases() {
        this.dbList = new ArrayList<>();
    }

    public List<Database> getDbList() {
        return dbList;
    }

    @XmlElement(name = "Database")
    public void setDbList(List<Database> dbList) {
        this.dbList = dbList;
    }

    @Override
    public String toString() {
        return "Databases{" +
                "db=" + dbList ;
    }
}
