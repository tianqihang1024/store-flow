package extend.vo;

import extend.bean.StoreFlow;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/5 16:03
 */
@Data
@Builder
public class SearchScrollStoreFlowVO {

    private String scrollId;

    private Long total;

    private List<StoreFlow> content;
}
