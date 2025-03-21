package com.bluebell.planter.converters.job;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.api.dto.job.JobDTO;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link JobDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class JobDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private JobDTOConverter jobDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.jobDTOConverter.convert(null))
                .isNotNull()
                .satisfies(JobDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.jobDTOConverter.convert(generateTestJob()))
                .isNotNull()
                .extracting("name", "type")
                .containsExactly("Test Job", EnumDisplay.builder().label(JobType.FETCH_MARKET_NEWS.getLabel()).code(JobType.FETCH_MARKET_NEWS.getCode()).build());

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.jobDTOConverter.convertAll(List.of(generateTestJob())))
                .isNotEmpty()
                .first()
                .extracting("name", "type")
                .containsExactly("Test Job", EnumDisplay.builder().label(JobType.FETCH_MARKET_NEWS.getLabel()).code(JobType.FETCH_MARKET_NEWS.getCode()).build());
    }
}
