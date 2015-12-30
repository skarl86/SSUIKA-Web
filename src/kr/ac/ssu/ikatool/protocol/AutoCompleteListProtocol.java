package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.SQLManager;
import kr.ac.ssu.ikatool.util.sql.entitiy.Atom;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by NCri on 2015. 12. 29..
 */
public class AutoCompleteListProtocol extends IKAProtocol{
    public final static int SEQUENTAIL = 0;
    public final static int CONTAIN = 1;

    public static JSONObject getCompleteRuleList(String inputText, int flag){
        JSONObject jsonObject = getDefaultJSON();
        ArrayList<Atom> list = SQLManager.getAutoCompleteList(inputText, flag);
        JSONArray atomList = new JSONArray();
        JSONObject atom = null;
        for(Atom atm : list ){
            atom = new JSONObject();
            atom.put("atomID", atm.getId());
            atom.put("atomName", atm.getName());
            atom.put("atomType", atm.getType());
            atomList.add(atom);
        }
        jsonObject.put("atomList", atomList);

        return jsonObject;
    }
}
