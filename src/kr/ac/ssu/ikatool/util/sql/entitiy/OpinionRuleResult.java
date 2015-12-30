package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NCri on 2015. 12. 30..
 */
public class OpinionRuleResult {
    private Map<Integer, Rule> rules;
    private ArrayList<Atom> highlight;

    public OpinionRuleResult() {
        this.rules = new HashMap<Integer, Rule>();
    }

    public void addRule(Rule rule){
        rules.put(rule.getId(), rule);
    }
    public Rule getRuleByID(Integer ruleID){
        return rules.get(ruleID);
    }
    public ArrayList<Rule> getRules(){
        ArrayList<Rule> ruleList = new ArrayList<Rule>();
        for(Integer key : rules.keySet()){
            ruleList.add(rules.get(key));
        }
        return ruleList;
    }
}
