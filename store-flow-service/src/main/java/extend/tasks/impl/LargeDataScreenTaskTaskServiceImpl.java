package extend.tasks.impl;

import extend.tasks.FestivalConfigService;
import extend.tasks.FlowRankingOfStoresService;
import extend.tasks.LargeDataScreenTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class LargeDataScreenTaskTaskServiceImpl implements LargeDataScreenTaskService {

    @Resource
    private FestivalConfigService festivalConfigService;

    @Resource
    private FlowRankingOfStoresService flowRankingOfStoresService;

    @Override
    public void updateHolidayConfig(LocalDateTime taskStartTime) {
        // TODO 待补充分布式锁，重试机制
        festivalConfigService.syncHolidayConfig(taskStartTime);
    }

    @Override
    public void customerFlowRankingOfStoresByTenant(LocalDateTime taskStartTime) {
        // TODO 待补充分布式锁，重试机制
        flowRankingOfStoresService.customerFlowRankingOfStoresByTenant(taskStartTime);
    }

}
