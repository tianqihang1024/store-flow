package extend.es;

import extend.bean.Article;
import extend.bean.StoreFlow;
import extend.param.SearchScrollArticleParam;
import org.springframework.data.elasticsearch.core.SearchScrollHits;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:49
 */
public interface ArticleService {

    void initData();

    SearchScrollHits<Article> searchScrollArticle(SearchScrollArticleParam searchScrollArticleParam);
}
