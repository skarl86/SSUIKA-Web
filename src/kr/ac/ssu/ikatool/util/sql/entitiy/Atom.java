package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 29..
 */
public class Atom {
    public Integer id;
    public String name;
    public Integer type;
    public String value;

    public Atom(Integer id, String name, Integer type) {
        this(id, name, type, null);
    }

    public Atom(Integer id, String name, Integer type, String value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() { return value; }
}
