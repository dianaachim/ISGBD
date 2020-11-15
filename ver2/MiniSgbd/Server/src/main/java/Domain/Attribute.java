package Domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

public class Attribute implements Serializable {
    private String name;
    private String type;
    private Integer length;
    private Boolean notNull;
    private Boolean pk;
    private Boolean fk;
    private Boolean uk;
    private String reference;

    public Attribute(String name, String type, Integer length, Boolean notNull, Boolean pk, Boolean fk, Boolean uk, String reference) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.notNull = notNull;
        this.pk = pk;
        this.fk = fk;
        this.uk = uk;
        this.reference = reference;
    }

    public Attribute(){
        this.name = "";
        this.type = "";
        this.length = 0;
        this.notNull = false;
        this.pk = false;
        this.fk = false;
        this.uk = false;
        this.reference = "";
    }

    @XmlAttribute(name="name")
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

    @XmlAttribute(name="length")
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @XmlTransient
    public Boolean getPk() {
        return pk;
    }

    public void setPk(Boolean pk) {
        this.pk = pk;
    }

    @XmlTransient
    public Boolean getFk() {
        return fk;
    }

    public void setFk(Boolean fk) {
        this.fk = fk;
    }

    @XmlTransient
    public Boolean getUk() {
        return uk;
    }

    public void setUk(Boolean uk) {
        this.uk = uk;
    }

    @XmlTransient
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.type;
//        return "attribute name = " + name + "; " + " type= " + type + "; pk= " + pk  + "; uk= " + uk + "; fk= " + fk + "; ref=" + reference ;
    }

    @XmlAttribute(name="notNull")
    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }
}
