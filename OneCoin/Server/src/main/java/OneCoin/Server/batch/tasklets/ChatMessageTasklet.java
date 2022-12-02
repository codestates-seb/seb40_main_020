package OneCoin.Server.batch.tasklets;

import OneCoin.Server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageTasklet implements Tasklet {
    private final ChatService chatService;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        chatService.saveInMemoryChatMessagesToRdb();
        log.info("In-Memory ChatMessage saved to RDB");
        return RepeatStatus.FINISHED;
    }
}
