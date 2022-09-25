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
public class StoreFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @TableId
    @Field(type = FieldType.Long)
    private Long id;
    /**
     * FieldType.Text 和 FieldType.Keyword 的区别
     * Text在存储字符串数据的时候，会自动建立索引，占用部分空间资源。
     * Keyword存储字符串数据时，不会建立索引。
     * 相同点：两者都是字符串类型
     * <p>
     * analyzer 和 searchAnalyzer 的区别：
     * 分析器主要有两种情况会被使用：
     * 第一种是插入文档时，将text类型的字段做分词然后插入倒排索引，
     * 第二种就是在查询时，先对要查询的text类型的输入做分词，再去倒排索引搜索
     * 如果想要让 索引 和 查询 时使用不同的分词器，ElasticSearch也是能支持的，只需要在字段上加上search_analyzer参数
     * 在索引时，只会去看字段有没有定义analyzer，有定义的话就用定义的，没定义就用ES预设的
     * 在查询时，会先去看字段有没有定义search_analyzer，如果没有定义，就去看有没有analyzer，再没有定义，才会去使用ES预设的
     * <p>
     * ik_smart 和 ik_max_word
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
    private Long flowCount;
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

}