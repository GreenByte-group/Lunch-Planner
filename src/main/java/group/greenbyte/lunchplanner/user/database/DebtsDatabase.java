package group.greenbyte.lunchplanner.user.database;

public class DebtsDatabase {

    private int debsID;
    private String creditor;
    private String debtor;
    private Float sum;

    public Debts getDebts(){
        Debts debts = new Debts();
        debts.setDebtsId(this.debsID);
        debts.setCreatorName(this.creditor);
        debts.setDebtorName(this.debtor);
        debts.setSum(this.sum);

        return debts;
    }

    public void setDebsID(int debsID) {
        this.debsID = debsID;
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
}
