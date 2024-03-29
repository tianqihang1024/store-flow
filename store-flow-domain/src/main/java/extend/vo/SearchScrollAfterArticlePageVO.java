package extend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 田奇杭
 * @Description 滚动查询文章页vo
 * @Date 2022/11/10 22:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchScrollAfterArticlePageVO extends BasePageVO {

    /**
     * 深度分页查询结果
     */
    private List<SearchScrollArticleVO> searchResponse;

    /**
     * 深度分页游标，根据这个值进行上下页切换
     */
    private Object[] sortValues;

}
