package extend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/14 0:07
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "commonPool")
    public ExecutorService commonPool() {
        return new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()
                , Runtime.getRuntime().availableProcessors() * 2
                , 60
                , TimeUnit.SECONDS
                , new ArrayBlockingQueue<>(1000)
                , r -> new Thread(r, "common_pool_" + r.hashCode())
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
