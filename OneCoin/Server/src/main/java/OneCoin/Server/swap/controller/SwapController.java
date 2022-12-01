package OneCoin.Server.swap.controller;

import OneCoin.Server.swap.mapper.SwapMapper;
import OneCoin.Server.swap.service.SwapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/swaps")
@Validated
@Slf4j
public class SwapController {
    private final SwapService swapService;
    private final SwapMapper swapMapper;

    public SwapController(SwapService swapService, SwapMapper swapMapper) {
        this.swapService = swapService;
        this.swapMapper = swapMapper;
    }

    @GetMapping("/calculates")
    public ResponseEntity getExchangeRate(@Valid @RequestParam String givenToken,
                                          @Valid @RequestParam String takenToken,
                                          @Valid @RequestParam String amount) {
        return new ResponseEntity(swapMapper.exchangeRateToSwapExchangeRate(swapService.calculateExchangeRate(givenToken, takenToken, new BigDecimal(amount))), HttpStatus.OK);
    }

}
