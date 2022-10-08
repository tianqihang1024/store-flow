package extend.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/9/17 16:24
 */
@Configuration
public class ElasticsearchRestTemplateConfig {

    @Value("${spring.elasticsearch.rest.ip}")
    private String uris;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        int i = Runtime.getRuntime().availableProcessors();
        String uri = i > 4 ? uris : "172.17.0.4";
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(uri, 9200, "http")
                )
        );
    }

}
