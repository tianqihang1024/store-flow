package extend.es;

import extend.bean.StoreFlow;
import extend.param.SearchStoreFlowParam;
import extend.param.UpdateStoreFlowParam;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.List;

public interface StoreFlowEsService {


    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return
     */
    List<StoreFlow> findStoreFlowById(Long id);

    /**
     * 根据租户、创建时间查询
     *
     * @param searchStoreFlowParam
     * @return
     */
    SearchHits<StoreFlow> searchStoreFlowByParam(SearchStoreFlowParam searchStoreFlowParam);

    /**
     * 修改客流数据
     *
     * @param storeFlowParam 待修改的客流数据
     * @return
     */
    UpdateResponse updateStoreFlow(UpdateStoreFlowParam storeFlowParam);
}
