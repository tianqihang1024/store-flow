package extend.tasks;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import extend.bean.Store;
import extend.bean.StoreFlow;
import extend.mapper.StoreFlowAgeMapper;
import extend.mapper.StoreFlowMapper;
import extend.mapper.StoreFlowSexMapper;
import extend.mapper.StoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 13:21
 */
@Slf4j
@Component
public class StoreFlowTask {

    /**
     * 任务执行周期
     */
    private static final int TASK_EXECUTION_CYCLE = 365;

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    @Resource
    private StoreFlowMapper storeFlowMapper;

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private StoreFlowAgeMapper storeFlowAgeMapper;

    @Resource
    private StoreFlowSexMapper storeFlowSexMapper;

    public void storeFlowSave() {
        List<Store> storeList = storeMapper.selectList(new LambdaQueryWrapper<>());

        storeList.parallelStream().forEach(store -> {
            for (int i = 0; i < TASK_EXECUTION_CYCLE; i++) {
                LocalDateTime now = LocalDateTime.now().minusDays(i);
                LocalDateTime startTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 10, 10);
                LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 22, 0);
                for (LocalDateTime copy = startTime; copy.compareTo(endTime) <= 0; copy = copy.plusMinutes(10)) {
                    long flowCount = RANDOM.nextLong(1000);
                    StoreFlow storeFlow = StoreFlow.builder()
                            .tenantId(store.getTenantId())
                            .storeId(store.getStoreId())
                            .flowCount(flowCount)
                            .valid(0)
                            .created(copy)
                            .updated(copy)
                            .build();
                    storeFlowMapper.insert(storeFlow);
                }
            }
        });
    }

}
