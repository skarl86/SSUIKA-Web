package kr.ac.ssu.ikatool.util.sql;

import java.sql.*;
import java.util.ArrayList;

import kr.ac.ssu.ikatool.protocol.AutoCompleteListProtocol;
import kr.ac.ssu.ikatool.util.sql.entitiy.*;

/**
 * Created by NCri on 2015. 12. 23..
 */
public class SQLManager {
    // JDBC driver name and database URL
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

    public static OpinionRuleResult getRuleByOpinion(String patientID, String opinionID){

        Connection conn = null;
        PreparedStatement prestmt = null;

        OpinionRuleResult result = new OpinionRuleResult();

        try{
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            String ruleIDColumn = "rule_user.rule_id";
            String authorNameColumn = "user.name";
            String createdDateColumn = "created_date";
            String modifiedDateColumn = "modified_date";
            String atomIDColumn = "atom_default.atom_id";
            String atomTypeColumn = "atom_default.atom_type";
            String atomNameColumn = "atom_name";
            String valueStrColumn = "value_str";

            String sql = "SELECT rule_user.rule_id, user.name, created_date, modified_date, atom_default.atom_id, atom_default.atom_type, atom_name, value_str " +
                    "FROM (patient_opinion_rule JOIN rule_user rule_user ON id = ? AND opinion_id = ? " +
                    "JOIN user ON rule_user.user_id = user.id " +
                    "JOIN rule_ant " +
                    "JOIN valued_atom ON ant_va_id = va_id) " +
                    "JOIN atom_default ON valued_atom.atom_id = atom_default.atom_id " +
                    "JOIN value on valued_atom.value_id = value.value_id";
            System.out.println(String.format("Patient ID : %s / Opinion ID : %s", patientID, opinionID));

            prestmt = conn.prepareStatement(sql);

//            prestmt.setString(1, ruleIDColumn);
//            prestmt.setString(2, authorNameColumn);
//            prestmt.setString(3, createdDateColumn);
//            prestmt.setString(4, modifiedDateColumn);
//            prestmt.setString(5, atomIDColumn);
//            prestmt.setString(6, atomTypeColumn);
//            prestmt.setString(7, atomNameColumn);
//            prestmt.setString(8, valueStrColumn);

            prestmt.setString(1, patientID);
            prestmt.setString(2, opinionID);

            ResultSet rs = prestmt.executeQuery();

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
                ruleID = rs.getInt(ruleIDColumn);
                author = rs.getString(authorNameColumn);
                createdDate = rs.getString(createdDateColumn);
                modifiedDate = rs.getString(modifiedDateColumn);
                atomID = rs.getInt(atomIDColumn);
                atomType = rs.getInt(atomTypeColumn);
                atomName = rs.getString(atomNameColumn);
                valueString = rs.getString(valueStrColumn);

                rule = result.getRuleByID(ruleID);

                if(rule == null)
                    rule = new Rule(ruleID, author, createdDate, modifiedDate);

                rule.addAntecedents(new Atom(atomID, atomName,atomType, valueString));
                result.addRule(rule);
            }

            sql = "SELECT rule_user.rule_id, user.name, created_date, modified_date, atom_default.atom_id, atom_default.atom_type, atom_name, value_str " +
                    "FROM (patient_opinion_rule JOIN rule_user rule_user ON id = ? AND opinion_id = ? " +
                    "JOIN user ON rule_user.user_id = user.id " +
                    "JOIN rule_con " +
                    "JOIN valued_atom ON con_va_id = va_id) " +
                    "JOIN atom_default ON valued_atom.atom_id = atom_default.atom_id " +
                    "JOIN value on valued_atom.value_id = value.value_id";

            prestmt = conn.prepareStatement(sql);

//            prestmt.setString(1, ruleIDColumn);
//            prestmt.setString(2, authorNameColumn);
//            prestmt.setString(3, createdDateColumn);
//            prestmt.setString(4, modifiedDateColumn);
//            prestmt.setString(5, atomIDColumn);
//            prestmt.setString(6, atomTypeColumn);
//            prestmt.setString(7, atomNameColumn);
//            prestmt.setString(8, valueStrColumn);

            prestmt.setString(1, patientID);
            prestmt.setString(2, opinionID);

            rs = prestmt.executeQuery();

            // Consequent Query Result
            while(rs.next()){
                //Retrieve by column name
                ruleID = rs.getInt(ruleIDColumn);
                author = rs.getString(authorNameColumn);
                createdDate = rs.getString(createdDateColumn);
                modifiedDate = rs.getString(modifiedDateColumn);
                atomID = rs.getInt(atomIDColumn);
                atomType = rs.getInt(atomTypeColumn);
                atomName = rs.getString(atomNameColumn);
                valueString = rs.getString(valueStrColumn);

                rule = result.getRuleByID(ruleID);

                if(rule == null)
                    rule = new Rule(ruleID, author, createdDate, modifiedDate);

                rule.addCoseqeunts(new Atom(atomID, atomName,atomType, valueString));
                result.addRule(rule);
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

            //STEP 5: Extract data from result set
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
