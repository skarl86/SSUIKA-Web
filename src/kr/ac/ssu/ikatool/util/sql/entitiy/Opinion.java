package kr.ac.ssu.ikatool.util.sql.entitiy;

/**
 * Created by NCri on 2015. 12. 24..
 */
public class Opinion {
    private String opinionID;
    private String opinionContents;

    public Opinion(String opinionID, String opinionContents) {
        this.opinionID = opinionID;
        this.opinionContents = opinionContents;
    }

    public String getOpinionID() {
        return opinionID;
    }

    public void setOpinionID(String opinionID) {
        this.opinionID = opinionID;
    }

    public String getOpinionContents() {
        return opinionContents;
    }

    public void setOpinionContents(String opinionContents) {
        this.opinionContents = opinionContents;
    }
}
