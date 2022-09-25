package extend.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺客流表-性别
 *
 * @TableName store_flow_sex
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreFlowSex implements Serializable {

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
     * 男性
     */
    private Long maleCount;
    /**
     * 女性
     */
    private Long femaleCount;
    /**
     * 未知
     */
    private Long unknownCount;
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