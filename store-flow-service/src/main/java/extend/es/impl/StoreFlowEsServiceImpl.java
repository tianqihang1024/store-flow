package extend.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import extend.bean.StoreFlow;
import extend.dto.UpdateStoreFlowDTO;
import extend.enums.ExtendExceptionEnum;
import extend.enums.StoreFlowSortColumnNameEnum;
import extend.es.StoreFlowEsService;
import extend.param.SearchStoreFlowParam;
import extend.param.SearchStoreFlowScrollParam;
import extend.param.UpdateStoreFlowParam;
import extend.util.StoreFlowFailEnum;
import extend.utils.Result;
import extend.vo.SearchScrollStoreFlowVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/9/25 15:58
 */
@Slf4j
@Service
public class StoreFlowEsServiceImpl implements StoreFlowEsService {

    /**
     * ES索引名称
     */
    private static final String INDEX_NAME = "store_flow";

    @Resource
    private RestHighLevelClient restHighLevelClient;

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 主键对应的记录，查询不到返回null
     */
    @Override
    public Result<StoreFlow> findStoreFlowById(Long id) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.termQuery("id", id));

        SearchRequest searchRequest = new SearchRequest().source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            org.elasticsearch.search.SearchHit searchHit = searchResponse.getHits().getHits()[0];
            StoreFlow storeFlow = JSON.parseObject(searchHit.getSourceAsString(), StoreFlow.class);
            return Result.success(storeFlow);
        } catch (IOException e) {
            log.info("StoreFlowEsServiceImpl.findStoreFlowById 执行失败 e:", e);
        }
        return Result.fail(StoreFlowFailEnum.SEARCH_FAIL.getCode(), StoreFlowFailEnum.SEARCH_FAIL.getMsg());
    }

    /**
     * 根据租户、创建时间查询
     *
     * @param searchStoreFlowParam 查询条件
     * @return 条件对应的记录
     */
    @Override
    public Result<StoreFlow> searchStoreFlowByParam(SearchStoreFlowParam searchStoreFlowParam) {

        // 1.检查参数
        StoreFlowFailEnum checkFlag = this.checkSearchParam(searchStoreFlowParam);
        if (checkFlag != null) return Result.fail(checkFlag.getCode(), checkFlag.getMsg());

        // 2.构建查询器
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("tenantId", searchStoreFlowParam.getTenantId())).filter(QueryBuilders.rangeQuery("created").from(searchStoreFlowParam.getStartTime()).to(searchStoreFlowParam.getEndTime()));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(boolQueryBuilder);
        SearchRequest searchQuery = new SearchRequest().source(searchSourceBuilder);

        // 3.查询并返回
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchQuery, RequestOptions.DEFAULT);
            org.elasticsearch.search.SearchHit searchHit = searchResponse.getHits().getHits()[0];
            StoreFlow storeFlow = JSON.parseObject(searchHit.getSourceAsString(), StoreFlow.class);
            return Result.success(storeFlow);
        } catch (IOException e) {
            log.info("StoreFlowEsServiceImpl.searchStoreFlowByParam 执行失败 e:", e);
        }
        return Result.fail(StoreFlowFailEnum.SEARCH_FAIL.getCode(), StoreFlowFailEnum.SEARCH_FAIL.getMsg());
    }

    /**
     * 修改客流数据
     *
     * @param storeFlowParam 待修改的客流数据
     * @return 修改 or 插入 执行结果
     */
    @Override
    public Result<Void> updateStoreFlow(UpdateStoreFlowParam storeFlowParam) {

        // 请求参数转换为 DTO 以便插入 ES
        UpdateStoreFlowDTO storeFlowDTO = new UpdateStoreFlowDTO();
        BeanUtils.copyProperties(storeFlowParam, storeFlowDTO);

        Map<String, Object> storeFlowMap = JSON.parseObject(JSON.toJSONString(storeFlowDTO), new TypeReference<>() {
        });

        UpdateRequest updateRequest = new UpdateRequest(INDEX_NAME, String.valueOf(storeFlowParam.getId()));
        updateRequest.docAsUpsert(true);
        updateRequest.doc(storeFlowMap);

        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED || updateResponse.getResult() == DocWriteResponse.Result.UPDATED)
                return new Result<>();
            log.info("StoreFlowEsServiceImpl.updateStoreFlow 执行不符合预期，出现了创建和更新以外的操作");
        } catch (IOException e) {
            log.info("StoreFlowEsServiceImpl.updateStoreFlow 执行失败 e:", e);
        }
        return Result.fail(StoreFlowFailEnum.UPDATE_FAIL.getCode(), StoreFlowFailEnum.UPDATE_FAIL.getMsg());
    }

    /**
     * 滚动查询店铺客流数据
     *
     * @param searchStoreFlowScrollParam
     * @return
     */
    @Override
    public Result<SearchScrollStoreFlowVO> searchScrollStoreFlow(SearchStoreFlowScrollParam searchStoreFlowScrollParam) {

        // 生命查询请求
        SearchRequest searchRequest;

        // scrollId 为空表明第一次查询，需要根据请求入参组装查询请求
        if (StringUtils.isEmpty(searchStoreFlowScrollParam.getScrollId())) {
            searchRequest = buildSearchRequestByParam(searchStoreFlowScrollParam);
        } else {
            // scrollId 不为空，可以直接根据 scrollId 查询
            searchRequest = new SearchRequest().scroll(searchStoreFlowScrollParam.getScrollId());
        }

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            List<StoreFlow> storeFlowList = Arrays.stream(searchResponse.getHits().getHits()).map(SearchHit::getSourceAsString).map(searchHit -> JSON.parseObject(searchHit, StoreFlow.class)).collect(Collectors.toList());


            TotalHits totalHits = searchResponse.getHits().getTotalHits();
            Long total = totalHits != null ? totalHits.value : 0L;

            SearchScrollStoreFlowVO searchScrollStoreFlowVO = SearchScrollStoreFlowVO.builder().content(storeFlowList).scrollId(searchResponse.getScrollId()).total(total).build();

            return Result.success(searchScrollStoreFlowVO);
        } catch (IOException e) {
            log.error("ArticleServiceImpl.searchScrollArticle search fail e:", e);
        }

        return Result.fail(ExtendExceptionEnum.SYSTEM_EXCEPTION.getCode(), ExtendExceptionEnum.SYSTEM_EXCEPTION.getMsg());
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


    private SearchRequest buildSearchRequestByParam(SearchStoreFlowScrollParam searchStoreFlowScrollParam) {

        // 条件查询器
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("tenantId", searchStoreFlowScrollParam.getTenantId())).filter(QueryBuilders.termQuery("storeId", searchStoreFlowScrollParam.getStoreId()));

        // 自定义排序器
        StoreFlowSortColumnNameEnum columnNameEnum = StoreFlowSortColumnNameEnum.getEnumByCode(searchStoreFlowScrollParam.getSortColumnCode());
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort(columnNameEnum.getColumnName()).order(SortOrder.ASC);
        // 分数排序器
        ScoreSortBuilder scoreSortBuilder = new ScoreSortBuilder();


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(queryBuilder).size(searchStoreFlowScrollParam.getPageSize()).sort(scoreSortBuilder).sort(sortBuilder);

        // 设置 scroll 快照时间
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(100L));

        return new SearchRequest().indices(INDEX_NAME).source(searchSourceBuilder).scroll(scroll);
    }

}
