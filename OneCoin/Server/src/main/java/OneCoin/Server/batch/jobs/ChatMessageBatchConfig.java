package OneCoin.Server.batch.jobs;

import OneCoin.Server.batch.tasklets.ChatMessageTasklet;
import OneCoin.Server.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ChatMessageBatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ChatService chatService;

    @Bean
    public Job saveChatMessageJob() {
        return jobBuilderFactory.get("saveChatMessageJob")
                .start(saveChatMessageStep())
                .build();
    }

    @Bean
    public Step saveChatMessageStep() {
        return stepBuilderFactory.get("saveChatMessageStep")
                .tasklet(new ChatMessageTasklet(chatService))
                .build();
    }
}
