package kr.ac.ssu.ikatool.obj;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-12-30.
 */
public class Rule {
    int ruleID;
    ArrayList<ValuedAtom> anttArr;
    ArrayList<ValuedAtom> conArr;

    public Rule(int ruleID, ArrayList<ValuedAtom> anttArr, ArrayList<ValuedAtom> conArr) {
        this.ruleID = ruleID;
        this.anttArr = anttArr;
        this.conArr = conArr;
    }

    public Rule(int ruleID, ArrayList<ValuedAtom> anttArr) {
        this.ruleID = ruleID;
        this.anttArr = anttArr;
    }

    public Boolean isSameRule(ArrayList<Integer> ants, ArrayList<Integer> cons) {
        Boolean[] antsCheck = new Boolean[ants.size()];
        for (int i=0; i<antsCheck.length; i++) {
            antsCheck[i] = false;
        }

        Boolean[] consCheck = new Boolean[cons.size()];
        for (int i=0; i<consCheck.length; i++) {
            consCheck[i] = false;
        }

        for (int i=0; i<anttArr.size(); i++) {
            ValuedAtom ruleAntAtom = anttArr.get(i);
            for (int j=0; j<ants.size(); j++) {
                if (ants.get(j) == ruleAntAtom.getId()) {
                    antsCheck[j] = true;
                    break;
                }
            }
        }

        for (int i=0; i<conArr.size(); i++) {
            ValuedAtom ruleConAtom = conArr.get(i);
            for (int j=0; j<cons.size(); j++) {
                if (cons.get(j) == ruleConAtom.getId()) {
                    consCheck[j] = true;
                    break;
                }
            }
        }

        return areAllTrue(antsCheck) && areAllTrue(consCheck);

    }

    public Boolean areAllTrue(Boolean[] array) {
        for (boolean b : array) if (!b) return false;
        return true;
    }

    /*
    Getter & Setter
     */

    public int getRuleID() {
        return ruleID;
    }

    public void setRuleID(int ruleID) {
        this.ruleID = ruleID;
    }

    public ArrayList<ValuedAtom> getAnttArr() {
        return anttArr;
    }

    public void setAnttArr(ArrayList<ValuedAtom> anttArr) {
        this.anttArr = anttArr;
    }

    public ArrayList<ValuedAtom> getConArr() {
        return conArr;
    }

    public void setConArr(ArrayList<ValuedAtom> conArr) {
        this.conArr = conArr;
    }
}
