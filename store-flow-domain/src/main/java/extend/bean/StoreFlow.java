package extend.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺客流表
 *
 * @TableName store_flow
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "store_flow", shards = 2, replicas = 1)
public class StoreFlow implements Serializable, Comparable<StoreFlow>{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @TableId
    @Field(type = FieldType.Long)
    private Long id;
    /**
     * 租户ID
     */
    @Field(type = FieldType.Keyword, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String tenantId;
    /**
     * 店铺ID
     */
    @Field(type = FieldType.Keyword, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String storeId;
    /**
     * 客流量
     */
    @Field(type = FieldType.Long)
    private Integer flowCount;
    /**
     * 是否有效 0：有效 1：失效
     */
    @Field(type = FieldType.Integer)
    private Integer valid;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "epoch_millis")
    private LocalDateTime updateTime;

    /**
     * 默认降序排列，有特殊要求，请自定义
     * @param storeFlow the object to be compared.
     * @return
     */
    @Override
    public int compareTo(StoreFlow storeFlow) {
        if(null == storeFlow || null == storeFlow.getFlowCount()) {
            return -1;
        }
        if(null == this.getFlowCount()) {
            return 1;
        }
        return storeFlow.getFlowCount().compareTo(this.getFlowCount());
    }

    public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
    }

}