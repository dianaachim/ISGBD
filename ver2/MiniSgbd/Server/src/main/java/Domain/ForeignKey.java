package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class ForeignKey implements Serializable {
    private String fkAttribute;
    private String refAttribute;
    private String refTable;
    private String fkfile;

    public ForeignKey(String fkAttribute, String refAttribute, String refTable, String fkfile) {
        this.fkAttribute = fkAttribute;
        this.refAttribute = refAttribute;
        this.refTable = refTable;
        this.fkfile = fkfile;
    }

    public ForeignKey() {}

    public String getFkAttribute() {
        return fkAttribute;
    }

    @XmlElement(name="FkAttribute")
    public void setFkAttribute(String fkAttribute) {
        this.fkAttribute = fkAttribute;
    }

    public String getRefAttribute() {
        return refAttribute;
    }

    @XmlElement(name = "RefAttribute")
    public void setRefAttribute(String refAttribute) {
        this.refAttribute = refAttribute;
    }

    public String getRefTable() {
        return refTable;
    }

    @XmlElement(name="RefTable")
    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getFkfile() {
        return fkfile;
    }

    @XmlAttribute(name="FkFile")
    public void setFkfile(String fkfile) {
        this.fkfile = fkfile;
    }

    @Override
    public String toString() {
        return "FK( " + this.fkAttribute + " ref ( " + this.refAttribute + ", " + this.refTable + " ))";
    }
}
