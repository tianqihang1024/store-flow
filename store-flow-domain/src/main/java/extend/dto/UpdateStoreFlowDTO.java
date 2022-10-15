package extend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/10/15 21:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStoreFlowDTO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 店铺ID
     */
    private String storeId;
    /**
     * 客流量
     */
    private Long flowCount;
    /**
     * 是否有效 0：有效 1：失效
     */
    private Integer valid;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
}
