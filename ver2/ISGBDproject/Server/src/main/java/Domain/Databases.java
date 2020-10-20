package Domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="Databases")
public class Databases implements Serializable {
    private List<DatabasesNames> db;

    public Databases() {
        this.db = new ArrayList<>();
    }

    @XmlElement(name="Database")
    public List<DatabasesNames> getDb() {
        return db;
    }

    public void setDb(List<DatabasesNames> db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "Databases{" +
                "db=" + db ;
    }
}
