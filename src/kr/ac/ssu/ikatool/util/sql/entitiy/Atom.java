package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 29..
 */
public class Atom {
    private Integer id;
    private String name;
    private Integer type;
    private String value;
    private Value _value;

    public Atom(Integer id, String name, Integer type ,Value _value) {
        this.id = id;
        this.name = name;
        this._value = _value;
        this.type = type;
    }

    public Atom(Integer id, Value value) {
        this.id = id;
        this._value = value;
    }

    public Atom(Integer id, String name, Integer type) {
        this(id, name, type, "");
    }

    public void setValue(Value _value) {
        this._value = _value;
    }

    public Atom(Integer id, String name, Integer type, String value) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Atom(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public Integer getValueID() { return _value.getId(); }
    public String getValueStr() { return _value.getName(); }

    public boolean isEqaul(Atom atom){
        return atom.getId().equals(this.getId()) && atom.getValueStr().equals(this.getValueStr());
    }
}
