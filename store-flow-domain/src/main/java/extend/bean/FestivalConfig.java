package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 假期配置表
 *
 * @author 22489
 * @TableName festival_config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalConfig implements Serializable {

    private static final long serialVersionUID = 7583416394649375033L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 假期名称-中文
     */
    private String festivalNameCn;

    /**
     * 假期名称-英文
     */
    private String festivalNameEn;

    /**
     * 假期开始时间
     */
    private LocalDateTime festivalStartTime;

    /**
     * 假期结束时间
     */
    private LocalDateTime festivalEndTime;

    /**
     * 假期时长
     */
    private Integer festivalSize;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 修改时间
     */
    private LocalDateTime updated;

}