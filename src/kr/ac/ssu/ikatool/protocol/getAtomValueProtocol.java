package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.obj.Value;
import kr.ac.ssu.ikatool.util.sql.SQLManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-01-04.
 */
public class getAtomValueProtocol extends IKAProtocol{
    public static JSONObject getAtomValue(String sAtomID){
        //SELECT atom_value.value_id ,value.value_str FROM `atom_value` AS atom_value, value AS value WHERE atom_value.value_id = value.value_id AND atom_value.atom_id = 1
        ArrayList<Value> valueList = SQLManager.getAtomValue(Integer.parseInt(sAtomID.trim()));

        JSONArray valueJsonList = new JSONArray();
        JSONObject value = null;

        if(!valueList.equals(null)){
            JSONObject jsonObject = getDefaultJSON();

            for (Value value1 : valueList) {
                value = new JSONObject();
                value.put("value_id", value1.getId());
                value.put("value_str",value1.getName());

                valueJsonList.add(value);
            }
            jsonObject.put("valueList", valueJsonList);

            return jsonObject;
        }else {
            return getDefaultJSON("해당하는 Atom에 Value가 없습니다.", "-1");
        }
    }
}
