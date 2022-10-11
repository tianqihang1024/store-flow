package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 假期配置表
 * @TableName holiday_config
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayConfig implements Serializable {

    private static final long serialVersionUID = 7583416394649375033L;

    /**
     * 
     */
    private Integer id;

    /**
     * 假期名称-中文
     */
    private String holidayNameCn;

    /**
     * 假期名称-英文
     */
    private String holidayNameEn;

    /**
     * 假期开始时间
     */
    private Date holidayStartTime;

    /**
     * 假期结束时间
     */
    private Date holidayEndTime;

    /**
     * 假期时长
     */
    private Integer holidaySize;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 修改时间
     */
    private Date updated;

}