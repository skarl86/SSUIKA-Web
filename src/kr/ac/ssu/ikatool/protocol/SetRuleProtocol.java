package kr.ac.ssu.ikatool.protocol;

import kr.ac.ssu.ikatool.obj.*;
import kr.ac.ssu.ikatool.util.sql.SQLManager;
import kr.ac.ssu.ikatool.util.sql.entitiy.Atom;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-12-29.
 */
public class SetRuleProtocol {
    public static JSONObject insertRule(String sPatientID, String sOpinionID, String sAntecedents, String sConsequents, String sAuthorID) throws SQLException, ClassNotFoundException {


        /*AST_H!@ASP_N!@AAA_P
        !@ 잘라서 ArrayList<Atmo>
        Atom : atomName, atomValue

        Array.for
            Atom 가져와서 Query날려서
            if(resultSet.count = 0){
                insert atom
                }
             get ID
         */

        ArrayList<ValuedAtom> arrAnt = setAtom(parseAtom(sAntecedents));
        ArrayList<Integer> antVaIDList = getVaIdList(arrAnt);

        ArrayList<ValuedAtom> arrCon = setAtom(parseAtom(sConsequents));
        ArrayList<Integer> conVaIDList = getVaIdList(arrCon);

        HashMap<Integer, ArrayList<Integer>> antHash = SQLManager.get_ant_Rule();
        HashMap<Integer, ArrayList<Integer>> conHash = SQLManager.get_con_Rule();
        HashMap<Integer, Rule> ruleHashMap = new HashMap<>();

        // Antecedent 처리
        for (Map.Entry<Integer, ArrayList<Integer>> entry : antHash.entrySet()) {
            ArrayList<Integer> antIdArr = entry.getValue();
            ArrayList<ValuedAtom> antArr = new ArrayList<>();

            for (int i=0; i<antIdArr.size(); i++) {
                antArr.add(new ValuedAtom(antIdArr.get(i)));
            }
            Rule newRule = new Rule(entry.getKey(), antArr);
            ruleHashMap.put(entry.getKey(), newRule);
        }

        // Consequent 처리
        for (Map.Entry<Integer, ArrayList<Integer>> entry : conHash.entrySet()) {
            ArrayList<Integer> conIdArr = entry.getValue();
            ArrayList<ValuedAtom> conArr = new ArrayList<>();

            for (int i=0; i<conIdArr.size(); i++) {
                conArr.add(new ValuedAtom(conIdArr.get(i)));
            }

            Rule rule = ruleHashMap.get(entry.getKey());
            rule.setConArr(conArr);
        }

        // 중복 롤 체크
        ArrayList<Rule> ruleList = new ArrayList<>(ruleHashMap.values());
        for (Rule rule : ruleList) {
            if (rule.isSameRule(antVaIDList, conVaIDList)) {
                System.out.println("중복된 규칙이 있습니다.");
                return getJSON("중복된 규칙이 있습니다.", "-1");
            }
        }

        int ruleID = SQLManager.insertRule(antVaIDList, "ant");
        SQLManager.insertRule(conVaIDList, "con");

        //rule_ref 삽입

        SQLManager.insertPatientOpinionRule( Integer.parseInt(sPatientID), Integer.parseInt(sOpinionID), ruleID);
        SQLManager.insertRuleUser(ruleID, Integer.parseInt(sAuthorID));


        return getJSON("성공", "0");

    }

    public static ArrayList<Integer> getVaIdList(ArrayList<ValuedAtom> ruleInAtoms) {
        ArrayList<Integer> list = new ArrayList<>();

        for (ValuedAtom va : ruleInAtoms) {
            list.add(va.getId());
        }

        return list;
    }



    public static ArrayList<ValuedAtom> parseAtom(String s) {
        ArrayList<ValuedAtom> atomList = new ArrayList<>();
        String[] tempArr = s.split("!@");
        //AST_H!@ASP_N!@AAA_P
        for (String sAtom : tempArr) {
            String[] arrAtom = sAtom.split("_",2);
            atomList.add(new ValuedAtom(new KSAtom(arrAtom[0]), new Value(arrAtom[1])));
        }
        return atomList;
    }
    public static ArrayList<ValuedAtom> setAtom(ArrayList<ValuedAtom> arr) throws SQLException {
        for (ValuedAtom vAtom : arr) {
            //atom이 존재 여부 파악
            String atomName = vAtom.getAtom().getName();
            if(!SQLManager.isExistedAtom(atomName)){
                //존재 안함
                SQLManager.insertAtom_Default(atomName, 0);
                int atomID = SQLManager.getAtomID(atomName);
                //새로운 ATOM 일때 기본 6가지 value 설정
                for(int i =1 ; i < 7 ; i++){
                    SQLManager.insertAtom_Value(atomID, i);
                }
            }

            //getID
            int atomID = SQLManager.getAtomID(atomName);

            vAtom.getAtom().setId(atomID);

            //value 존재 여부 파악
            String valueName = vAtom.getValue().getName();

            if(!SQLManager.isExistedValue(valueName)){
                //존재 안함
                SQLManager.insertValue(valueName);

                int valueID = SQLManager.getValueID(valueName);

                //atom_value에 삽입
                SQLManager.insertAtom_Value(atomID, valueID);
            }

            int valueID = SQLManager.getValueID(valueName);

            vAtom.getValue().setId(valueID);

            //valued_atom
            if(!SQLManager.isExistedValuedAtom(atomID, valueID)){
                //valued_atom에 존재 하지 않으므로 삽입
                SQLManager.insertValued_Atom(atomID, valueID);
            }

            int valuedID = SQLManager.getValued_atomID(atomID, valueID);
            vAtom.setId(valuedID);
        }
        return arr;
    }

    /*public static ArrayList<RuleInAtom> setAtom(ArrayList<RuleInAtom> arr) throws SQLException {
        for (RuleInAtom ruleInAtom : arr) {
            ResultSet checkAtomRs = SQLManager.checkAtom(ruleInAtom);
            checkAtomRs.next();
            if (SQLManager.getResultSetSize(checkAtomRs) > 0) {
                //checkAtomRs :
                //조회시 atom 있을때
                ResultSet valuedAtmoRs = SQLManager.getValuedAtom(ruleInAtom);

                if (SQLManager.getResultSetSize(valuedAtmoRs) > 0) {
                    valuedAtmoRs.next();
                    ruleInAtom.setId(Integer.parseInt(valuedAtmoRs.getString("va_id")));

                } else {
                    //valued_atom에 데이터 쳐넣어
                    String atom_id = checkAtomRs.getString("atom_id");
                    String value_id = checkAtomRs.getString("_value_id");

                    if (SQLManager.insertValued_Atom(atom_id, value_id)) {
                        //성공
                        int va_id = SQLManager.getValued_atomID(atom_id, value_id);
                        ruleInAtom.setId(va_id);



                    } else {
                        System.out.println("insertValued_Atom Error ");
                    }
                }
            } else {
                //조회시 atom 없을때
                //RuleInAtom Name, value
                int atom_Default_ID = SQLManager.insertAtom_Default(ruleInAtom.getName(), 0);
                if (atom_Default_ID > 0) {

                    int atom_valueID = SQLManager.getValueID(ruleInAtom.getValue());
                    if (SQLManager.insertValued_atom(atom_Default_ID, atom_valueID)) {
                        continue;
                    } else {
                        System.out.println("atom_valueID Insert False");
                    }


                } else {
                    System.out.println("insertAtom_Default False");
                }


            }
        }

        return arr;
    }*/

    public static JSONObject getJSON(String msg, String code){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("rt_msg", msg);
        jsonObject.put("rt_code", code);
        return jsonObject;
    }




}
