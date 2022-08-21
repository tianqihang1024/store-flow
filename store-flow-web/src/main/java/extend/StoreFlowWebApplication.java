package extend;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(value = "extend.mapper")
@SpringBootApplication
public class StoreFlowWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreFlowWebApplication.class, args);
    }

}
