package extend.tasks;

import extend.bean.FestivalConfig;

import java.time.LocalDateTime;
import java.util.List;

public interface FestivalConfigService {

    void syncHolidayConfig(LocalDateTime taskStartTime);

    int updateHolidayConfig(List<FestivalConfig> festivalConfigList);


}
