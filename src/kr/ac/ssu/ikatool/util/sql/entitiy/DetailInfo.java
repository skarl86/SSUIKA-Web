package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by NCri on 2015. 12. 24..
 */
public class DetailInfo {
    private Integer id;
    private Integer testItemCode;
    private Integer subCode;
    private Map<Integer, ArrayList<TestItem>> map = new HashMap<Integer, ArrayList<TestItem>>();
    /**
     *
     * @param id
     * @param testItemCode
     * @param subCode
     * @param testItemName
     * @param numValue
     * @param strValue
     */
    public DetailInfo(Integer id, Integer testItemCode, Integer subCode, String testItemName, Float numValue, String strValue) {
        if(testItemName == null) testItemName = "";
        if(strValue == null) strValue = "";

        this.id = id;
        this.testItemCode = testItemCode;
        this.subCode = subCode;
        ArrayList<TestItem> list = new ArrayList<TestItem>();
        list.add(new TestItem(subCode, testItemName, numValue, strValue));
        this.map.put(testItemCode, list);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubCode() {
        return subCode;
    }

    public void setSubCode(Integer subCode) {
        this.subCode = subCode;
    }

    public void addItem(Integer testItemCode, Integer subCode, String testItemName, Float numValue, String strValue){
        ArrayList<TestItem> list = this.map.get(testItemCode);
        if(list == null)
            list = new ArrayList<TestItem>();

        if(testItemName == null) testItemName = "";
        if(strValue == null) strValue = "";
        list.add(new TestItem(subCode, testItemName, numValue, strValue));
        this.map.put(testItemCode, list);
    }
    public Set<Integer> getTestItemCodes(){
        return this.map.keySet();
    }
    public ArrayList<TestItem> getItems(Integer testItemCode){
        return this.map.get(testItemCode);
    }
}
