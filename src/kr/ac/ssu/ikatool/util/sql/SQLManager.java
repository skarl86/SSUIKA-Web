package kr.ac.ssu.ikatool.util.sql;

import java.sql.*;
import java.util.*;

import kr.ac.ssu.ikatool.protocol.AutoCompleteListProtocol;
import kr.ac.ssu.ikatool.util.sql.entitiy.*;

/**
 * Created by NCri ON 2015. 12. 23..
 */
public class SQLManager {
    // JDBC driver name AND database URL
    static final int ANTCEDENT = 1;
    static final int CONSEQUENT = 2;

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://ailab.synology.me/medicaldb";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    private static Connection getConn() throws ClassNotFoundException, SQLException {
        //STEP 2: Register JDBC driver
        Class.forName("com.mysql.jdbc.Driver");

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        return DriverManager.getConnection(DB_URL,USER,PASS);
    }

    public static RuleResult getRuleByPatient(String patientID){
        return getRuleByOpinion(patientID, null);
    }
    public static RuleResult generateRule(RuleResult result, ResultSet rs, int antOrCon) throws SQLException {
        int ruleID = -1;
        String author = null;
        String createdDate = null;
        String modifiedDate = null;
        int atomID = -1;
        int atomType = -1;
        String atomName = null;
        String valueString = null;

        Rule rule = null;

        // Antecedent Query Result
        while(rs.next()){
            //Retrieve by column name
            ruleID = rs.getInt(2);
            author = rs.getString(4);
            createdDate = rs.getString(5);
            modifiedDate = rs.getString(6);
            atomID = rs.getInt(7);
            atomName = rs.getString(9);
            atomType = rs.getInt(10);
            valueString = rs.getString(11);

            rule = result.getRuleByID(ruleID);

            if(rule == null)
                rule = new Rule(ruleID, author, createdDate, modifiedDate);

            if(antOrCon == ANTCEDENT)
                rule.addAntecedents(new Atom(atomID, atomName,atomType, valueString));
            else if(antOrCon == CONSEQUENT)
                rule.addCoseqeunts(new Atom(atomID, atomName,atomType, valueString));
            result.addRule(rule);
        }

        return result;
    }
    public static Integer getAtomIDByName(String name){
        Integer id = -1;

        Connection conn = null;
        PreparedStatement prestmt = null;

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();


            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql = "select atom_id from  atom_default where atom_name = ?";

            prestmt = conn.prepareStatement(sql);
            prestmt.setString(1, name);

            ResultSet rs = prestmt.executeQuery();

            while(rs.next()){
                //Retrieve by column name
                id = rs.getInt(1);
            }

            //STEP 6: Clean-up environment
            rs.close();
            prestmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(prestmt!=null)
                    prestmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return id;
    }
    public static ArrayList<Integer> generateAtomIDList(ArrayList<String> atomNameList){
        ArrayList<Integer> idList = new ArrayList<Integer>();
        for(String name : atomNameList){
            idList.add(getAtomIDByName(name));
        }
        return idList;
    }
    private static HashMap<Integer, Rule> generateRule(ResultSet rs, HashMap<Integer, Rule> ruleMap, int antOrCons) throws SQLException {
        int ruleID = -1;
        int atomID = -1;
        String atomName = null;
        int atomType = -1;
        int valueID = -1;
        String valueName = null;

        Rule rule = null;

        // rule_id  new_atom_id atom_name   atom_type   new_value_id    value_str

        while(rs.next()){
            //Retrieve by column name
            ruleID = rs.getInt(1);
            atomID = rs.getInt(2);
            atomName = rs.getString(3);
            atomType = rs.getInt(4);
            valueID = rs.getInt(5);
            valueName = rs.getString(6);

            if(ruleMap.containsKey(ruleID)){
                rule = ruleMap.get(ruleID);
            } else {
                rule = new Rule(ruleID);
            }

            if(antOrCons == ANTCEDENT){
                rule.addAntecedents(new Atom(atomID,atomName, atomType, new Value(valueID, valueName)));
            }else if(antOrCons == CONSEQUENT){
                rule.addCoseqeunts(new Atom(atomID,atomName, atomType, new Value(valueID, valueName)));
            }

            ruleMap.put(ruleID, rule);
        }
        return ruleMap;
    }
    public static HashMap<Integer, Rule> generateRuleMap(){
        Connection conn = null;
        Statement stmt = null;

        HashMap<Integer, Rule> ruleMap = new HashMap<Integer, Rule>();

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            stmt = conn.createStatement();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            String sql = "select rule_id, new_atom_id, atom_name, atom_type, new_value_id, value_str from " +
                            "(select rule_id, new_atom_id, atom_name, atom_type, new_value_id from " +
                                "(select rule_id, atom_id as new_atom_id, value_id as new_value_id from " +
                                    "rule_ant join valued_atom on ant_va_id = va_id) " +
                                "as A join atom_default on new_atom_id = atom_id) " +
                            "as B join value on value_id = new_value_id";
            ResultSet rs = stmt.executeQuery(sql);
            ruleMap = generateRule(rs, ruleMap, ANTCEDENT);

            sql = "select rule_id, new_atom_id, atom_name, atom_type, new_value_id, value_str from " +
                        "(select rule_id, new_atom_id, atom_name, atom_type, new_value_id from " +
                            "(select rule_id, atom_id as new_atom_id, value_id as new_value_id from " +
                                "rule_con join valued_atom on con_va_id = va_id) " +
                            "as A join atom_default on new_atom_id = atom_id) " +
                        "as B join value on value_id = new_value_id";
            rs = stmt.executeQuery(sql);
            ruleMap = generateRule(rs, ruleMap, CONSEQUENT);

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return ruleMap;
    }
    public static Set<Rule> getAssociateRule(ArrayList<String> antecedent, ArrayList<String> consequent){
        ArrayList<Integer> antAtomIDList = generateAtomIDList(antecedent);
        ArrayList<Integer> consAtomIDList = generateAtomIDList(consequent);

        HashMap<Integer, Rule> ruleMap = generateRuleMap();

        Set<Rule> associateRuleSet = new HashSet<Rule>();

        boolean isAssociateAntecedent = false;
        boolean isAssociateConsequent = false;
        Rule rule = null;
        for(Integer key : ruleMap.keySet()){

            isAssociateAntecedent = true;
            isAssociateConsequent = true;

            rule = ruleMap.get(key);

            if(rule != null){
                for(Integer atomID : antAtomIDList){
                    for(Atom exAtom : rule.getAntecedents()){
                        isAssociateAntecedent &= exAtom.getId() == atomID;
                    }
                }

                for(Integer atomID : consAtomIDList){
                    for(Atom exAtom : rule.getConseqeunts()){
                        isAssociateConsequent &= exAtom.getId() == atomID;
                    }
                }

                if(isAssociateAntecedent | isAssociateConsequent) associateRuleSet.add(rule);
            }
        }

        return associateRuleSet;
    }
    public static RuleResult getRuleByOpinion(String patientID, String opinionID){

        Connection conn = null;
        Statement stmt = null;

//        RuleResult result = new RuleResult();
        RuleResult result = new RuleResult();

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            stmt = conn.createStatement();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");

            // Antecedent를 가져올때랑 Consequent를 가져올 때 Query가 달라진다.
            String sql = QueryManager.generatedQueryAntecedentsRuleListBy(patientID, opinionID);

            System.out.println(String.format("Patient ID : %s / Opinion ID : %s", patientID, opinionID));

            // Antecedent 가져오기.
            ResultSet rs = stmt.executeQuery(sql);

            result = generateRule(result, rs, ANTCEDENT);

            sql = QueryManager.generatedQueryConsequentsRuleListBy(patientID, opinionID);

            // Consequent 가져오기.
            rs = stmt.executeQuery(sql);

            result = generateRule(result, rs, CONSEQUENT);

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return result;

    }

    public static ArrayList<Atom> getAutoCompleteList(String inputText, int flag){
        Connection conn = null;
        PreparedStatement prestmt = null;

        ArrayList<Atom> list = new ArrayList<Atom>();

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            String sql = "SELECT * FROM atom_default WHERE atom_name LIKE ?";
            System.out.println("Input Text = " + inputText);

            if(flag == AutoCompleteListProtocol.CONTAIN){
                // Contain
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, "%" + inputText + "%");
            }else if(flag == AutoCompleteListProtocol.SEQUENTAIL){
                // Sequential
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, inputText + "%");
            }else{
                // error
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, "%" + inputText);
            }

            ResultSet rs = prestmt.executeQuery();

            while(rs.next()){
                //Retrieve by column name
                int atomId = rs.getInt("atom_id");
                String atomName = rs.getString("atom_name");
                int atomType = rs.getInt("atom_type");

                list.add(new Atom(atomId, atomName, atomType));
            }

            //STEP 6: Clean-up environment
            rs.close();
            prestmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(prestmt!=null)
                    prestmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return list;
    }

    public static Rule getRuleList(String patientID, String opinionID){
        Connection conn = null;
        Statement stmt = null;

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String ruleIdColumn = "rule_id";
            String atomNameColumn = "atom_name";

            String sql = String.format("SELECT %s, %s FROM rule_ant JOIN atom_default WHERE ant_va_id = atmo_id",ruleIdColumn, atomNameColumn);
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                //Retrieve by column name
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return null;
    }

    public static Patient getPatientList(String paramPatientID){
        Connection conn = null;
        Statement stmt = null;

        Patient pat = null;

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT P.id, name, age, gender, opinion_id, content " +
                    "FROM patient_default AS P JOIN patient_opinion AS O " +
                    "ON P.id = O.id " +
                    "WHERE P.id = " + paramPatientID;
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data FROM result set
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("P.id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                int opnId = rs.getInt("opinion_id");
                String content = rs.getString("content");

                if(pat == null)
                    pat = new Patient(String.valueOf(id), name, String.valueOf(age), gender, null, new ArrayList<Opinion>());

                pat.addOpinion(new Opinion(String.valueOf(opnId), content));

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Age: " + age);
                System.out.print(", name: " + name);
                System.out.println(", gender: " + gender);
                System.out.println(", opinion: " + content);
            }
            String testItemCodeColumn = "testitem_code";
            String subCodeColumn = "sub_code";
            String testItemNameColumn = "testitem_name";
            String numValueColumn = "num_value";
            String strValueColumn = "str_value";
            String query = String.format("SELECT id, %s, %s, %s, %s, %s  FROM patient_detail WHERE id = %s", testItemCodeColumn, subCodeColumn, testItemNameColumn, numValueColumn, strValueColumn, paramPatientID);
            rs = stmt.executeQuery(query);

            DetailInfo info = null;

            while(rs.next()){
                int id = rs.getInt("id");
                int testItemCode = rs.getInt(testItemCodeColumn);
                int subCode = rs.getInt(subCodeColumn);
                String testItemName = rs.getString(testItemNameColumn);
//                if(testItemName == null){
//                    testItemName = "";
//                }
                float numValue = rs.getFloat(numValueColumn);
                String strValue = rs.getString(strValueColumn);
//                if(strValue == null){
//                    strValue = "";
//                }

                if(info == null)
                    // 처음 생성자에서 정보를 넣어주면 addItem동작한다. 그러니 굳이 addItem을 호출할 필요는 없다.
                    info = new DetailInfo(id, testItemCode, subCode, testItemName, numValue, strValue);
                else
                    info.addItem(testItemCode, subCode, testItemName, numValue, strValue);
            }

            if(pat != null)
                pat.addDetailInfo(info);

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return pat;
    }
}
