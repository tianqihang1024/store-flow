package extend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import extend.bean.LargeDataScreen;
import extend.mapper.LargeDataScreenMapper;
import extend.service.LargeDataScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class LargeDataScreenServiceImpl extends ServiceImpl<LargeDataScreenMapper, LargeDataScreen> implements LargeDataScreenService {

    @Resource
    private LargeDataScreenMapper largeDataScreenMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchSomeColumn(List<LargeDataScreen> entityList) {
        int insertCount = largeDataScreenMapper.insertBatchSomeColumn(entityList);
        if (entityList.size() != insertCount) {
            throw new RuntimeException("未全部插入成功");
        }
        return insertCount;
    }
}
