package kr.ac.ssu.ikatool.obj;

/**
 * Created by Administrator on 2015-12-31.
 */
public class KSAtom {
    int id = -1;
    String name;

    public KSAtom( String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
