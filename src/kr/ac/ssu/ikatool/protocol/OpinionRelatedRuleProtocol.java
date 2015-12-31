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
            rule.put("antecedent", JSONGenerator.createAtoms(r.getAntecedents()));
            rule.put("consequent", JSONGenerator.createAtoms(r.getConseqeunts()));
            ruleList.add(rule);
        }

        jsonObject.put("ruleList", ruleList);
        jsonObject.put("highlight", new JSONArray());
        return jsonObject;

    }
}
