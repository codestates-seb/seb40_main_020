package OneCoin.Server.performance;

import org.junit.jupiter.api.Test;

import java.util.List;

public class StreamTest {

    private List<Double> prices;

    @Test
    void measureSpeedTest() {
        prices = List.of(0.0, 1.0, 0.0);

        long startTime = System.nanoTime();
        int cnt = 0;
        for (Double price : prices) {
            if (price == 0) {
                cnt++;
            }
        }
        long endTime = System.nanoTime();
        System.out.printf("# for-loop: %d, zero count: %d\n", (endTime - startTime), cnt);

        startTime = System.nanoTime();
        cnt = (int) prices.stream().filter(n -> n == 0).count();
        endTime = System.nanoTime();
        System.out.printf("# stream: %d, zero count: %d\n", (endTime - startTime), cnt);
    }
}
