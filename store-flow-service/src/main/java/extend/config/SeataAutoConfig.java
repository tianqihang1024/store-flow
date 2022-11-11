package extend.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class SeataAutoConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    /**
     * druid数据源
     * @return
     */
    @Bean(name = "druidDataSource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        log.info("dataSourceProperties.getUrl():{}", dataSourceProperties.getUrl());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(0);
        druidDataSource.setMaxActive(180);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(0);
        druidDataSource.setValidationQuery("Select 1 from DUAL");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        log.info("装载dataSource........");
        return druidDataSource;
    }

    /**
     * init datasource proxy
     *
     * @Param: druidDataSource datasource bean instance
     * @Return: DataSourceProxy datasource proxy
     */
    @Bean(name = "dataSource")
    @Primary // 在同样的DataSource中，首先使用被标注的DataSource
    @DependsOn(value = {"druidDataSource"})
    public DataSourceProxy dataSourceProxy(@Qualifier(value = "druidDataSource") DruidDataSource druidDataSource) {
        log.info("代理dataSource........");
        return new DataSourceProxy(druidDataSource);
    }

    /**
     * init global transaction scanner
     *
     * @Return: GlobalTransactionScanner
     */
    @Bean
    @DependsOn(value = {"druidDataSource"})
    public GlobalTransactionScanner globalTransactionScanner() {
        log.info("配置seata........");
        return new GlobalTransactionScanner("store-flow-service", "my_test_tx_group");
    }
}