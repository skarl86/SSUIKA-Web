package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 31..
 */
public class Value {
    private Integer id;
    private String name;

    public Value(Integer id) {
        this.id = id;
    }

    public Value(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
