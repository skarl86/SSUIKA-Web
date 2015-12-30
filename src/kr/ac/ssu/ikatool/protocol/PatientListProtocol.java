package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.entitiy.Opinion;
import kr.ac.ssu.ikatool.util.sql.entitiy.Patient;
import kr.ac.ssu.ikatool.util.sql.entitiy.TestItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import kr.ac.ssu.ikatool.util.sql.SQLManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by NCri on 2015. 12. 23..
 */
public class PatientListProtocol extends IKAProtocol{
    public static JSONObject getPatientList(String patientID){
        Patient pat = SQLManager.getPatientList(patientID);
        JSONObject jsonObject = getDefaultJSON("patientInfo","0");
        JSONArray opinionList = new JSONArray();
        for(Opinion opn : pat.getOpinionList()){
            JSONObject obj = new JSONObject();
            obj.put("opinionID", new Integer(opn.getOpinionID()));
            obj.put("opinionString", opn.getOpinionContents());
            opinionList.add(obj);
        }
        jsonObject.put("id", patientID);
        jsonObject.put("name", pat.getName());
        jsonObject.put("age", new Integer(pat.getAge()));
        jsonObject.put("sex", pat.getSex());
        jsonObject.put("opinionList", opinionList);

        // Detail info 추가.
        JSONArray detailList = new JSONArray();
        Set<Integer> testItemCodes = pat.getDetailInfo().getTestItemCodes();
        JSONArray itemList = null;
        JSONObject itemMap = null;
        for(Integer testItemCode : testItemCodes){
            itemList = new JSONArray();
            itemMap = new JSONObject();

            ArrayList<TestItem> items = pat.getDetailInfo().getItems(testItemCode);
            for(TestItem obj : items){
                JSONObject item = new JSONObject();
                item.put("testItemName", obj.getName());
                item.put("numValue", obj.getNumValue());
                item.put("strValue", obj.getStrValue());
                itemList.add(item);
            }
            itemMap.put("testItemCode", testItemCode);
            itemMap.put("testItems", itemList);
            detailList.add(itemMap);
        }
        jsonObject.put("detailInfo", detailList);

        return jsonObject;
    }
}
