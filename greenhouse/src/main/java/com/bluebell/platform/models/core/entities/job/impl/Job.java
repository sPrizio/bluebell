package com.bluebell.platform.models.core.entities.job.impl;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.JobExecutionException;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.GenericJob;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * An entity that performs actions and collects stats and logs about actions performed
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
@Getter
@Entity
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
public class Job implements GenericJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false, unique = true)
    private String jobId = UUID.randomUUID().toString();

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private LocalDateTime executionTime = null;

    @Setter
    @Column
    private LocalDateTime completionTime = null;

    @Setter
    @Column
    private JobStatus status = JobStatus.NOT_STARTED;

    @Setter
    @Column
    private JobType type;

    @Setter
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("priority DESC")
    private Set<Action> actions;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_result_id", referencedColumnName = "job_id")
    private JobResult jobResult;


    //  CONSTRUCTORS

    @Builder
    private Job(final String name, final JobType type) {
        this.name = name;
        this.type = type;
    }


    //  METHODS

    /**
     * Calculates the job's duration in seconds. If the job is in progress, -1 will be returned
     *
     * @return difference between completion and execution time (in seconds)
     */
    public long calculateJobDuration() {

        if (this.executionTime == null) {
            throw new JobExecutionException(String.format("Job %s has not been started", this.name));
        }

        if (this.completionTime == null) {
            return -1L;
        }

        if (this.executionTime.isAfter(this.completionTime)) {
            throw new JobExecutionException(String.format("Job %s completion time was before execution time", this.name));
        }

        return Math.abs(ChronoUnit.SECONDS.between(this.executionTime, this.completionTime));
    }

    /**
     * Database assistance method
     *
     * @param entry {@link Action}
     */
    public void addAction(final Action entry) {

        if (CollectionUtils.isEmpty(this.actions)) {
            this.actions = new TreeSet<>(Comparator.comparing(Action::getPriority));
        }

        this.actions.add(entry);
        entry.setJob(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link Action}
     */
    public void removeEntry(final Action entry) {
        if (CollectionUtils.isNotEmpty(this.actions)) {
            final Set<Action> newActions = new TreeSet<>(Comparator.comparing(Action::getPriority));
            newActions.addAll(this.actions);
            newActions.remove(entry);
            this.actions = newActions;
            entry.setJob(null);
        }
    }
}
