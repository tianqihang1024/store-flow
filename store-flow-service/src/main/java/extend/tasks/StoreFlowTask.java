package extend.tasks;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import extend.bean.Store;
import extend.bean.StoreFlow;
import extend.dao.StoreFlowRepository;
import extend.mapper.StoreFlowAgeMapper;
import extend.mapper.StoreFlowMapper;
import extend.mapper.StoreFlowSexMapper;
import extend.mapper.StoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    private static final int TASK_EXECUTION_CYCLE = 730;

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final AtomicInteger initIndex = new AtomicInteger(0);

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private StoreFlowMapper storeFlowMapper;

    @Resource
    private StoreFlowAgeMapper storeFlowAgeMapper;

    @Resource
    private StoreFlowSexMapper storeFlowSexMapper;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private StoreFlowRepository storeFlowRepository;

    @Async("commonPool")
    public void storeFlowSave(long value) {
        List<Store> storeList = storeMapper.selectList(new LambdaQueryWrapper<>());

        storeList.forEach(store -> {
            for (long i = value == 0L ? 1 : value; i < TASK_EXECUTION_CYCLE; i++) {

                try {
                    TimeUnit.SECONDS.sleep(0);
                } catch (Exception e) {
                    log.info("安全区创建失败");
                }

                LocalDateTime now = LocalDateTime.now().minusDays(i);
                LocalDateTime startTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 10, 10);
                LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 22, 0);
                for (LocalDateTime copy = startTime; copy.compareTo(endTime) <= 0; copy = copy.plusMinutes(10)) {
                    try {
                        long flowCount = RANDOM.nextLong(1000);
                        StoreFlow storeFlow = StoreFlow.builder()
                                .tenantId(store.getTenantId())
                                .storeId(store.getStoreId())
                                .flowCount(flowCount)
                                .valid(0)
                                .createTime(copy)
                                .updateTime(copy)
                                .build();
                        storeFlowMapper.insert(storeFlow);
                    } catch (Exception e) {
                        log.info("店铺-{} 新增失败时间-{} e-{}", store, copy, e);
                    }
                }
                log.info("店铺-{} 时间-{} 新增记录成功", store, now);
            }
            log.info("店铺-{} 新增记录成功", store);
        });
    }

    //    @Scheduled(cron = "0 * * * * ?")
    @Async("commonPool")
    public void syncDataEs(int value) {

        if (value != 0) {
            initIndex.set(value);
        }

        log.info("当前线程为：{}", Thread.currentThread().getName());

        for (; true; ) {

            try {
                TimeUnit.SECONDS.sleep(0);
            } catch (Exception e) {
                log.info("安全区创建失败");
            }

            Page<StoreFlow> producePage = new Page<>(initIndex.incrementAndGet(), 10);
            LambdaQueryWrapper<StoreFlow> queryWrapper = new LambdaQueryWrapper<>();
            IPage<StoreFlow> storeFlowIPage = storeFlowMapper.selectPage(producePage, queryWrapper);
            List<StoreFlow> records = storeFlowIPage.getRecords();

            if (CollectionUtils.isEmpty(records)) {
                log.info("数据同步完成，终止程序");
                return;
            }
            try {
                storeFlowRepository.saveAll(records);
                long count = storeFlowRepository.count();
                log.info("以同步的数据量为：{}", count);
            } catch (Exception e) {
                log.info("同步数据到 es 时发送错误：", e);
            }
        }
    }


}
