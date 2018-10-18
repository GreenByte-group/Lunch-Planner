package group.greenbyte.lunchplanner.user.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;



@Entity
public class Debts  implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer debtsId;

    @Column(nullable = false)
    private String creditor;

    @Column(nullable = false)
    private String debtor;

    @Column
    private Float sum;


    //------------GETTER AND SETTER-----------------------//


    public Integer getDebtsId() {
        return this.debtsId;
    }

    public void setDebtsId(Integer debtsId) {
        this.debtsId = debtsId;
    }

    public Float getSum() {
        return sum;
    }

    public void setSum(Float sum) {
        this.sum = sum;
    }

    public String getCreatorName() {
        return this.creditor;
    }

    public void setCreatorName(String creatorName) {
        this.creditor= creatorName;
    }

    public String getDebtorName() {
        return this.debtor;
    }

    public void setDebtorName(String debtorName) {
        this.debtor = debtorName;
    }

}
