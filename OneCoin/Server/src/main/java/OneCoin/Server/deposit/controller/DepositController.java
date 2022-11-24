package OneCoin.Server.deposit.controller;

import OneCoin.Server.balance.BalanceService;
import OneCoin.Server.deposit.dto.DepositDto;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.deposit.mapper.DepositMapper;
import OneCoin.Server.deposit.service.DepositService;
import OneCoin.Server.dto.SingleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/deposits")
@Validated
@Slf4j
public class DepositController {
    private final DepositService depositService;
    private final DepositMapper depositMapper;
    private final BalanceService balanceService;

    public DepositController(DepositService depositService, DepositMapper depositMapper, BalanceService balanceService) {
        this.depositService = depositService;
        this.depositMapper = depositMapper;
        this.balanceService = balanceService;
    }

    @PostMapping
    public ResponseEntity postDeposit(@Valid @RequestBody DepositDto.Post requestBody,
                                      @AuthenticationPrincipal Map<String, Object> userInfo) {
        Deposit deposit = depositMapper.depositPostToDeposit(requestBody);
        deposit.setBalance(balanceService.findBalanceByUserId(Long.parseLong(userInfo.get("id").toString())));
        Deposit createdDeposit = depositService.createDeposit(deposit);

        return new ResponseEntity(new SingleResponseDto<>(depositMapper.depositToDepositResponse(createdDeposit)), HttpStatus.CREATED);
    }
}
