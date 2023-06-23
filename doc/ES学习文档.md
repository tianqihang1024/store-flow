`ES`存储时间是带有时区的，查询要做处理

## `ElasticsearchTemplate`

- `ElasticsearchTemplate` 封装`ES`客户端的一些原生`API`模板，方便实现一些查询

```java
elasticsearchTemplate.queryForPage   #是查询一个分页列表，用的就是一个对象实例
        NativeSearchQuery                #是springdata中的查询条件
        NativeSearchQueryBuilder         #用于建造一个NativeSearchQuery查询对象
        QueryBuilders                    #设置查询条件,是ES中的类
        SortBuilders                     #设置排序条件
        HighlightBuilder                 #设置高亮显示
```

## `QueryBuilders`

`QueryBuilders`是`ES`中的查询条件构造器

- `QueryBuilders.termQuery`精确查询指定字段

  ```java
  // 查询 storeId = 2 的记录
  TermQueryBuilder termQuery = QueryBuilders.termQuery("storeId", 2);
  ```

- `QueryBuilders.matchQuery`按分词器进行模糊查询

  ```java
  // 查询标题存在中国的记录
  MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "中国");
  ```

- `QueryBuilders.rangeQuery`按指定字段进行区间范围查询

  ```java
  // 查询创建时间在范围内的记录
  // 大于等于      .from    .gte   
  // 小于等于      .to      .lte 
  // 大于          .gt
  // 小于          .lt
  RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("createTime").from(startTime).to(endTime);
  ```

- `QueryBuilders.boolQuery`其他查询的布尔组合查询器。

  ```java
  // filter、must、mustNot可多条件联查
  BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery() 
                  // filter：不计算分值
                  .filter(QueryBuilders.termQuery("tenantId", tenantId))
                  // must：计算分值
                  .must(QueryBuilders.termQuery("storeId", tenantId)) 
                  // mustNot：满足条件的数据将被过滤
                  .mustNot(QueryBuilders.termQuery("storeId", tenantId))
                  // should：非必须满足的条件
                  .should(QueryBuilders.termQuery("storeId", tenantId))
                  // minimumShouldMatch：至少要满足的 should 的数量
                  .minimumShouldMatch(1);
  ```

## `NativeSearchQuery`

- 原生的查询条件类，用来和`ES`的一些原生查询方法进行搭配，实现一些比较复杂的查询，最终进行构建`.build`
  可作为`ElasticsearchTemplate.search`的参数使用

  ```java
  // 本地查询对象
  NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                  // 条件
                  .withQuery(boolQueryBuilder)
                  // 排序
                  .withSort(SortBuilders.fieldSort("id").order(SortOrder.ASC))
                  // 高亮
                  .withHighlightFields(name, ms)
                  // 分页
                  .withPageable(PageRequest.of(pageNum - 1, pageSize))
                  // 构建
                  .build();
  // 调用 es 查询
  // 参数一: NativeSearchQuery 封装的查询数据对象
  // 参数二: es对应索引实体类
  // 参数三: 调用高亮工具类
  elasticsearchRestTemplate.search(searchQuery, StoreFlow.class, new Hig());
  ```


-

## `API`

### 等值查询

```java

```

```java

```

## `filter or must`

- `filter`不会计算记录的分数，`must`会计算
- `filter`再确认记录符合要求后会将其放入缓存中，`must`则不会

```java

```

```java

```

```java

```

```java

```

```java

```

```java

```

```java

```

```java

```

```java
package extend.service.impl;

import extend.bean.StoreFlow;
import extend.dto.StoreFlowRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 22489
 * @description 针对表【store_flow(店铺客流表)】的 ES 操作Service实现
 * @createDate 2022-08-19 22:59:58
 */
@Service
public class StoreFlowServiceImpl {

    @Resource
    private StoreFlowRepository storeFlowRepository;

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public SearchHits<StoreFlow> findStoreFlowByTenantId(String tenantId, String startTime, String endTime) {
        // 
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .filter(QueryBuilders.rangeQuery("createTime").gte(startTime).lte(endTime))
                .filter(QueryBuilders.matchQuery("tenantId", tenantId))
                .must(QueryBuilders.matchQuery("storeId", tenantId))
                .mustNot(QueryBuilders.matchQuery("storeId", tenantId));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();
        return elasticsearchRestTemplate.search(searchQuery, StoreFlow.class);
    }

}

```

















