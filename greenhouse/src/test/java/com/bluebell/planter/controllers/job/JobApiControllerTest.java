package com.bluebell.planter.controllers.job;

import com.bluebell.planter.AbstractPlanterTest;
import com.bluebell.planter.constants.ApiConstants;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.radicle.services.job.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link JobApiController}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
class JobApiControllerTest extends AbstractPlanterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;

    @MockitoBean
    private UniqueIdentifierService uniqueIdentifierService;

    @BeforeEach
    void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("1234");
        Mockito.when(this.jobService.findJobByJobId("1234")).thenReturn(Optional.of(generateTestJob()));
        Mockito.when(this.jobService.findJobByJobId("5678")).thenReturn(Optional.empty());
        Mockito.when(this.jobService.findJobsByStatusPaged(any(), any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(generateTestJob()), Pageable.ofSize(10), 10));
        Mockito.when(this.jobService.findJobsByTypePaged(any(), any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(generateTestJob()), Pageable.ofSize(10), 10));
        Mockito.when(this.jobService.findJobsByStatusAndTypePaged(any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(generateTestJob()), Pageable.ofSize(10), 10));
    }


    //  ----------------- getJobForJobId -----------------

    @Test
    void test_getJobForJobId_badId() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-by-id").queryParam("jobId", "5678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Job not found for job id : 5678")));
    }

    @Test
    void test_getJobForJobId_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-by-id").queryParam("jobId", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.name", is("Test Job")));
    }


    //  ----------------- getJobsWithinIntervalByStatusPaged -----------------

    @Test
    void test_getJobsWithinIntervalByStatusPaged_badParams() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-status-paged")
                        .queryParam("start", "adasdasdasd")
                        .queryParam("end", "2025-01-01")
                        .queryParam("jobStatus", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));

        this.mockMvc.perform(get("/api/v1/job/get-status-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "adasdasdasd")
                        .queryParam("jobStatus", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getJobsWithinIntervalByStatusPaged_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-status-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "2025-02-02")
                        .queryParam("jobStatus", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.jobs[0].name", is("Test Job")));
    }


    //  ----------------- getJobsWithinIntervalByTypePaged -----------------

    @Test
    void test_getJobsWithinIntervalByTypePaged_badParams() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-type-paged")
                        .queryParam("start", "adasdasdasd")
                        .queryParam("end", "2025-01-01")
                        .queryParam("jobType", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));

        this.mockMvc.perform(get("/api/v1/job/get-type-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "adasdasdasd")
                        .queryParam("jobType", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getJobsWithinIntervalByTypePaged_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-type-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "2025-02-02")
                        .queryParam("jobStatus", "COMPLETED")
                        .queryParam("jobType", "FETCH_MARKET_NEWS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.jobs[0].name", is("Test Job")));
    }

    //  ----------------- getJobsWithinIntervalByStatusAndTypePaged -----------------

    @Test
    void test_getJobsWithinIntervalByStatusAndTypePaged_badParams() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-status-type-paged")
                        .queryParam("start", "adasdasdasd")
                        .queryParam("end", "2025-01-01")
                        .queryParam("jobStatus", "COMPLETED")
                        .queryParam("jobType", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));

        this.mockMvc.perform(get("/api/v1/job/get-status-type-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "adasdasdasd")
                        .queryParam("jobStatus", "COMPLETED")
                        .queryParam("jobType", "COMPLETED")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    void test_getJobsWithinIntervalByStatusAndTypePaged_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/job/get-status-type-paged")
                        .queryParam("start", "2025-01-01")
                        .queryParam("end", "2025-02-02")
                        .queryParam("jobStatus", "COMPLETED")
                        .queryParam("jobType", "FETCH_MARKET_NEWS")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.jobs[0].name", is("Test Job")));
    }
}
