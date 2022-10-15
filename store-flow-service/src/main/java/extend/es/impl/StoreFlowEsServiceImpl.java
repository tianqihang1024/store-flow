package extend.es.impl;

import com.alibaba.fastjson.JSON;
import extend.bean.StoreFlow;
import extend.dao.StoreFlowRepository;
import extend.dto.UpdateStoreFlowDTO;
import extend.es.StoreFlowEsService;
import extend.param.SearchStoreFlowParam;
import extend.param.UpdateStoreFlowParam;
import extend.utils.StoreFlowFailEnum;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private static final ZoneOffset OF = ZoneOffset.of("+8");

    @Resource
    private StoreFlowRepository storeFlowRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 主键对应的记录，查询不到返回null
     */
    @Override
    public List<StoreFlow> findStoreFlowById(Long id) {
        return storeFlowRepository.findStoreFlowsById(id);
    }

    /**
     * 根据租户、创建时间查询
     *
     * @param searchStoreFlowParam 查询条件
     * @return 条件对应的记录
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
     * 修改客流数据
     * 说一下这里为什么需要对事件进行处理，ES 本身提供的 Repository 方式是没有 update 操作的
     * 这里需要借助 elasticsearchRestTemplate.update 来完成修改操作，
     * 而他的 update 与 Repository 的 save 在对时间的处理上不同，这里将 update 是配成 save
     * update：2022-02-10T22:22:00 时间的字符串
     * save：'123456787' 时间的字符串时间戳
     *
     * @param storeFlowParam 待修改的客流数据
     * @return 修改 or 插入 执行结果
     */
    @Override
    public UpdateResponse updateStoreFlow(UpdateStoreFlowParam storeFlowParam) {

        // 请求参数转换为 DTO 以便插入 ES
        UpdateStoreFlowDTO storeFlowDTO = new UpdateStoreFlowDTO();
        BeanUtils.copyProperties(storeFlowParam, storeFlowDTO);

        if (!StringUtils.isEmpty(storeFlowParam.getCreateTime())) {
            // ES 对于时间的格式要求比较严格，这里需要将时间转换为字符串的时间戳
            storeFlowDTO.setCreateTime(String.valueOf(storeFlowParam.getCreateTime().toInstant(OF).toEpochMilli()));
        }
        if (!StringUtils.isEmpty(storeFlowParam.getUpdateTime())) {
            // ES 对于时间的格式要求比较严格，这里需要将时间转换为字符串的时间戳
            storeFlowDTO.setUpdateTime(String.valueOf(storeFlowParam.getUpdateTime().toInstant(OF).toEpochMilli()));
        }
        // 指定文档主键，并根据条件返回组装好的 updateQuery
        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(storeFlowDTO.getId()))
                // 修改的内容
                .withDocument(Document.parse(JSON.toJSONString(storeFlowDTO)))
                // 被修改的文档不存在时，改为插入
                .withDocAsUpsert(true)
                .build();
        return elasticsearchRestTemplate.update(updateQuery, IndexCoordinates.of("store_flow"));
    }

    /**
     * 检查必要查询参数
     *
     * @param searchStoreFlowParam 查询条件
     * @return true：null false：StoreFlowFailEnum
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
     * @param searchStoreFlowParam 请求参数
     * @return 查询器
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
     * @param searchStoreFlowParam 请求参数
     * @return 范围查询器
     */
    private RangeQueryBuilder tryBuilderRangeQuery(SearchStoreFlowParam searchStoreFlowParam) {
        // 条件不满足返回null
        if (searchStoreFlowParam.getStartTime() == null && searchStoreFlowParam.getEndTime() == null) {
            return null;
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime");
        if (searchStoreFlowParam.getStartTime() != null) {
            rangeQueryBuilder.from(searchStoreFlowParam.getStartTime().toInstant(OF).toEpochMilli());
        }
        if (searchStoreFlowParam.getEndTime() != null) {
            rangeQueryBuilder.to(searchStoreFlowParam.getEndTime().toInstant(OF).toEpochMilli());
        }
        return rangeQueryBuilder;
    }
}
