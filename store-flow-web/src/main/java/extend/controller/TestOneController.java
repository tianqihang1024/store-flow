package extend.controller;

import com.alibaba.fastjson.JSON;
import extend.bean.StoreFlow;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/27 21:06
 */
@Slf4j
@RestController
@RequestMapping("/test/one/")
public class TestOneController {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @RequestMapping("searchStoreFlowByTenantId")
    public List<StoreFlow> searchStoreFlowByTenantId(@RequestParam(required = false) String tenantId,
                                                     @RequestParam(required = false) String storeId,
                                                     @RequestParam(required = false) String id) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(tenantId)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("tenantId", tenantId));
        }
        if (!StringUtils.isEmpty(storeId)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("storeId", storeId));
        }
        if (!StringUtils.isEmpty(id)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }

        SearchSourceBuilder query = SearchSourceBuilder.searchSource().query(boolQueryBuilder);

        SearchRequest searchRequest = new SearchRequest();
        SearchRequest source = searchRequest.source(query);

        try {
            List<StoreFlow> list = new ArrayList<>();
            SearchResponse response = restHighLevelClient.search(source, RequestOptions.DEFAULT);
            for (SearchHit hit : response.getHits().getHits()) {
                list.add(JSON.parseObject(hit.getSourceAsString(), StoreFlow.class));
            }
            return list;
        } catch (IOException e) {
            log.info("查询失败");
        }
        return null;
    }

    @RequestMapping("deleteStoreFlowById")
    public DeleteResponse deleteStoreFlowById(@RequestParam(required = false) Long id) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (Objects.nonNull(id)) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("id", id));
        }

        DeleteRequest deleteRequest = new DeleteRequest("store_flow", String.valueOf(id));

        try {
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);

            return delete;
        } catch (IOException e) {
            log.info("查询失败");
        }
        return null;
    }

    @RequestMapping("updateStoreFlowById")
    public UpdateResponse updateStoreFlowById(@RequestBody StoreFlow storeFlow) {

        UpdateRequest updateRequest = new UpdateRequest("store_flow", String.valueOf(storeFlow.getId()));
        updateRequest.doc(JSON.parseObject(JSON.toJSONString(storeFlow), Map.class));
        updateRequest.docAsUpsert(true);

        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);

            return update;
        } catch (IOException e) {
            log.info("查询失败");
        }
        return null;
    }
}
