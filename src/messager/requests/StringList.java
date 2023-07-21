package messager.requests;

import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@XmlRootElement
public class StringList {

    @XmlElement(name = "el")
    private final List<String> list = new ArrayList<>();

    public void put(int value) {
        put(String.valueOf(value));
    }

    public void put(long value) {
        put(String.valueOf(value));
    }

    public void put(double value) {
        put(String.valueOf(value));
    }

    public void put(boolean value) {
        put(String.valueOf(value));
    }

    public void put(String value) {
        list.add(value);
    }

    public String getString(int i) {
        return list.get(i);
    }

    public int getInt(int i) {
        return Integer.parseInt(list.get(i));
    }

    public double getLong(int i) {
        return Long.parseLong(list.get(i));
    }

    public double getDouble(int i) {
        return Double.parseDouble(list.get(i));
    }

    public boolean getBoolean(int i) {
        return Boolean.parseBoolean(list.get(i));
    }

    public int size() {
        return list.size();
    }

}
