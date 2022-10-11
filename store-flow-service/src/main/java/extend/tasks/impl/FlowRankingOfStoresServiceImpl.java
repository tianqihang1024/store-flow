package extend.tasks.impl;

import extend.bean.LargeDataScreen;
import extend.bean.StoreFlow;
import extend.mapper.StoreFlowMapper;
import extend.mapper.StoreMapper;
import extend.service.LargeDataScreenService;
import extend.tasks.FlowRankingOfStoresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlowRankingOfStoresServiceImpl implements FlowRankingOfStoresService {

    /**
     * 最大百分比
     */
    private static final BigDecimal PERCENTAGE_MAX = new BigDecimal(100);

    @Resource
    private StoreMapper storeMapper;

    @Resource
    private StoreFlowMapper storeFlowMapper;

    @Resource
    private LargeDataScreenService largeDataScreenService;

    @Override
    public void customerFlowRankingOfStoresByTenant(LocalDateTime taskStartTime) {

        LocalDateTime startTime = taskStartTime.minusDays(1L).with(LocalTime.MIN);
        LocalDateTime endTime = startTime.with(LocalTime.MAX);

        // 获取有效的租户列表
        List<String> tenantIds = storeMapper.getAValidTenantList(endTime);

        Iterator<String> iterator = tenantIds.iterator();
        while (iterator.hasNext()) {

            String tenantId = iterator.next();

            Map<String, Integer> storeFlowMap = getStoreFlowMap(tenantId, startTime, endTime);

            List<Map.Entry<String, Integer>> storeRankingList = builderStoreRankingList(storeFlowMap);

            List<LargeDataScreen> largeDataScreenList = builderRankingInfoList(storeRankingList);

            try {
                largeDataScreenService.insertBatchSomeColumn(largeDataScreenList);
            } catch (Exception e) {
                log.error("门店排行创建失败，请手动维护");
            }

            iterator.remove();
        }
    }


    /**
     * 获取店铺客流map
     *
     * @param tenantId  租户id
     * @param startTime
     * @param endTime
     * @return key：storeId value：flowCount
     */
    private Map<String, Integer> getStoreFlowMap(String tenantId, LocalDateTime startTime, LocalDateTime endTime) {

        List<StoreFlow> storeFlowList = storeFlowMapper.getStoreFlowByTenantId(tenantId, startTime, endTime);

        Map<String, List<StoreFlow>> collect = storeFlowList.stream().collect(Collectors.groupingBy(StoreFlow::getStoreId));

        return collect.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().mapToInt(StoreFlow::getFlowCount).sum()));
    }

    /**
     * 生成门店客流排行榜
     *
     * @param storeFlowMap 门店客流map
     * @return
     */
    private List<Map.Entry<String, Integer>> builderStoreRankingList(Map<String, Integer> storeFlowMap) {

        List<Map.Entry<String, Integer>> storeRankingList = new ArrayList<>(storeFlowMap.entrySet());

        storeRankingList.sort((o1, o2) -> {
            if (null == o1.getValue()) {
                return 1;
            }
            if (null == o2.getValue()) {
                return -1;
            }
            int i = o1.getValue().compareTo(o2.getValue());
            o1.setValue(null);
            return i;
        });

        return storeRankingList;
    }

    /**
     * 计算门店排名信息
     *
     * @param storeRankingList 店铺排名帮
     * @return 待插入的数据大屏对象
     */
    private List<LargeDataScreen> builderRankingInfoList(List<Map.Entry<String, Integer>> storeRankingList) {

        int ranking = 0;

        BigDecimal storeSize = BigDecimal.valueOf(storeRankingList.size());

        List<LargeDataScreen> largeDataScreenList = new ArrayList<>(storeRankingList.size());

        for (Map.Entry<String, Integer> storeRanking : storeRankingList) {

            BigDecimal divide = storeSize.subtract(BigDecimal.valueOf(ranking)).divide(PERCENTAGE_MAX, 2, RoundingMode.HALF_UP);

            LargeDataScreen largeDataScreen = LargeDataScreen.builder()
                    .storeId(storeRanking.getKey())
                    .flowRankingToday(++ranking)
                    .flowRankingPercentageToday(divide)
                    .build();
            largeDataScreenList.add(largeDataScreen);
        }
        return largeDataScreenList;
    }

}
