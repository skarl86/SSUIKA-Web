package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.SQLManager;
import kr.ac.ssu.ikatool.util.sql.entitiy.Atom;
import kr.ac.ssu.ikatool.util.sql.entitiy.OpinionRuleResult;
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
        OpinionRuleResult result = SQLManager.getRuleByOpinion(patientID, opinionID);
        JSONObject rule = new JSONObject();
        JSONArray ruleList = new JSONArray();
        for(Rule r : result.getRules()){
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

        for(Atom atom : atoms){
            result.add(atom.getName());
        }

        return result;
    }
}
