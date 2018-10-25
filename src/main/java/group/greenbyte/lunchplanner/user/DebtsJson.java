package group.greenbyte.lunchplanner.user;

import java.io.Serializable;

public class DebtsJson implements Serializable{


    private static final long serialVersionUID = 465186153151351686L;


    private String creditor;
    private String debtor;
    private Float sum;


    public DebtsJson(){ }

    public DebtsJson(String creditor, String debtor, Float sum){
        this.creditor = creditor;
        this.debtor = debtor;
        this.sum = sum;
    }


    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public String getDebtor() {
        return debtor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }
}
