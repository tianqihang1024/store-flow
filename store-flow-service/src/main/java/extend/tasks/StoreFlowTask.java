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
import java.util.ArrayList;
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
        log.info("当前线程为：{}", Thread.currentThread().getName());

        for (; true; ) {

            LambdaQueryWrapper<Store> queryWrapper = new LambdaQueryWrapper<>();
            Page<Store> producePage = new Page<>(++value, 1);
            IPage<Store> storeFlowIPage = storeMapper.selectPage(producePage, queryWrapper);
            List<Store> records = storeFlowIPage.getRecords();

            if (CollectionUtils.isEmpty(records)) {
                log.info("初始化客流数据完成，终止程序");
                return;
            }
            Store store = records.get(0);

            LambdaQueryWrapper<StoreFlow> storeFlowQueryWrapper = new LambdaQueryWrapper<>();
            storeFlowQueryWrapper.eq(StoreFlow::getStoreId, store.getStoreId());
            Integer integer = storeFlowMapper.selectCount(storeFlowQueryWrapper);
            if (integer != null && integer > 0) {
                log.info("店铺数据已经初始化 store-{} count-{}", store, integer);
                continue;
            }

            for (long i = 1; i < TASK_EXECUTION_CYCLE; i++) {

                try {
                    TimeUnit.SECONDS.sleep(0);
                } catch (Exception e) {
                    log.info("安全区创建失败");
                }

                LocalDateTime now = LocalDateTime.now().minusDays(i);
                LocalDateTime startTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 10, 10);
                LocalDateTime endTime = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 22, 0);

                List<StoreFlow> list = new ArrayList<>(75);
                for (LocalDateTime copy = startTime; copy.compareTo(endTime) <= 0; copy = copy.plusMinutes(10)) {
                    long flowCount = RANDOM.nextLong(1000);
                    StoreFlow storeFlow = StoreFlow.builder()
                            .tenantId(store.getTenantId())
                            .storeId(store.getStoreId())
                            .flowCount(flowCount)
                            .valid(0)
                            .createTime(copy)
                            .updateTime(copy)
                            .build();
                    list.add(storeFlow);
                }
                try {
                    int i1 = storeFlowMapper.insertBatchSomeColumn(list);
                    log.info("店铺-{} 时间-{} 待新增数-{} 新增数-{} 新增记录成功", store, list.size(), i1, now);
                } catch (Exception e) {
                    log.info("店铺-{} e-{}", store, e);
                }
            }
            log.info("店铺-{} 新增记录成功", store);
            ;
        }

    }

    /**
     *
     * @param minId 入参需要比实际数小100，达到兼容作用
     */
    @Async(value = "commonPool")
    public void syncDataEs(long minId) {

        log.info("当前线程为：{}", Thread.currentThread().getName());
         minId = minId != 0L ? minId : storeFlowMapper.selectMinId() - 100;

        for (; true; ) {

            try {
                TimeUnit.SECONDS.sleep(0);
            } catch (Exception e) {
                log.info("安全区创建失败");
            }
            minId = minId + 100;

            List<StoreFlow> records = storeFlowMapper.selectLimit(minId);

            try {
                if (! CollectionUtils.isEmpty(records)) {
                    storeFlowRepository.saveAll(records);
                } else {
                    long count = storeFlowRepository.count();
                    if (count == 47240784) {
                        log.info("数据同步完成，终止程序");
                        return;
                    }
                }
            } catch (Exception e) {
                log.info("同步数据到 es 时发送错误：", e);
            }
        }
    }


}
