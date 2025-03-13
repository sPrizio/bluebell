package com.bluebell.radicle.services.job;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.JobExecutionException;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.services.action.ActionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link JobService}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class JobServiceTest extends AbstractGenericTest {

    @Autowired
    private ActionRepository actionRepository;

    @MockitoBean
    private ActionService actionService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @BeforeEach
    void setUp() {
        this.actionRepository.deleteAll();
        this.jobRepository.deleteAll();
    }


    //  ----------------- calculateJobDuration -----------------

    @Test
    void test_jobDuration_success() {

        final Job job = Job.builder().build();
        assertThatExceptionOfType(JobExecutionException.class)
                .isThrownBy(job::calculateJobDuration)
                .withMessageContaining("has not been started");

        job.setExecutionTime(LocalDateTime.of(2025, 3, 10, 13, 12, 11));
        assertThat(job.calculateJobDuration()).isEqualTo(-1L);

        job.setCompletionTime(LocalDateTime.of(2025, 3, 9, 13, 12, 11));
        assertThatExceptionOfType(JobExecutionException.class)
                .isThrownBy(job::calculateJobDuration)
                .withMessageContaining("completion time was before execution time");

        job.setCompletionTime(LocalDateTime.of(2025, 3, 10, 13, 13, 11));
        assertThat(job.calculateJobDuration()).isEqualTo(60L);
    }


    //  ----------------- executeJob -----------------

    @Test
    void test_executeJob_missingJob() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.executeJob(null))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_CANNOT_BE_NULL);
    }

    @Test
    void test_executeJob_noActions() {
        assertThatExceptionOfType(JobExecutionException.class)
                .isThrownBy(() -> this.jobService.executeJob(Job.builder().build()))
                .withMessageContaining("job has no actions!");
    }

    @Test
    void test_executeJob_failedJobOnArbitraryAction() {

        final Action action1 = Action.builder().priority(1).name("Action 1").build();
        action1.setActionId(UUID.randomUUID().toString());
        final Action action2 = Action.builder().priority(2).name("Action 2").build();
        action2.setActionId(UUID.randomUUID().toString());
        Mockito.when(this.actionService.performAction(action1)).thenReturn(ActionResult.builder().status(ActionStatus.SUCCESS).build());
        Mockito.when(this.actionService.performAction(action2)).thenReturn(ActionResult.builder().status(ActionStatus.FAILURE).build());

        final Job job = Job
                .builder()
                .name("Bad Job")
                .build();

        job.addAction(action1);
        job.addAction(action2);

        try {
            final JobResult jobResult = this.jobService.executeJob(job);
            assertThat(jobResult).isNotNull();
            assertThat(jobResult.getEntries()).hasSize(2);
            assertThat(jobResult.getEntries().get(0).isSuccess()).isTrue();
            assertThat(jobResult.getEntries().get(1).isSuccess()).isFalse();
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    void test_executeJob_success() {

        final Action action1 = Action.builder().priority(1).name("Action 1").build();
        action1.setActionId(UUID.randomUUID().toString());
        final Action action2 = Action.builder().priority(2).name("Action 2").build();
        action2.setActionId(UUID.randomUUID().toString());
        Mockito.when(this.actionService.performAction(any())).thenReturn(ActionResult.builder().status(ActionStatus.SUCCESS).build());

        final Job job = Job
                .builder()
                .name("Good Job")
                .build();

        job.addAction(action1);
        job.addAction(action2);

        try {
            final JobResult jobResult = this.jobService.executeJob(job);
            assertThat(jobResult).isNotNull();
            assertThat(jobResult.getEntries()).hasSize(2);
            assertThat(jobResult.getEntries().get(0).isSuccess()).isTrue();
            assertThat(jobResult.getEntries().get(1).isSuccess()).isTrue();
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


    //  ----------------- findJobByJobId -----------------

    @Test
    void test_findJobByJobId_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.findJobByJobId(null))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_ID_CANNOT_BE_NULL);

        final Job job = this.jobRepository.save(Job.builder().build());
        assertThat(job).isNotNull();
        assertThat(this.jobService.findJobByJobId(job.getJobId())).isNotNull();
    }


    //  ----------------- findJobsByStatus -----------------

    @Test
    void test_findJobsByStatus_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.findJobsByStatus(null))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);

        final Job job = this.jobRepository.save(Job.builder().build());
        assertThat(job).isNotNull();
        assertThat(this.jobService.findJobsByStatus(JobStatus.NOT_STARTED)).isNotNull();
        assertThat(this.jobService.findJobsByStatus(JobStatus.NOT_STARTED).get(0).getJobId()).isEqualTo(job.getJobId());
    }


    //  ----------------- findJobsByType -----------------

    @Test
    void test_findJobsByType_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.findJobsByType(null))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);

        final Job job = this.jobRepository.save(Job.builder().type(JobType.FETCH_MARKET_NEWS).build());
        assertThat(job).isNotNull();
        assertThat(this.jobService.findJobsByType(JobType.FETCH_MARKET_NEWS)).isNotNull();
        assertThat(this.jobService.findJobsByType(JobType.FETCH_MARKET_NEWS).get(0).getJobId()).isEqualTo(job.getJobId());
    }


    //  ----------------- findJobsByStatusAndType -----------------

    @Test
    void test_findJobsByStatusAndType_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.findJobsByStatusAndType(null, JobType.FETCH_MARKET_NEWS))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.jobService.findJobsByStatusAndType(JobStatus.IN_PROGRESS, null))
                .withMessageContaining(CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);

        final Job job = this.jobRepository.save(Job.builder().type(JobType.FETCH_MARKET_NEWS).build());
        assertThat(job).isNotNull();
        assertThat(this.jobService.findJobsByStatusAndType(JobStatus.NOT_STARTED, JobType.FETCH_MARKET_NEWS)).isNotNull();
        assertThat(this.jobService.findJobsByStatusAndType(JobStatus.NOT_STARTED, JobType.FETCH_MARKET_NEWS).get(0).getJobId()).isEqualTo(job.getJobId());
    }
}
