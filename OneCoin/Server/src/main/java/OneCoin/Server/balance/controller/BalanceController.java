package OneCoin.Server.balance.controller;

import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.balance.mapper.BalanceMapper;
import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.dto.SingleResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/balances")
@Validated
@Slf4j
public class BalanceController {
    private final BalanceService balanceService;
    private final BalanceMapper balanceMapper;

    public BalanceController(BalanceService balanceService, BalanceMapper balanceMapper) {
        this.balanceService = balanceService;
        this.balanceMapper = balanceMapper;
    }

    @GetMapping
    public ResponseEntity getBalance(@AuthenticationPrincipal Map<String, Object> userInfo) {
        Balance balance = balanceService.findBalanceByUserId(Long.parseLong(userInfo.get("id").toString()));

        return new ResponseEntity(
                new SingleResponseDto<>(balanceMapper.balanceToBalanceResponse(balance)), HttpStatus.OK
        );
    }
}
