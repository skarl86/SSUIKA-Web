package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.SQLManager;
import org.json.simple.JSONObject;

import java.sql.SQLException;

/**
 * Created by Administrator on 2015-12-31.
 */
public class DeleteRuleProtocol {
    public static JSONObject deleteRule(String sPatientID ,String sOpinionID, String sRuleID){
        try {
            if(SQLManager.deleteRule(sPatientID,sOpinionID,sRuleID)){
                return getJSON("성공", "0");
            }else {
                return getJSON("unKnwon Error", "-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getJSON("unKnwon Error", "-1");

        }

    }

    public static JSONObject getJSON(String msg, String code){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("rt_msg", msg);
        jsonObject.put("rt_code", code);
        return jsonObject;
    }
}
