package extend.config;

import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/9/17 16:24
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hosts}")
    private String hosts;

    /**
     * 本人会将这个服务部署到云服务器，采用的docker方式，存在IP问题，
     * 如果是和es服务不在一台机器上，可以省略这个，直接写成es服务地址
     *
     * @return
     */
    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        String[] hosts = this.hosts.split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String host = hosts[i].split(":")[0];
            int port = Integer.parseInt(hosts[i].split(":")[1]);
            httpHosts[i] = new HttpHost(host, port, "http");
        }

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

        RestClientBuilder builder = RestClient.builder(httpHosts).setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(-1);
            requestConfigBuilder.setSocketTimeout(-1);
            requestConfigBuilder.setConnectionRequestTimeout(-1);
            return requestConfigBuilder;
        }).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        });
        return new RestHighLevelClient(builder);
    }

}
