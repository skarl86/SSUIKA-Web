package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by NCri on 2015. 12. 30..
 */
public class RuleResult {
    private Map<Integer, Rule> rules;
    private ArrayList<Atom> highlight;

    public RuleResult() {
        this.rules = new HashMap<Integer, Rule>();
    }

    public void addRule(Rule rule){
        rules.put(rule.getId(), rule);
    }
    public Rule getRuleByID(Integer ruleID){
        return rules.get(ruleID);
    }
    public Set<Integer> getRuleIDs(){
        return rules.keySet();
    }
    public ArrayList<Rule> getRules(){
        ArrayList<Rule> ruleList = new ArrayList<Rule>();
        for(Integer key : rules.keySet()){
            ruleList.add(rules.get(key));
        }
        return ruleList;
    }
}
