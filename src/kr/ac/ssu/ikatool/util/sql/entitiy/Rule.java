package kr.ac.ssu.ikatool.util.sql.entitiy;

import java.util.ArrayList;

/**
 * Created by NCri on 2015. 12. 28..
 */
public class Rule {
    private Integer id;
    private ArrayList<Atom> antecedents;
    private ArrayList<Atom> conseqeunts;
    private String author;
    private String createDate;
    private String modifyDate;
    private String formalString;

    public Rule(Integer id) {
        this.id = id;
    }

    public Rule(Integer id, String author, String createDate, String modifyDate) {
        this.id = id;
        this.author = author;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public void addAntecedents(Atom atom){
        if (antecedents == null) antecedents = new ArrayList<Atom>();
        antecedents.add(atom);
    }

    public ArrayList<Atom> getAntecedents() {
        return antecedents;
    }

    public void addCoseqeunts(Atom atom){
        if (conseqeunts == null) conseqeunts = new ArrayList<Atom>();
        conseqeunts.add(atom);
    }

    private ArrayList<Integer> generateAtomIDs(ArrayList<Atom> atoms){
        ArrayList<Integer> ids = new ArrayList<Integer>();

        for(Atom atom : atoms){
            ids.add(atom.getId());
        }
        return ids;
    }
    public Atom getAntecedentByID(Integer id){
        for(Atom atom : antecedents){
            if(atom.getId() == id)
                return atom;
        }
        return null;
    }
    public ArrayList<Integer> getAntecedentAtomIDs(){
        return generateAtomIDs(antecedents);
    }

    public ArrayList<Integer> getConsequentAtomIDs(){
        return generateAtomIDs(conseqeunts);
    }

    public ArrayList<Atom> getConseqeunts() {
        return conseqeunts;
    }

    public Integer getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public String getFormalString() {
        return formalString;
    }

    public String toString(){
        return String.format("Antecedent : %s // Consequent : %s", this.antecedents, this.conseqeunts);
    }
}
