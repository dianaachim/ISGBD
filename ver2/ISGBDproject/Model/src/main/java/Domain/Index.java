package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

public class Index implements Serializable {
    private String name;
    private String type;
    private String file;

    public Index(String name, String type,String file) {
        this.name = name;
        this.type = type;
        this.file=file;
    }
    public Index(){}

    @XmlAttribute(name="attributeName")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name="type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute(name="file")
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return name +  type+file;
    }
}
