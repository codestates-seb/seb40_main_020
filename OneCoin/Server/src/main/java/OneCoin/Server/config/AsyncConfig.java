package OneCoin.Server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor upbitExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // 기본 쓰레드 수
        executor.setMaxPoolSize(10); // 최대 쓰레드 수
        executor.setQueueCapacity(30); // Queue 사이즈
        executor.setThreadNamePrefix("OneCoin-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor sendEmailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("SendEmail-");
        executor.initialize();
        return executor;
    }
}
