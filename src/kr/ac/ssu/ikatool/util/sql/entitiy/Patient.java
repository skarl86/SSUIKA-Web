package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;

/**
 * Created by NCri on 2015. 12. 24..
 */
public class Patient {
    private String id;
    private String name;
    private String age;
    private String sex;
    private DetailInfo detailInfo;
    private ArrayList<Opinion> opinionList;

    public Patient(String id, String name, String age, String sex, DetailInfo detailInfo, ArrayList<Opinion> opinionList) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.detailInfo = detailInfo;
        this.opinionList = opinionList;
    }

    public Patient(String id, String name, String age, String sex) {
        this(id, name, age, sex, null, null);
    }

    public String getSex() {
        return sex;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public DetailInfo getDetailInfo() {
        return detailInfo;
    }

    public ArrayList<Opinion> getOpinionList() {
        return opinionList;
    }
    public void addDetailInfo(DetailInfo info){
        this.detailInfo = info;
    }
    public void addOpinion(Opinion opn){
        this.opinionList.add(opn);
        System.out.println(this.opinionList);
    }
}
