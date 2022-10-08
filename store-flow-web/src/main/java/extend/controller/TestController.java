package extend.controller;

import extend.bean.StoreFlow;
import extend.es.StoreFlowEsService;
import extend.param.SearchStoreFlowParam;
import extend.tasks.StoreFlowTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 17:06
 */
@Slf4j
@RestController
@RequestMapping(value = "/storeFlow/es/")
public class TestController {

    @Resource
    private StoreFlowEsService storeFlowEsService;

    @Resource
    private StoreFlowTask storeFlowTask;

    @RequestMapping("storeFlowSave")
    public void storeFlowSave(long value) {
        log.info("当前线程为：{}", Thread.currentThread().getName());
        storeFlowTask.storeFlowSave(value);
    }

    @RequestMapping("syncDataToEs")
    public void syncDataToEs(long minId) {
        log.info("当前线程为：{}", Thread.currentThread().getName());
        storeFlowTask.syncDataEs(minId);
    }

    @RequestMapping(value = "saveTestData")
    public void saveTestData() {
        storeFlowEsService.saveTestData();
    }

    @RequestMapping(value = "findStoreFlowById")
    public List<StoreFlow> findStoreFlowById(Long id) {
        return storeFlowEsService.findStoreFlowById(id);
    }

    @RequestMapping(value = "searchStoreFlowByParam")
    public SearchHits<StoreFlow> searchStoreFlowByParam(@RequestBody SearchStoreFlowParam searchStoreFlowParam) {
        return storeFlowEsService.searchStoreFlowByParam(searchStoreFlowParam);
    }

}

