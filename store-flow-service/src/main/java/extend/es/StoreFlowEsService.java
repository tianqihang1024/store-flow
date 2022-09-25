package extend.es;

import extend.bean.StoreFlow;
import extend.param.SearchStoreFlowParam;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface StoreFlowEsService {

    void saveTestData();

    /**
     * 根据 id 查询
     *
     * @param id
     * @return
     */
    List<StoreFlow> findStoreFlowById(Long id);

    /**
     * 根据 tenantId 查询
     *
     * @param searchStoreFlowParam
     * @return
     */
    SearchHits<StoreFlow> searchStoreFlowByParam(SearchStoreFlowParam searchStoreFlowParam);
}
