package com.bluebell.planter.converters.job;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.job.JobResultEntryDTO;
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
 * Testing class for {@link JobResultEntryDTOConverter}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class JobResultEntryDTOConverterTest extends AbstractPlanterTest {

    @Autowired
    private JobResultEntryDTOConverter jobResultEntryDTOConverter;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    void test_convert_success_emptyResult() {
        assertThat(this.jobResultEntryDTOConverter.convert(null))
                .isNotNull()
                .satisfies(JobResultEntryDTO::isEmpty);

    }

    @Test
    void test_convert_success() {
        assertThat(this.jobResultEntryDTOConverter.convert(generateTestJobResultEntry()))
                .isNotNull()
                .extracting("success", "data", "logs")
                .containsExactly(1, "Test Data", "This is a log message");

    }


    //  ----------------- convertAll -----------------

    @Test
    void test_convertAll_success() {
        assertThat(this.jobResultEntryDTOConverter.convertAll(List.of(generateTestJobResultEntry())))
                .isNotEmpty()
                .first()
                .extracting("success", "data", "logs")
                .containsExactly(1, "Test Data", "This is a log message");
    }
}
