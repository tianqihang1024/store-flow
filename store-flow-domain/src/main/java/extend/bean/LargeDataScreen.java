package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客流数据大屏
 * @TableName large_data_screen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LargeDataScreen implements Serializable {

    private static final long serialVersionUID = 1917921990572282937L;

    /**
     * 
     */
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 店铺id
     */
    private String storeId;

    /**
     * 今日客流排名
     */
    private Integer flowRankingToday;

    /**
     * 今日客流排名百分比
     */
    private BigDecimal flowRankingPercentageToday;

    /**
     * 是否有效 0：有效 1：无效
     */
    private Integer valid;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date updated;
}