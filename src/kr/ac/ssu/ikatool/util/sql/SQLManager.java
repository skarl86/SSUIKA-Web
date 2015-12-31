package kr.ac.ssu.ikatool.util.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.ssu.ikatool.obj.*;

import kr.ac.ssu.ikatool.protocol.AutoCompleteListProtocol;
import kr.ac.ssu.ikatool.util.sql.entitiy.*;
import kr.ac.ssu.ikatool.util.sql.entitiy.Rule;

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
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static ArrayList<Atom> getAutoCompleteList(String inputText, int flag) {
        Connection conn = null;
        PreparedStatement prestmt = null;

        ArrayList<Atom> list = new ArrayList<Atom>();

        try {
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            String sql = "SELECT * FROM atom_default WHERE atom_name LIKE ?";
            System.out.println("Input Text = " + inputText);

            if (flag == AutoCompleteListProtocol.CONTAIN) {
                // Contain
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, "%" + inputText + "%");
            } else if (flag == AutoCompleteListProtocol.SEQUENTAIL) {
                // Sequential
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, inputText + "%");
            } else {
                // error
                prestmt = conn.prepareStatement(sql);
                prestmt.setString(1, "%" + inputText);
            }

            ResultSet rs = prestmt.executeQuery();

            while (rs.next()) {
                //Retrieve by column name
                int atomId = rs.getInt("atmo_id");
                String atomName = rs.getString("atom_name");
                int atomType = rs.getInt("atom_type");

                list.add(new Atom(atomId, atomName, atomType));
            }

            //STEP 6: Clean-up environment
            rs.close();
            prestmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (prestmt != null)
                    prestmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return list;
    }

    public static Rule getRuleList(String patientID, String opinionID) {
        Connection conn = null;
        Statement stmt = null;

        try {
            //STEP 2: Register JDBC driver
            //STEP 3: Open a connection
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String ruleIdColumn = "rule_id";
            String atomNameColumn = "atom_name";

            String sql = String.format("SELECT %s, %s FROM rule_ant JOIN atom_default WHERE ant_va_id = atmo_id", ruleIdColumn, atomNameColumn);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //Retrieve by column name
            }

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return null;
    }

    public static Patient getPatientList(String paramPatientID) {
        Connection conn = null;
        Statement stmt = null;

        Patient pat = null;

        try {
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
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("P.id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                int opnId = rs.getInt("opinion_id");
                String content = rs.getString("content");

                if (pat == null)
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

            while (rs.next()) {
                int id = rs.getInt("id");
                int testItemCode = rs.getInt(testItemCodeColumn);
                int subCode = rs.getInt(subCodeColumn);
                String testItemName = rs.getString(testItemNameColumn);
                float numValue = rs.getFloat(numValueColumn);
                String strValue = rs.getString(strValueColumn);

                if (info == null)
                    // 처음 생성자에서 정보를 넣어주면 addItem동작한다. 그러니 굳이 addItem을 호출할 필요는 없다.
                    info = new DetailInfo(id, testItemCode, subCode, testItemName, numValue, strValue);
                else
                    info.addItem(testItemCode, subCode, testItemName, numValue, strValue);
            }

            if (pat != null)
                pat.addDetailInfo(info);

            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");

        return pat;
    }

    /*public static ResultSet checkAtom(RuleInAtom atom) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String value ="";

        if(atom.getValue().isEmpty()){
            value = null;
        }else {
            value = atom.getValue();
        }

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT _atom_default.atom_id, \n" +
                    "       atom_value_join._value_id, \n" +
                    "       atom_value_join._value_str, \n" +
                    "       _atom_default.atom_name \n" +
                    "FROM   atom_default AS _atom_default, \n" +
                    "       (SELECT _value.value_str    AS _value_str, \n" +
                    "               _atom_value.atom_id AS _atom_id, \n" +
                    "               _value.value_id     AS _value_id \n" +
                    "        FROM   value AS _value, \n" +
                    "               atom_value AS _atom_value \n" +
                    "        WHERE  _atom_value.value_id = _value.value_id) AS atom_value_join \n" +
                    "WHERE  atom_value_join._atom_id = _atom_default.atom_id \n" +
                    "       AND _value_str = \'" + atom.getValue() + "\' \n" +
                    "       AND atom_name = \'" + atom.getName() + "\' ";
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return rs;

    }*/

    /*public static ResultSet getValuedAtom(RuleInAtom atom) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String value = "";

        if(atom.getValue().isEmpty()){
            value= null;
        }else {
            value = atom.getValue();
        }

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT va_id, \n" +
                    "       atom_value_str.atom_id, \n" +
                    "       atom_name, \n" +
                    "       value_id, \n" +
                    "       _value_str \n" +
                    "FROM   valued_atom, \n" +
                    "       (SELECT _atom_default.atom_id, \n" +
                    "               atom_value_join._value_id, \n" +
                    "               atom_value_join._value_str, \n" +
                    "               _atom_default.atom_name \n" +
                    "        FROM   atom_default AS _atom_default, \n" +
                    "               (SELECT _value.value_str    AS _value_str, \n" +
                    "                       _atom_value.atom_id AS _atom_id, \n" +
                    "                       _value.value_id     AS _value_id \n" +
                    "                FROM   value AS _value, \n" +
                    "                       atom_value AS _atom_value \n" +
                    "                WHERE  _atom_value.value_id = _value.value_id) AS \n" +
                    "               atom_value_join \n" +
                    "        WHERE  atom_value_join._atom_id = _atom_default.atom_id \n" +
                    "               AND _value_str = \'" + value + "\' \n" +
                    "               AND atom_name = \'" + atom.getName() + "\') AS atom_value_str \n" +
                    "WHERE  atom_value_str.atom_id = valued_atom.atom_id \n" +
                    "       AND atom_value_str._value_id = valued_atom.value_id ";
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return rs;

    }*/

    public static Boolean insertValued_Atom(int atomID, int valueID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO valued_atom(atom_id, value_id) VALUES (?,?)");
            preparedStatement.setInt(1, atomID);
            preparedStatement.setInt(2, valueID);

            if (preparedStatement.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static int getValued_atomID(int atomID , int valueID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT va_id FROM valued_atom WHERE atom_id = %d AND value_id = %d", atomID, valueID);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) == 0){
                return -1;
            }else {
                rs.next();
                return rs.getInt("va_id");
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;
    }


    public static Boolean insertAtom_Value(int atomID, int valueID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO atom_value(atom_id, value_id) VALUES (?,?)");
            preparedStatement.setInt(1, atomID);
            preparedStatement.setInt(2, valueID);

            if (preparedStatement.executeUpdate() > 0) {
                //good
                return true;
            } else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }
/*
    public static int getValueID(String value_Str){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT value_id FROM value WHERE value_str = %s", value_Str);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) == 0){
                return -1;
            }else {
                return rs.getInt(0);
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;
    }*/

    public static Boolean insertValued_atom(int atomID, int valueID){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO valued_atom(atom_id, value_id) VALUES (?,?)");
            preparedStatement.setInt(1, atomID);
            preparedStatement.setInt(2, valueID);

            if (preparedStatement.executeUpdate() > 0) {
                //good
                return true;
            } else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }

    }

    public static HashMap<Integer, ArrayList<Integer>> get_ant_Rule(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
//        ArrayList<RuleInAtom> antAtomArr = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> antHash = new HashMap<>();
        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = "SELECT rule_id, ant_va_id FROM rule_ant";
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) == 0){
                return null;
            }else {

                while(rs.next()){
                    int ruleId = Integer.parseInt(rs.getString("rule_id"));
                    int antVaId = Integer.parseInt(rs.getString("ant_va_id"));

                    if (antHash.keySet().contains(ruleId)) { // ruleID가 이미 존재할 경우.
                        ArrayList<Integer> vaList = antHash.get(ruleId);
                        if (!vaList.contains((Integer) antVaId)) {
                            vaList.add((Integer) antVaId);
                        }
                    } else {                                    // ruleID가 존재하지 않을 경우.
                        ArrayList<Integer> vaList = new ArrayList<>();
                        vaList.add(antVaId);
                        antHash.put(ruleId, vaList);
                    }
                }

                return antHash;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public static  HashMap<Integer, ArrayList<Integer>> get_con_Rule(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
//        ArrayList<RuleInAtom> antAtomArr = new ArrayList<>();
        HashMap<Integer, ArrayList<Integer>> conHash = new HashMap<>();

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = "SELECT rule_id, con_va_id FROM rule_con";
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) == 0){
                return null;
            }else {

                while(rs.next()){
                    int ruleId = Integer.parseInt(rs.getString("rule_id"));
                    int conVaId = Integer.parseInt(rs.getString("con_va_id"));

                    if (conHash.keySet().contains(ruleId)) { // ruleID가 이미 존재할 경우.
                        ArrayList<Integer> vaList = conHash.get(ruleId);
                        if (!vaList.contains((Integer) conVaId)) {
                            vaList.add((Integer) conVaId);
                        }
                    } else {                                    // ruleID가 존재하지 않을 경우.
                        ArrayList<Integer> vaList = new ArrayList<>();
                        vaList.add(conVaId);
                        conHash.put(ruleId, vaList);
                    }
                }

                return conHash;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

    public static int getRuleCount(){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = "SELECT DISTINCT count(rule_id) AS rulecount FROM rule_con";
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) == 0){
                return -1;
            }else {
                rs.next();
                return rs.getInt("rulecount");
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;
    }

    public static int  insertRule(ArrayList<Integer> arr, String flag) throws SQLException, ClassNotFoundException {

        String sql = null;
        if(flag.equals("con")){
            sql = "INSERT INTO rule_con(rule_id, con_va_id) VALUES (?,?)";
        }else if(flag.equals("ant")){
            sql = "INSERT INTO rule_ant(rule_id, ant_va_id) VALUES (?,?)";
        }

        Connection connection = getConn();
        PreparedStatement statement = connection.prepareStatement(sql);

        int i = 0;
        int ruleID =  getRuleCount()+1;

        for (Integer atomID : arr) {
            statement.setInt(1,ruleID);
            statement.setInt(2, atomID);

            statement.addBatch();
            i++;

            if (i % 1000 == 0 || i == arr.size()) {
                statement.executeBatch(); // Execute every 1000 items.
            }
        }

        return ruleID;

    }

    public static int getResultSetSize(ResultSet resultSet) {
        int size = -1;

        try {
            resultSet.last();
            size = resultSet.getRow();
            resultSet.beforeFirst();
        } catch(SQLException e) {
            return size;
        }

        return size;
    }

    public static boolean isExistedAtom(String atomName){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT * FROM atom_default WHERE atom_name = \'%s\'", atomName);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) > 0){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return false;

    }

    public static int getAtomID(String atomName){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT atom_id FROM atom_default WHERE atom_name = \'%s\'", atomName);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) > 0){
                rs.next();
                return rs.getInt("atom_id");
            }else {
                return -1;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;

    }



    public static boolean isExistedValue(String valueName){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT value_str FROM value WHERE value_str = \'%s\'", valueName);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) > 0){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return false;

    }

    public static int getValueID(String valueName){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            String sql = String.format("SELECT value_id FROM value WHERE value_str = \'%s\'", valueName);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) > 0){
                rs.next();
                return rs.getInt("value_id");
            }else {
                return -1;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;

    }

    public static int insertAtom_Default(String atomName, int atomType){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO atom_default(atom_name, atom_type) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, atomName);
            preparedStatement.setInt(2, atomType);

            if (preparedStatement.executeUpdate() > 0) {
                //good
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            } else {
                return -1;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;
    }

    public static int insertValue(String valueName){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO value( value_str) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, valueName);

            if (preparedStatement.executeUpdate() > 0) {
                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            } else {
                return -1;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return -1;
    }

    public static boolean isExistedValuedAtom(int atomID, int valueID){

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();


            String sql = String.format("SELECT va_id FROM valued_atom WHERE atom_id = %s AND value_id = \'%s\'", atomID, valueID);
            rs = stmt.executeQuery(sql);
            if (getResultSetSize(rs) > 0){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return false;

    }


    public static Boolean insertPatientOpinionRule( int patientID, int opinionID, int ruleID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO patient_opinion_rule(id, opinion_id, rule_id ) VALUES (?,?,?)");

            preparedStatement.setInt(1, patientID);
            preparedStatement.setInt(2, opinionID);
            preparedStatement.setInt(3, ruleID);

            if (preparedStatement.executeUpdate() > 0) {
                //good
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static Boolean insertRuleUser(int ruleID, int authorID) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = getConn();

            System.out.println("Creating statement...");
            stmt = conn.createStatement();

            preparedStatement = conn.prepareStatement("INSERT INTO rule_user(rule_id, user_id, created_date, modified_date ) VALUES (?,?,?,?)");

            preparedStatement.setInt(1, ruleID);
            preparedStatement.setInt(2, authorID);
            java.sql.Timestamp  sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
            preparedStatement.setTimestamp(3, sqlDate);
            preparedStatement.setTimestamp(4, sqlDate);

            if (preparedStatement.executeUpdate() > 0) {
                //good
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    public static Boolean deleteRule(String patientID ,String opinionID, String ruleID) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement preparedStatement = null;


        conn = getConn();

        //STEP 4: Execute a query
        System.out.println("Creating statement...");
        stmt = conn.createStatement();

        preparedStatement = conn.prepareStatement("DELETE FROM patient_opinion_rule WHERE id = ? AND opinion_id = ? AND rule_id = ?");
        preparedStatement.setInt(1, Integer.parseInt(patientID));
        preparedStatement.setInt(2, Integer.parseInt(opinionID));
        preparedStatement.setInt(3, Integer.parseInt(ruleID));

        if (preparedStatement.executeUpdate() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
