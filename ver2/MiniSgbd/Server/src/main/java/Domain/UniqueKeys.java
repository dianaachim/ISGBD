package Domain;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UniqueKeys implements Serializable {
    private List<String> uniqueKeys;

    public UniqueKeys() {
        this.uniqueKeys = new ArrayList<>();
    }

    public List<String> getUniqueKeys() {
        return uniqueKeys;
    }

    @XmlElement(name="UniqueAttribute")
    public void setUniqueKeys(List<String> uniqueKeys) {
        this.uniqueKeys = uniqueKeys;
    }

    @Override
    public String toString() {
        return "Unique keys{" +
                "uks=" + this.uniqueKeys ;
    }
}
