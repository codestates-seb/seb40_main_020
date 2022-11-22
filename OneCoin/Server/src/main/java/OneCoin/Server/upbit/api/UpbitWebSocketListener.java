package OneCoin.Server.upbit.api;

import OneCoin.Server.upbit.entity.SiseType;
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

    private String parameter;
    private final JsonUtil jsonUtil;

    public String getParameter() {
        return parameter;
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        System.out.println(code + " " + reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        System.out.println(code + " " + reason);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        log.info(t.getMessage());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString text) {
        String result = jsonUtil.fromJson(text.string(StandardCharsets.UTF_8), JsonNode.class).toPrettyString();
//        System.out.println(result);
        // TODO mapper
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        webSocket.send(getParameter());
    }

    public void setParameter(SiseType siseType, List<String> codes) {
        this.parameter = jsonUtil.toJson(List.of(new Ticket("test"), new Type(siseType.getType(), codes)));
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
