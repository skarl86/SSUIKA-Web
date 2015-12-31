package kr.ac.ssu.ikatool.obj;

/**
 * Created by Administrator on 2015-12-31.
 */
public class ValuedAtom {
    int id;
    KSAtom atom;
    Value value;

    public ValuedAtom(KSAtom atom, Value value) {
        this.id = -1;
        this.atom = atom;
        this.value = value;
    }

    public ValuedAtom(int id) {
        this.id = id;
        this.atom = null;
        this.value = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KSAtom getAtom() {
        return atom;
    }

    public void setAtom(KSAtom atom) {
        this.atom = atom;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }


}
