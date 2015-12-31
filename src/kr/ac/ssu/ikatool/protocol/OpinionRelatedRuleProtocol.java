package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.SQLManager;
import kr.ac.ssu.ikatool.util.sql.entitiy.Atom;
import kr.ac.ssu.ikatool.util.sql.entitiy.RuleResult;
import kr.ac.ssu.ikatool.util.sql.entitiy.Rule;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by NCri on 2015. 12. 28..
 */
public class OpinionRelatedRuleProtocol extends IKAProtocol{
    public static JSONObject getRuleList(String patientID, String opinionID){
        JSONObject jsonObject = getDefaultJSON();
        RuleResult result = SQLManager.getRuleByOpinion(patientID, opinionID);
        JSONObject rule = null;
        JSONArray ruleList = new JSONArray();
        for(Rule r : result.getRules()){
            rule = new JSONObject();
            rule.put("ruleID", r.getId());
            rule.put("author", r.getAuthor());
            rule.put("createDate", r.getCreateDate());
            rule.put("modifyDate", r.getModifyDate());
            rule.put("antecedent", createAtoms(r.getAntecedents()));
            rule.put("consequent", createAtoms(r.getConseqeunts()));
            ruleList.add(rule);
        }

        jsonObject.put("ruleList", ruleList);
        jsonObject.put("highlight", new JSONArray());
        return jsonObject;

    }
    public static JSONArray createAtoms(ArrayList<Atom> atoms){
        JSONArray result = new JSONArray();
        JSONObject atomMap = null;
        for(Atom atom : atoms){
            atomMap = new JSONObject();
            atomMap.put("name", atom.getName());
            atomMap.put("value", atom.getValue());
            result.add(atomMap);
        }

        return result;
    }
}
