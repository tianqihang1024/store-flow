package extend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/11/6 20:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "article_index", shards = 2)
public class Article {

    @Field(type = FieldType.Long)
    private Long id;

    /**
     * todo 嵌套对象里的string属性，查询时会报错： nested object under path [author] is not of nested type" 待解决
     */
    @Field(type = FieldType.Nested)
    private Author author;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String context;

    /**
     * todo 待解决时间戳问题，存储进es会变成字符串，再取数据时会转换错误
     */
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime created;
}
