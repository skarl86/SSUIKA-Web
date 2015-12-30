package kr.ac.ssu.ikatool.protocol;

import org.json.simple.JSONObject;


/**
 * Created by NCri on 2015. 12. 30..
 */
public class IKAProtocol {
    public static JSONObject getDefaultJSON(){
        return getDefaultJSON("", "0");
    }

    public static JSONObject getDefaultJSON(String msg, String rtCode){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rt_msg", msg);
        jsonObject.put("rt_code", rtCode);

        return jsonObject;
    }
}
