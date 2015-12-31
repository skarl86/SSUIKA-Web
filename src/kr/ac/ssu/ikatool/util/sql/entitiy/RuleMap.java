package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by NCri on 2015. 12. 31..
 */
public class RuleMap {
    private Integer ruleID;
    private ArrayList<Atom> atomList;

    public Integer getRuleID() {
        return ruleID;
    }

    public void setRuleID(Integer ruleID) {
        this.ruleID = ruleID;
    }

    public ArrayList<Atom> getAtomList() {
        return atomList;
    }

    public void setAtomList(ArrayList<Atom> atomList) {
        this.atomList = atomList;
    }
}
