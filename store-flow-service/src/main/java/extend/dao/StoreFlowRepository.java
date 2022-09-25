package extend.dao;

import extend.bean.StoreFlow;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/9/17 18:10
 */
public interface StoreFlowRepository extends ElasticsearchRepository<StoreFlow, Long> {

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    List<StoreFlow> findStoreFlowsById(Long id);

}
