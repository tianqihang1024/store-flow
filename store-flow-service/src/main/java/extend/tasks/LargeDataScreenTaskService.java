package extend.tasks;

import java.time.LocalDateTime;

public interface LargeDataScreenTaskService {

    void customerFlowRankingOfStoresByTenant(LocalDateTime taskStartTime);

    void updateHolidayConfig(LocalDateTime taskStartTime);
}
