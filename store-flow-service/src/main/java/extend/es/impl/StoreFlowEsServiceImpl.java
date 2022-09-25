package extend.es.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import extend.bean.StoreFlow;
import extend.dao.StoreFlowRepository;
import extend.es.StoreFlowEsService;
import extend.mapper.StoreFlowMapper;
import extend.param.SearchStoreFlowParam;
import extend.utils.StoreFlowFailEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/9/25 15:58
 */
@Slf4j
@Service
public class StoreFlowEsServiceImpl implements StoreFlowEsService {

    @Resource
    private StoreFlowMapper storeFlowMapper;

    @Resource
    private StoreFlowRepository storeFlowRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public void saveTestData() {
        IPage<StoreFlow> page = new Page<>();
        QueryWrapper<StoreFlow> queryWrapper = new QueryWrapper<>();
        IPage<StoreFlow> storeFlowIPage = storeFlowMapper.selectPage(page, queryWrapper);
        storeFlowRepository.saveAll(storeFlowIPage.getRecords());
    }

    @Override
    public List<StoreFlow> findStoreFlowById(Long id) {
        return storeFlowRepository.findStoreFlowsById(id);
    }

    /**
     * https://blog.csdn.net/m0_62866192/article/details/121608020
     * https://blog.csdn.net/robinson_911/article/details/105855099
     *
     * @param searchStoreFlowParam
     * @return
     */
    @Override
    public SearchHits<StoreFlow> searchStoreFlowByParam(SearchStoreFlowParam searchStoreFlowParam) {
        StoreFlowFailEnum checkFlag = this.checkSearchParam(searchStoreFlowParam);
        if (checkFlag != null) {
            return null;
        }
        BoolQueryBuilder boolQueryBuilder = this.builderQueryConditions(searchStoreFlowParam);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();
        return elasticsearchRestTemplate.search(searchQuery, StoreFlow.class);
    }

    /**
     * 检查必要查询参数
     *
     * @param searchStoreFlowParam
     * @return
     */
    private StoreFlowFailEnum checkSearchParam(SearchStoreFlowParam searchStoreFlowParam) {
        if (searchStoreFlowParam == null) {
            return StoreFlowFailEnum.PARAM_CANNOT_BE_EMPTY;
        }
        if (searchStoreFlowParam.getTenantId() == null) {
            return StoreFlowFailEnum.TENANT_CANNOT_BE_EMPTY;
        }
        return null;
    }

    /**
     * 组装查询器
     *
     * @param searchStoreFlowParam
     * @return
     */
    private BoolQueryBuilder builderQueryConditions(SearchStoreFlowParam searchStoreFlowParam) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("tenantId", searchStoreFlowParam.getTenantId()));
        RangeQueryBuilder rangeQueryBuilder = this.tryBuilderRangeQuery(searchStoreFlowParam);
        if (rangeQueryBuilder != null) {
            boolQueryBuilder.filter(rangeQueryBuilder);
        }
        return boolQueryBuilder;
    }

    /**
     * 尝试生成器范围查询
     *
     * @param searchStoreFlowParam
     * @return
     */
    private RangeQueryBuilder tryBuilderRangeQuery(SearchStoreFlowParam searchStoreFlowParam) {
        // 条件不满足返回null
        if (searchStoreFlowParam.getStartTime() == null && searchStoreFlowParam.getEndTime() == null) {
            return null;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime");
        ZoneOffset of = ZoneOffset.of("+8");
        if (searchStoreFlowParam.getStartTime() != null) {
            rangeQueryBuilder.from(searchStoreFlowParam.getStartTime().toInstant(of).toEpochMilli());
        }
        if (searchStoreFlowParam.getEndTime() != null) {
            rangeQueryBuilder.to(searchStoreFlowParam.getEndTime().toInstant(of).toEpochMilli());
        }
        return rangeQueryBuilder;
    }
}
