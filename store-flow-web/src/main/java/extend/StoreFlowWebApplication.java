package extend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/19 22:24
 */
@EnableScheduling
@EnableFeignClients
//@EnableAsync(proxyTargetClass=true)
@SpringBootApplication
public class StoreFlowWebApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(StoreFlowWebApplication.class, args);
    }

}
