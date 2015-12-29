package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 29..
 */
public class Atom {
    public Integer id;
    public String name;
    public Integer type;

    public Atom(Integer id, String name, Integer type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
