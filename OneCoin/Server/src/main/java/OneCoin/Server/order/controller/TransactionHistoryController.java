package OneCoin.Server.order.controller;

import OneCoin.Server.dto.PageResponseDto;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/order/completion")
@RequiredArgsConstructor
@Validated
public class TransactionHistoryController {

    private final int size = 10;
    private final TransactionHistoryService transactionHistoryService;

    @GetMapping
    public ResponseEntity getTransactionHistoryPagination(@RequestParam(name = "period", required = false, defaultValue = "w") String period,
                                                          @RequestParam(name = "type", required = false, defaultValue = "ALL") String type,
                                                          @RequestParam(name = "code", required = false) String code,
                                                          @Positive @RequestParam(name = "page", required = false, defaultValue = "1") int page) {

        Page<TransactionHistory> transactionHistoryPage = transactionHistoryService.findTransactionHistory(period, type, code, page - 1, size);
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();
        return new ResponseEntity(new PageResponseDto<>(transactionHistories, transactionHistoryPage), HttpStatus.OK);
    }
}
