package extend.dao;

import extend.bean.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:49
 */
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {


}
