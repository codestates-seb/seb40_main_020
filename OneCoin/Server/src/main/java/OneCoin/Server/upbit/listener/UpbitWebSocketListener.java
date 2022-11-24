package OneCoin.Server.upbit.listener;

import OneCoin.Server.upbit.entity.enums.SiseType;
import OneCoin.Server.upbit.service.UpbitHandlingService;
import OneCoin.Server.upbit.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UpbitWebSocketListener extends WebSocketListener {

    private final JsonUtil jsonUtil;
    private final UpbitHandlingService upbitHandlingService;
    private String parameter;

    public String getParameter() {
        return parameter;
    }

    public void setParameter(List<String> codes) {
        this.parameter = jsonUtil.toJson(
                List.of(new Ticket("OneCoinProject"),
                        new Type(SiseType.TICKER.getType(), codes),
                        new Type(SiseType.ORDER_BOOK.getType(), codes))
        );
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("Socket Closed: {}, {}", code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.warn("Socket Closing: {}, {}", code, reason);

    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        log.error("Socket Error: {}", t.getMessage());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString text) {
        JsonNode jsonNode = jsonUtil.fromJson(text.string(StandardCharsets.UTF_8), JsonNode.class);
        upbitHandlingService.parsing(jsonNode);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        webSocket.send(getParameter());
    }

    @AllArgsConstructor
    private static class Ticket {
        private String ticket;
    }

    @AllArgsConstructor
    private static class Type {
        private String type;
        private List<String> codes;
    }
}
