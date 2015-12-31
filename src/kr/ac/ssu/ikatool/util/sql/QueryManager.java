package kr.ac.ssu.ikatool.util.sql;

/**
 * Created by NCri on 2015. 12. 31..
 */
public class QueryManager {
    public static String generatedQueryAntecedentsRuleListBy(String patientID, String opinionID){
        String sql = null;

        if(opinionID == null){
            sql =   "SELECT pat_id, rule_id, user_id, name, created_date, modified_date , D.atom_id, D.value_id, atom_name, atom_type, value.value_str FROM " +
                    "(SELECT pat_id, rule_id, va_id, C.atom_id, value_id, atom_name, atom_type, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, B.rule_id, B.va_id, atom_id, value_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, A.rule_id, ant_va_id AS va_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT patient_opinion_rule.id AS pat_id, patient_opinion_rule.rule_id, rule_user.user_id, user.name, rule_user.created_date, rule_user.modified_date FROM " +
                    " patient_opinion_rule JOIN rule_user JOIN user on rule_user.rule_id = patient_opinion_rule.rule_id WHERE patient_opinion_rule.id = " + patientID + ") " +
                    "AS A JOIN rule_ant ON A.rule_id = rule_ant.rule_id) " +
                    "AS B JOIN valued_atom ON B.va_id = valued_atom.va_id) " +
                    "AS C JOIN atom_default ON C.atom_id = atom_default.atom_id) " +
                    "AS D JOIN value ON D.value_id = value.value_id";
        }else {
            sql =   "SELECT pat_id, rule_id, user_id, name, created_date, modified_date , D.atom_id, D.value_id, atom_name, atom_type, value.value_str FROM " +
                    "(SELECT pat_id, rule_id, va_id, C.atom_id, value_id, atom_name, atom_type, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, B.rule_id, B.va_id, atom_id, value_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, A.rule_id, ant_va_id AS va_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT patient_opinion_rule.id AS pat_id, patient_opinion_rule.rule_id, rule_user.user_id, user.name, rule_user.created_date, rule_user.modified_date FROM " +
                    " patient_opinion_rule JOIN rule_user JOIN user on rule_user.rule_id = patient_opinion_rule.rule_id WHERE patient_opinion_rule.id = "+ patientID +" AND opinion_id = "+opinionID+") " +
                    "AS A JOIN rule_ant ON A.rule_id = rule_ant.rule_id) " +
                    "AS B JOIN valued_atom ON B.va_id = valued_atom.va_id) " +
                    "AS C JOIN atom_default ON C.atom_id = atom_default.atom_id) " +
                    "AS D JOIN value ON D.value_id = value.value_id";
        }

        return sql;
    }

    public static String generatedQueryConsequentsRuleListBy(String patientID, String opinionID){
        String sql = null;
        String columnName = "con_va_id";
        String columnName2 = "rule_con";
        if(opinionID == null){
            sql =   "SELECT pat_id, rule_id, user_id, name, created_date, modified_date , D.atom_id, D.value_id, atom_name, atom_type, value.value_str FROM " +
                    "(SELECT pat_id, rule_id, va_id, C.atom_id, value_id, atom_name, atom_type, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, B.rule_id, B.va_id, atom_id, value_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, A.rule_id, con_va_id AS va_id , user_id, name, created_date, modified_date FROM " +
                    "(SELECT patient_opinion_rule.id AS pat_id, patient_opinion_rule.rule_id, rule_user.user_id, user.name, rule_user.created_date, rule_user.modified_date FROM " +
                    " patient_opinion_rule JOIN rule_user JOIN user on rule_user.rule_id = patient_opinion_rule.rule_id WHERE patient_opinion_rule.id = " + patientID + ") " +
                    "AS A JOIN rule_con ON A.rule_id = rule_con.rule_id) " +
                    "AS B JOIN valued_atom ON B.va_id = valued_atom.va_id) " +
                    "AS C JOIN atom_default ON C.atom_id = atom_default.atom_id) " +
                    "AS D JOIN value ON D.value_id = value.value_id";
        }else {
            sql =   "SELECT pat_id, rule_id, user_id, name, created_date, modified_date , D.atom_id, D.value_id, atom_name, atom_type, value.value_str FROM " +
                    "(SELECT pat_id, rule_id, va_id, C.atom_id, value_id, atom_name, atom_type, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, B.rule_id, B.va_id, atom_id, value_id, user_id, name, created_date, modified_date FROM " +
                    "(SELECT pat_id, A.rule_id, con_va_id AS va_id , user_id, name, created_date, modified_date FROM " +
                    "(SELECT patient_opinion_rule.id AS pat_id, patient_opinion_rule.rule_id, rule_user.user_id, user.name, rule_user.created_date, rule_user.modified_date FROM " +
                    " patient_opinion_rule JOIN rule_user JOIN user on rule_user.rule_id = patient_opinion_rule.rule_id WHERE patient_opinion_rule.id = "+ patientID +" AND opinion_id = "+opinionID+") " +
                    "AS A JOIN rule_con ON A.rule_id = rule_con.rule_id) " +
                    "AS B JOIN valued_atom ON B.va_id = valued_atom.va_id) " +
                    "AS C JOIN atom_default ON C.atom_id = atom_default.atom_id) " +
                    "AS D JOIN value ON D.value_id = value.value_id";
        }

        return sql;
    }
}
