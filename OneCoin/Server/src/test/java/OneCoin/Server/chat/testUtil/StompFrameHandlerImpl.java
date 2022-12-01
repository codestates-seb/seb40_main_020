package OneCoin.Server.chat.testUtil;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class StompFrameHandlerImpl<T> implements StompFrameHandler {

    private final T response;
    private final BlockingQueue<T> responses;

    public StompFrameHandlerImpl(final T response, final BlockingQueue<T> responses) {
        this.response = response;
        this.responses = responses;
    }

    @Override
    public Type getPayloadType(final StompHeaders headers) {
        return response.getClass();
    }

    @Override
    public void handleFrame(final StompHeaders headers, final Object payload) {
        System.out.println(payload);
        responses.offer((T) payload);
    }
}
