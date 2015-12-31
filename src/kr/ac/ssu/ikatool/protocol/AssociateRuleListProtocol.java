package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.SQLManager;
import kr.ac.ssu.ikatool.util.sql.entitiy.Rule;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by NCri on 2015. 12. 31..
 */
public class AssociateRuleListProtocol extends IKAProtocol {
    public static JSONObject getAssoicateRule(String ants, String cons){
        JSONObject jsonObject = getDefaultJSON("associateRule", "SUCCESS");

        String delimiter = "!@";
        ArrayList<String> antList = generateList(ants.split(delimiter));
        ArrayList<String> conList = generateList(cons.split(delimiter));

        Set<Rule> ruleSet = SQLManager.getAssociateRule(antList, conList);

        JSONObject rule = null;
        JSONArray ruleList = new JSONArray();
        for(Rule r : ruleSet){
            rule = new JSONObject();
            rule.put("ruleID", r.getId());
            rule.put("author", r.getAuthor());
            rule.put("createDate", r.getCreateDate());
            rule.put("modifyDate", r.getModifyDate());
            rule.put("antecedent", JSONGenerator.createAtoms(r.getAntecedents()));
            rule.put("consequent", JSONGenerator.createAtoms(r.getConseqeunts()));
            ruleList.add(rule);
        }

        jsonObject.put("ruleList", ruleList);
        return jsonObject;
    }

    public static ArrayList<String> generateList(String[] array){
        ArrayList<String> list = null;
        if(array != null){
            list = new ArrayList<String>();
            for(String elm : array){
                list.add(elm);
            }
        }

        return list;
    }
}
