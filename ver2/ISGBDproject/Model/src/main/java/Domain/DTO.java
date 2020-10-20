package Domain;

import java.io.Serializable;

public class DTO implements Serializable {
    private String key;
    private String value;

    public DTO(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public DTO() {}


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DTO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
