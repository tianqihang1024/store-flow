package extend.es.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import extend.bean.Article;
import extend.dao.ArticleRepository;
import extend.enums.ExtendExceptionEnum;
import extend.enums.StoreFlowSortColumnNameEnum;
import extend.es.ArticleService;
import extend.param.SearchScrollAfterArticleParam;
import extend.param.SearchScrollArticleParam;
import extend.util.StoreFlowFailEnum;
import extend.utils.Result;
import extend.vo.SearchScrollAfterArticlePageVO;
import extend.vo.SearchScrollAfterArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private ArticleRepository articleRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    public void initData() {

        Article article1 = new Article(1L, "社会主义本质的确定性与实现形式的开放性", "习近平总书记指出，“全面建设社会主义现代化国家、基本实现社会主义现代化，既是社会主义初级阶段我国发展的要求，也是我国社会主义从初级阶段向更高阶段迈进的要求”。社会主义的本质是确定的。社会主义本质在不同国家、不同历史阶段", LocalDateTime.now());
        Article article2 = new Article(2L, "毫不动摇坚持和发展中国特色社会主义", "2018年1月5日，习近平总书记在新进中央委员会的委员、候补委员和省部级主要领导干部学习贯彻习近平新时代中国特色社会主义思想和党的十九大精神研讨班上鲜明提出“三个一以贯之”的要求，第一条就是“坚持和发展中国特色社会主义要一以贯之”。", LocalDateTime.now());
        Article article3 = new Article(3L, "习近平：高举中国特色社会主义伟大旗", "高举中国特色社会主义伟大旗帜为全面建设社会主义现代化国家而团结奋斗——在中国共产党第二十次全国代表大会上的报告", LocalDateTime.now());
        Article article5 = new Article(4L, "论中国特色社会主义的世界历史使命", "摘要】  中国特色社会主义是由中国特色社会主义理论、中国特色社会主义道路、中国特色社会主义制度构成的三位一体的现实的历史性总体。", LocalDateTime.now());
        Article article4 = new Article(5L, "社会主义", "社会主义（socialism）是一种社会学思想，诞生于16世纪初，主张整个社会应作为整体，由社会拥有和控制产品、资本、土地、资产等，其管理和分配基于公众利益。19世纪30至40年代，“社会主义”的概念在西欧广为流传，发展出不同分支。", LocalDateTime.now());
        Article article6 = new Article(6L, "宣言：社会主义没有辜负中国", "习近平总书记在党史学习教育动员大会上深刻指出，对共产主义的信仰，对中国特色社会主义的信念，是共产党人的政治灵魂，是共产党人经受住任何考验的精神支柱，强调党的百年奋斗历程和伟大成就，是我们增强道路自信、理论自信、制度自信、文化自信最坚实的基础。", LocalDateTime.now());
        Article article7 = new Article(7L, "社会主义", "社会主义（socialism）是一种社会学思想，诞生于16世纪初，主张整个社会应作为整体，由社会拥有和控制产品、资本、土地、资产等，其管理和分配基于公众利益。19世纪30至40年代，“社会主义”的概念在西欧广为流传，发展出不同分支。", LocalDateTime.now());

        List<Article> articleList = Arrays.asList(article1, article2, article3, article4, article5, article6, article7);
        articleRepository.saveAll(articleList);
    }

    /**
     * 滚动查询文章
     *
     * @param searchScrollArticleParam
     * @return
     */
    @Override
    public SearchScrollHits<Article> searchScrollArticle(SearchScrollArticleParam searchScrollArticleParam) {

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

        // 搜索查询条件
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSort(scoreSortBuilder)
                .withSort(sortBuilder)
                .build();
        nativeSearchQuery.setMaxResults(searchScrollArticleParam.getPageSize());

        //设置缓存内数据的保留时间
        long scrollTimeInMillis = 60000;

        SearchScrollHits<Article> searchScrollHits;

        // scrollId 为空表示第一次查询
        if (StringUtils.isEmpty(searchScrollArticleParam.getScrollId())) {
            searchScrollHits = elasticsearchRestTemplate.searchScrollStart(scrollTimeInMillis, nativeSearchQuery, Article.class, IndexCoordinates.of(INDEX_NAME));
        } else {
            // scrollId 不为空表示最少进行了一次查询，该方法执行后会重新刷新快照保留时间
            searchScrollHits = elasticsearchRestTemplate.searchScrollContinue(searchScrollArticleParam.getScrollId(), scrollTimeInMillis, Article.class, IndexCoordinates.of(INDEX_NAME));
        }

        return searchScrollHits;
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
        if (storeFlowFailEnum != null) {
            return Result.fail(storeFlowFailEnum.getCode(), storeFlowFailEnum.getMsg());
        }

        // 构建查询器
        SearchRequest searchRequest = this.buildSearchRequest(searchScrollAfterArticleParam);
        try {
            // 查询
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 构建结果集并返回
            return Result.success(buildSearchScrollAfterArticlePageVO(searchResponse));
        } catch (IOException e) {
            log.error("ArticleServiceImpl.searchScrollAfterArticle search fail e:", e);
            return Result.fail(ExtendExceptionEnum.SYSTEM_EXCEPTION.getCode(), ExtendExceptionEnum.SYSTEM_EXCEPTION.getMsg());
        }
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
                .should(QueryBuilders.matchQuery("title", searchScrollAfterArticleParam.getSearchTxt()))
                .should(QueryBuilders.matchQuery("context", searchScrollAfterArticleParam.getSearchTxt()));

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

        if (searchScrollAfterArticleParam.getPageNum() == null || searchScrollAfterArticleParam.getPageNum() >= 1) {
            return new Object[0];
        }

        String searchTxtKey = String.format(SEARCH_TXT_KEY, searchScrollAfterArticleParam.getSearchTxt());
        Object o = redisTemplate.boundHashOps(searchTxtKey).get(searchScrollAfterArticleParam.getPageNum());

        if (!(o instanceof String)) {
            return new Object[0];
        }

        JSONArray objects = JSON.parseArray((String) o);
        return new Object[]{objects.get(0), objects.get(1)};
    }

    /**
     * 构建 VO
     *
     * @param searchResponse
     * @return
     */
    private SearchScrollAfterArticlePageVO buildSearchScrollAfterArticlePageVO(SearchResponse searchResponse) {

        List<SearchScrollAfterArticleVO> searchScrollAfterArticleVos = new ArrayList<>();
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            searchScrollAfterArticleVos.add(JSON.parseObject(searchHit.getSourceAsString(), SearchScrollAfterArticleVO.class));
        }

        SearchHit[] hits = searchResponse.getHits().getHits();
        SearchHit last = hits[hits.length - 1];
        Object[] sortValues = last.getSortValues();

        return SearchScrollAfterArticlePageVO.builder()
                .searchResponse(searchScrollAfterArticleVos)
                .sortValues(sortValues)
                .build();
    }

}
