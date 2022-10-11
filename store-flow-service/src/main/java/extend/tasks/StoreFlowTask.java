package extend.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 13:21
 */
@Slf4j
@Component
public class StoreFlowTask {

    @Resource
    private LargeDataScreenTaskService largeDataScreenTaskService;

    /**
     * 更新节假日配置
     */
    @Scheduled(cron = "0 * * * * ?")
    public void updateHolidayConfig() {

        largeDataScreenTaskService.updateHolidayConfig(LocalDateTime.now());
    }

    /**
     * 店铺客流排名
     */
    @Scheduled(cron = "0 * * * * ?")
    public void customerFlowRankingOfStoresByTenant() {

        largeDataScreenTaskService.customerFlowRankingOfStoresByTenant(LocalDateTime.now());

    }




}
