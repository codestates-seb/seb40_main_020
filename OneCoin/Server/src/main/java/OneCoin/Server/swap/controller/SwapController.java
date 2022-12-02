package OneCoin.Server.swap.controller;

import OneCoin.Server.dto.PageResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import OneCoin.Server.swap.dto.SwapDto;
import OneCoin.Server.swap.entity.Swap;
import OneCoin.Server.swap.mapper.SwapMapper;
import OneCoin.Server.swap.service.SwapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public ResponseEntity postSwap(@Valid @RequestBody SwapDto.Post requestBody,
                                   @AuthenticationPrincipal Map<String, Object> userInfo) {
        Swap swap = swapService.createSwap(swapMapper.swapPostToSwap(requestBody), Long.parseLong(userInfo.get("id").toString()));

        return new ResponseEntity(
                new SingleResponseDto<>(swapMapper.swapToSwapResponseDto(swap)), HttpStatus.CREATED
        );
    }

    @GetMapping("/calculates")
    public ResponseEntity getExchangeRate(@Valid @RequestParam String givenToken,
                                          @Valid @RequestParam String takenToken,
                                          @Valid @RequestParam String amount) {
        return new ResponseEntity(swapMapper.exchangeRateToSwapExchangeRate(swapService.calculateExchangeRate(givenToken, takenToken, new BigDecimal(amount))), HttpStatus.OK);
    }

    @GetMapping("/swap-history")
    public ResponseEntity getSwaps(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size,
                                   @AuthenticationPrincipal Map<String, Object> userInfo) {
        Page<Swap> swapPage = swapService.findSwaps(page-1, size);
        List<Swap> swaps = swapPage.getContent();
        return new ResponseEntity(
                new PageResponseDto<>(swapMapper.swapsToSwapResponses(swaps), swapPage), HttpStatus.OK
        );
    }

}
