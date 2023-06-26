package extend.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import extend.bean.Article;
import extend.bean.Author;
import extend.enums.ExtendExceptionEnum;
import extend.enums.StoreFlowSortColumnNameEnum;
import extend.es.ArticleService;
import extend.param.SearchScrollAfterArticleParam;
import extend.param.SearchScrollArticleParam;
import extend.util.StoreFlowFailEnum;
import extend.utils.Result;
import extend.vo.SearchScrollAfterArticlePageVO;
import extend.vo.SearchScrollArticlePageVO;
import extend.vo.SearchScrollArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:49
 */
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    /**
     * ES索引名称
     */
    private static final String INDEX_NAME = "article_index";

    /**
     * 搜索文本键
     */
    private static final String SEARCH_TXT_KEY = "search_txt_%s";

    /**
     * 搜索文本最大长度
     */
    private static final int SEARCH_TXT_MAX_LENGTH = 25;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    public void initData() {

        Article article1 = new Article(1L, new Author("毛泽东", 1), "社会主义本质的确定性与实现形式的开放性", "习近平总书记指出，“全面建设社会主义现代化国家、基本实现社会主义现代化，既是社会主义初级阶段我国发展的要求，也是我国社会主义从初级阶段向更高阶段迈进的要求”。社会主义的本质是确定的。社会主义本质在不同国家、不同历史阶段", LocalDateTime.now());
        Article article2 = new Article(2L, new Author("习近平", 1), "毫不动摇坚持和发展中国特色社会主义", "2018年1月5日，习近平总书记在新进中央委员会的委员、候补委员和省部级主要领导干部学习贯彻习近平新时代中国特色社会主义思想和党的十九大精神研讨班上鲜明提出“三个一以贯之”的要求，第一条就是“坚持和发展中国特色社会主义要一以贯之”。", LocalDateTime.now());
        Article article3 = new Article(3L, new Author("习近平", 1), "习近平：高举中国特色社会主义伟大旗", "高举中国特色社会主义伟大旗帜为全面建设社会主义现代化国家而团结奋斗——在中国共产党第二十次全国代表大会上的报告", LocalDateTime.now());
        Article article5 = new Article(4L, new Author("习近平", 1), "论中国特色社会主义的世界历史使命", "摘要】  中国特色社会主义是由中国特色社会主义理论、中国特色社会主义道路、中国特色社会主义制度构成的三位一体的现实的历史性总体。", LocalDateTime.now());
        Article article4 = new Article(5L, new Author("习近平", 1), "社会主义", "社会主义（socialism）是一种社会学思想，诞生于16世纪初，主张整个社会应作为整体，由社会拥有和控制产品、资本、土地、资产等，其管理和分配基于公众利益。19世纪30至40年代，“社会主义”的概念在西欧广为流传，发展出不同分支。", LocalDateTime.now());
        Article article6 = new Article(6L, new Author("习近平", 1), "宣言：社会主义没有辜负中国", "习近平总书记在党史学习教育动员大会上深刻指出，对共产主义的信仰，对中国特色社会主义的信念，是共产党人的政治灵魂，是共产党人经受住任何考验的精神支柱，强调党的百年奋斗历程和伟大成就，是我们增强道路自信、理论自信、制度自信、文化自信最坚实的基础。", LocalDateTime.now());
        Article article7 = new Article(7L, new Author("习近平", 1), "社会主义", "社会主义（socialism）是一种社会学思想，诞生于16世纪初，主张整个社会应作为整体，由社会拥有和控制产品、资本、土地、资产等，其管理和分配基于公众利益。19世纪30至40年代，“社会主义”的概念在西欧广为流传，发展出不同分支。", LocalDateTime.now());

        List<Article> articleList = Arrays.asList(article1, article2, article3, article4, article5, article6, article7);
        for (Article article : articleList) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
            }
            LocalDateTime now = LocalDateTime.now();
            log.info("now:{}", now);
            article.setCreated(now);
            Map<String, Object> map1 = JSON.parseObject(JSON.toJSONString(article), new TypeReference<>() {
            });
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME).source(map1, Requests.INDEX_CONTENT_TYPE);
            try {
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.info("创建索引失败");
            }
        }
    }


    /**
     * 滚动查询文章
     *
     * @param searchScrollArticleParam 滚动查询入参
     * @return 滚动查询文章页vo
     */
    @Override
    public Result<SearchScrollArticlePageVO> searchScrollArticle(SearchScrollArticleParam searchScrollArticleParam) {

        // 生命查询请求
        SearchRequest searchRequest;

        // scrollId 为空表明第一次查询，需要根据请求入参组装查询请求
        if (StringUtils.isEmpty(searchScrollArticleParam.getScrollId())) {
            searchRequest = buildSearchRequestByParam(searchScrollArticleParam);
        } else {
            // scrollId 不为空，可以直接根据 scrollId 查询
            searchRequest = new SearchRequest().scroll(searchScrollArticleParam.getScrollId());
        }

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            return Result.success(this.buildSearchScrollArticlePageVO(searchResponse));
        } catch (IOException e) {
            log.error("ArticleServiceImpl.searchScrollArticle search fail e:", e);
        }
        return Result.fail(ExtendExceptionEnum.SYSTEM_EXCEPTION.getCode(), ExtendExceptionEnum.SYSTEM_EXCEPTION.getMsg());
    }

    /**
     * 根据滚动查询入参返回es请求对象
     *
     * @param searchScrollArticleParam 滚动查询入参
     * @return 请求对象
     */
    private SearchRequest buildSearchRequestByParam(SearchScrollArticleParam searchScrollArticleParam) {

        // 条件查询器
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", searchScrollArticleParam.getSearchText()))
                .should(QueryBuilders.matchQuery("context", searchScrollArticleParam.getSearchText()));

        // 自定义排序器
        StoreFlowSortColumnNameEnum columnNameEnum = StoreFlowSortColumnNameEnum.getEnumByCode(searchScrollArticleParam.getSortColumnCode());
        FieldSortBuilder sortBuilder = SortBuilders
                .fieldSort(columnNameEnum.getColumnName())
                .order(SortOrder.DESC);

        // 分数排序器
        ScoreSortBuilder scoreSortBuilder = new ScoreSortBuilder();

        // 存活时间，当索引数据量特别大时，出现超时可能性大，此值适当调大
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(searchScrollArticleParam.getPageSize())
                .sort(scoreSortBuilder)
                .sort(sortBuilder);

        // 设置 scroll 快照时间
        Scroll scroll = new Scroll(TimeValue.timeValueMinutes(100L));

        return new SearchRequest()
                .indices(INDEX_NAME)
                .scroll(scroll)
                .source(searchSourceBuilder);
    }

    /**
     * 滚动查询文章
     *
     * @param searchScrollAfterArticleParam
     * @return
     */
    @Override
    public Result<SearchScrollAfterArticlePageVO> searchScrollAfterArticle(SearchScrollAfterArticleParam searchScrollAfterArticleParam) {

        // 参数检查
        StoreFlowFailEnum storeFlowFailEnum = this.checkSearchScrollAfterArticleParam(searchScrollAfterArticleParam);
        if (storeFlowFailEnum != null)
            return Result.fail(storeFlowFailEnum.getCode(), storeFlowFailEnum.getMsg());

        // 构建查询器
        SearchRequest searchRequest = this.buildSearchRequest(searchScrollAfterArticleParam);
        try {
            // 查询
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 构建结果集
            SearchScrollAfterArticlePageVO searchScrollAfterArticlePageVO = this.buildSearchScrollAfterArticlePageVO(searchResponse);
            // 尝试缓存排序结果，方便后续的前后翻页。向后查询和第一次查询需要缓存排序结果，用以维护缓存中的页码索引
            if (searchScrollAfterArticleParam.getSortValues() != null || searchScrollAfterArticleParam.getPageNum() == 1)
                this.tryCacheSortValue(searchScrollAfterArticlePageVO.getSortValues(), searchScrollAfterArticleParam.getSearchTxt(), searchScrollAfterArticleParam.getPageNum());
            // 返回结果集
            return Result.success(searchScrollAfterArticlePageVO);
        } catch (IOException e) {
            log.error("ArticleServiceImpl.searchScrollAfterArticle search fail e:", e);
        }
        return Result.fail(ExtendExceptionEnum.SYSTEM_EXCEPTION.getCode(), ExtendExceptionEnum.SYSTEM_EXCEPTION.getMsg());
    }

    private StoreFlowFailEnum checkSearchScrollAfterArticleParam(SearchScrollAfterArticleParam searchScrollAfterArticleParam) {

        if (searchScrollAfterArticleParam == null) {
            return StoreFlowFailEnum.PARAM_CANNOT_BE_EMPTY;
        }
        if (StringUtils.isEmpty(searchScrollAfterArticleParam.getSearchTxt())) {
            return StoreFlowFailEnum.SEARCH_TEXT_IS_NULL;
        }
        return null;
    }

    private SearchRequest buildSearchRequest(SearchScrollAfterArticleParam searchScrollAfterArticleParam) {

        // 尝试修正搜索内容的长度，避免极端的搜索内容
        if (searchScrollAfterArticleParam.getSearchTxt().length() > SEARCH_TXT_MAX_LENGTH) {
            searchScrollAfterArticleParam.setSearchTxt(searchScrollAfterArticleParam.getSearchTxt().substring(0, SEARCH_TXT_MAX_LENGTH));
        }

        // 条件查询器
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("title", searchScrollAfterArticleParam.getSearchTxt()))
                .must(QueryBuilders.matchQuery("context", searchScrollAfterArticleParam.getSearchTxt()));

        if (!StringUtils.isEmpty(searchScrollAfterArticleParam.getName())) {
            NestedQueryBuilder author = QueryBuilders.nestedQuery("author", QueryBuilders.termQuery("author.name.keyword", searchScrollAfterArticleParam.getName()), ScoreMode.Avg);
            queryBuilder.filter(author);
        }
        if (!StringUtils.isEmpty(searchScrollAfterArticleParam.getAge())) {
            NestedQueryBuilder author = QueryBuilders.nestedQuery("author", QueryBuilders.termsQuery("author.age.keyword", searchScrollAfterArticleParam.getAge()), ScoreMode.Avg);
            queryBuilder.filter(author);
        }
        if (searchScrollAfterArticleParam.getStartTime() != null || searchScrollAfterArticleParam.getEndTime() != null) {
            queryBuilder.filter(QueryBuilders.rangeQuery("created").from(searchScrollAfterArticleParam.getStartTime()).to(searchScrollAfterArticleParam.getEndTime()));
        }


        // 自定义排序器
        StoreFlowSortColumnNameEnum columnNameEnum = StoreFlowSortColumnNameEnum.getEnumByCode(searchScrollAfterArticleParam.getSortColumnCode());
        FieldSortBuilder sortBuilder = SortBuilders
                .fieldSort(columnNameEnum.getColumnName())
                .order(SortOrder.DESC);
        // 分数排序器
        ScoreSortBuilder scoreSortBuilder = new ScoreSortBuilder();

        // 源生成器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .query(queryBuilder)
                .size(searchScrollAfterArticleParam.getPageSize())
                .sort(scoreSortBuilder)
                .sort(sortBuilder);
        // 尝试获取排序值，并复制
        Object[] sortValues = searchScrollAfterArticleParam.getSortValues() != null ? searchScrollAfterArticleParam.getSortValues() : this.tryGetCacheSortValues(searchScrollAfterArticleParam);
        if (sortValues.length == 2) {
            sourceBuilder.searchAfter(sortValues);
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);
        searchRequest.source(sourceBuilder);
        return searchRequest;
    }


    /**
     * 尝试获取缓存的排序值
     *
     * @param searchScrollAfterArticleParam
     * @return
     */
    private Object[] tryGetCacheSortValues(SearchScrollAfterArticleParam searchScrollAfterArticleParam) {

        if (searchScrollAfterArticleParam.getPageNum() == null || searchScrollAfterArticleParam.getPageNum() <= 1) {
            return new Object[]{};
        }

        String searchTxtKey = String.format(SEARCH_TXT_KEY, searchScrollAfterArticleParam.getSearchTxt());
        Object o = redisTemplate.boundHashOps(searchTxtKey).get(searchScrollAfterArticleParam.getPageNum().toString());

        if (!(o instanceof String)) {
            return new Object[]{};
        }

        JSONArray objects = JSON.parseArray((String) o);
        return new Object[]{String.valueOf(objects.get(0)), String.valueOf(objects.get(1))};
    }

    /**
     * 构建滚动查询文章页vo
     *
     * @param searchResponse 滚动查询结果
     * @return 滚动查询文章页vo
     */
    private SearchScrollAfterArticlePageVO buildSearchScrollAfterArticlePageVO(SearchResponse searchResponse) {

        if (searchResponse.getHits().getHits() == null || searchResponse.getHits().getHits().length == 0) {
            return SearchScrollAfterArticlePageVO.builder().build();
        }

        List<SearchScrollArticleVO> searchScrollArticleVos = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchScrollArticleVos.add(JSON.parseObject(searchHit.getSourceAsString(), SearchScrollArticleVO.class));
        }

        SearchHit[] hits = searchResponse.getHits().getHits();
        SearchHit last = hits[hits.length - 1];
        Object[] sortValues = last.getSortValues();

        return SearchScrollAfterArticlePageVO.builder()
                .searchResponse(searchScrollArticleVos)
                .sortValues(sortValues)
                .build();
    }

    /**
     * 构建滚动查询文章页vo
     *
     * @param searchResponse 滚动查询结果
     * @return 滚动查询文章页vo
     */
    private SearchScrollArticlePageVO buildSearchScrollArticlePageVO(SearchResponse searchResponse) {

        if (searchResponse.getHits().getHits() == null || searchResponse.getHits().getHits().length == 0) {
            return SearchScrollArticlePageVO.builder().build();
        }

        List<SearchScrollArticleVO> searchScrollArticleVos = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchScrollArticleVos.add(JSON.parseObject(searchHit.getSourceAsString(), SearchScrollArticleVO.class));
        }
        String scrollId = searchResponse.getScrollId();

        return SearchScrollArticlePageVO.builder()
                .searchResponse(searchScrollArticleVos)
                .ScrollId(scrollId)
                .build();
    }

    /**
     * 尝试缓存排序结果，方便以后可能的前后页查询。
     * 本方法并不保证成功，即使出现异常，也不能因为缓存而影响到查询结果的展示。
     * todo 所有涉及缓存的地方都需要整改，待公共包给完善后替换现有API
     *
     * @param sortValueArray 排序结果，下一次查询将从他开始检索
     * @param searchTxt      本次查询的搜索内容
     * @param pageNum        本次查询的搜索内容的页码
     */
    private void tryCacheSortValue(Object[] sortValueArray, String searchTxt, Integer pageNum) {
        String searchTxtKey = String.format(SEARCH_TXT_KEY, searchTxt);
        String sortValueString = JSON.toJSONString(sortValueArray);
        try {
            redisTemplate.boundHashOps(searchTxtKey).put((++pageNum).toString(), sortValueString);
        } catch (Exception e) {
            log.error("sortValueArray 缓存失败 searchTxt:{}, pageNum:{}, sortValueArray:{} e:", searchTxt, pageNum, sortValueString, e);
        }
    }

}
