package extend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/10 22:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchScrollAfterArticlePageVO extends BasePageVO {

    private List<SearchScrollAfterArticleVO> searchResponse;

    private Object[] sortValues;

}
