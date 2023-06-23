package extend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 田奇杭
 * @Description 滚动查询文章页vo
 * @Date 2023/6/23 11:59
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchScrollArticlePageVO {

    /**
     * 深度分页查询结果
     */
    private List<SearchScrollArticleVO> searchResponse;

    /**
     * 滚动id，根据这个值进行上下页切换
     */
    private String ScrollId;

}
