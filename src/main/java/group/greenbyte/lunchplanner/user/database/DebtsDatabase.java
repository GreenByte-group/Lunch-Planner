package group.greenbyte.lunchplanner.user.database;

public class DebtsDatabase {

    private Integer debtsId;
    private String creditor;
    private String debtor;
    private Float sum;

    public Debts getDebts(){
        Debts debts = new Debts();

        debts.setCreditor(this.creditor);
        debts.setDebtor(this.debtor);
        debts.setSum(this.sum);
        debts.setDebtsId(this.debtsId);
        System.out.println("ALLE WICHTIGEN DATEN DER DATABASE:\nid: "+debtsId+"\ncreditor: "+creditor+"\ndebtor:"+debtor+"\nsum: "+sum);
        return debts;
    }


    public void setCreditor(String creditor) {
        this.creditor = creditor;
    }

    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public void setDebtsId(Integer debtsId) { this.debtsId = debtsId; }

    public String getCreditor() {
        return creditor;
    }

    public String getDebtor() {
        return debtor;
    }

    public Float getSum() {
        return sum;
    }

    public Integer getDebtsId() {return this.debtsId; }
}
