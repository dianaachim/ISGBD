package Domain;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrimaryKeys implements Serializable {
    private List<String> primaryKeys;

    public PrimaryKeys() {
        this.primaryKeys = new ArrayList<>();
    }

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    @XmlElement(name="PkAttribute")
    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    @Override
    public String toString() {
        return "Primary keys{" +
                "pks=" + this.primaryKeys ;
    }
}
