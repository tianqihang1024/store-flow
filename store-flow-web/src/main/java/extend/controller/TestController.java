package extend.controller;

import extend.tasks.StoreFlowTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 田奇杭
 * @Description
 * @Date 2022/8/20 17:06
 */
@Slf4j
@RestController
@RequestMapping(value = "/test/")
public class TestController {

    @Resource
    private StoreFlowTask storeFlowTask;

    @RequestMapping(value = "test")
    public void test() {
        storeFlowTask.storeFlowSave();
    }
}
