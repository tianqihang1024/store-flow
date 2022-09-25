package extend.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺客流表-年龄表
 *
 * @TableName store_flow_age
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreFlowAge implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
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
     * 0-10
     */
    private Long age1;
    /**
     * 11-20
     */
    private Long age2;
    /**
     * 21-30
     */
    private Long age3;
    /**
     * 31-40
     */
    private Long age4;
    /**
     * 41-50
     */
    private Long age5;
    /**
     * 51-60
     */
    private Long age6;
    /**
     * 61以上
     */
    private Long age7;
    /**
     * 是否有效 0：有效 1：失效
     */
    private Integer valid;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

}