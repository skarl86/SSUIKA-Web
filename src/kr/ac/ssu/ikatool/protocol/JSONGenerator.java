package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.util.sql.entitiy.Atom;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by NCri on 2015. 12. 31..
 */
public class JSONGenerator {
    public static JSONArray createAtoms(ArrayList<Atom> atoms){
        JSONArray result = new JSONArray();
        JSONObject atomMap = null;
        for(Atom atom : atoms){
            atomMap = new JSONObject();
            atomMap.put("name", atom.getName());
            atomMap.put("value", atom.getValueStr());
            result.add(atomMap);
        }

        return result;
    }
}
