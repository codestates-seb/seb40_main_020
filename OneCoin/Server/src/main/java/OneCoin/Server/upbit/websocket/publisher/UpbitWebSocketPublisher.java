package OneCoin.Server.upbit.websocket.publisher;

import OneCoin.Server.dto.SingleResponseDto;
import OneCoin.Server.upbit.dto.MarketDto;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.repository.OrderBookRepository;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.upbit.service.UpbitHandlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpbitWebSocketPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    private final TickerRepository tickerRepository;
    private final OrderBookRepository orderBookRepository;

    @Scheduled(fixedDelay = 1000)
    public void send() {
        List<TickerDto> tickerDto = tickerRepository.findTickers();
        List<OrderBookDto> orderBookDto = orderBookRepository.findOrderBooks();
        MarketDto marketDto = new MarketDto(tickerDto, orderBookDto);
        messagingTemplate.convertAndSend("/info/upbit", new SingleResponseDto<>(marketDto));
    }
}
