package extend.tasks.impl;

import extend.bean.FestivalConfig;
import extend.mapper.FestivalConfigMapper;
import extend.tasks.FestivalConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FestivalConfigServiceImplTest {

    @Mock
    private FestivalConfigService mockFestivalConfigService;

    @Mock
    private FestivalConfigMapper mockFestivalConfigMapper;

    @InjectMocks
    private FestivalConfigServiceImpl festivalConfigServiceImplUnderTest;

    @Test
    public void testSyncHolidayConfig() {
        // Setup
        // Configure FestivalConfigMapper.getFestivalConfigByNameCn(...).
        final List<FestivalConfig> festivalConfigs = Collections.singletonList(
                new FestivalConfig(0, "festivalNameCn", "festivalNameEn", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0)));
        when(mockFestivalConfigMapper.getFestivalConfigByNameCn(Collections.singletonList("value"))).thenReturn(festivalConfigs);

        Mockito.when(mockFestivalConfigService.updateHolidayConfig(Mockito.anyList())).thenReturn(0);

        // Run the test
        festivalConfigServiceImplUnderTest.syncHolidayConfig(LocalDateTime.of(2020, 10, 1, 0, 0, 0));

    }

    @Test
    public void testUpdateHolidayConfig() {
        // Setup
        final List<FestivalConfig> festivalConfigList = Arrays.asList(
                new FestivalConfig(0, "festivalNameCn", "festivalNameEn", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0)));
        when(mockFestivalConfigMapper.updateHolidayConfig(Arrays.asList(
                new FestivalConfig(0, "festivalNameCn", "festivalNameEn", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0), 0, LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0))))).thenReturn(0);

        // Run the test
        festivalConfigServiceImplUnderTest.updateHolidayConfig(festivalConfigList);

    }
}
