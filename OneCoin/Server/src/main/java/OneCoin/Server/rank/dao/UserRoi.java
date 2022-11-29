package OneCoin.Server.rank.dao;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Setter
@Getter
public class UserRoi implements Comparable<UserRoi> {
    private Long userId;
    private String userDisplayName;
    private double sumOfBids = 0.0;
    private double sumOfAsks = 0.0;
    private double sumOfCurrentCoinValues = 0.0;
    private double totalRoi = 0.0;

    public UserRoi(Long userId, String userDisplayName, BigDecimal sumOfAsks) {
        this.userId = userId;
        this.userDisplayName = userDisplayName;
        this.sumOfBids = sumOfAsks.doubleValue();
    }

    public UserRoi(Long userId, BigDecimal sumOfBids) {
        this.userId = userId;
        this.sumOfAsks = sumOfBids.doubleValue();
    }

    public void addSumOfCurrentCoinValues(double coinValue){
        this.sumOfCurrentCoinValues += coinValue;
    }

    public void calculate() {
        this.totalRoi = (this.sumOfAsks + this.sumOfCurrentCoinValues - this.sumOfBids) / this.sumOfBids;
    }

    @Override
    public int compareTo(@NotNull UserRoi o) {
        if(this.totalRoi < o.totalRoi) return 1;
        if(this.totalRoi > o.totalRoi) return -1;
        return 0;
    }
}
