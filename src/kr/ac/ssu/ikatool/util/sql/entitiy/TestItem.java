package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 28..
 */
public class TestItem {
    private Integer subCode;
    private String name;
    private Float numValue;
    private String strValue;

    public TestItem(Integer subCode, String name, Float numValue, String strValue) {
        this.subCode = subCode;
        this.name = name;
        this.numValue = numValue;
        this.strValue = strValue;
    }

    public Integer getSubCode() {
        return subCode;
    }

    public void setSubCode(Integer subCode) {
        this.subCode = subCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getNumValue() {
        return numValue;
    }

    public void setNumValue(Float numValue) {
        this.numValue = numValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }
}
