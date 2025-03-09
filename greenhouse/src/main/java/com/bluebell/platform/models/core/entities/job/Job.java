package com.bluebell.platform.models.core.entities.job;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.action.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * An entity that performs actions and collects stats and logs about actions performed
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Builder
@Table(name = "jobs")
@NoArgsConstructor
@AllArgsConstructor
public class Job implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String jobId;

    @Column
    private String name;

    @Column(nullable = false)
    private LocalDateTime executionTime;

    @Column
    private LocalDateTime completionTime;

    @Column
    private JobStatus status;

    @Column
    private int retryCount;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("priority DESC")
    private Set<Action> actions;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link Action}
     */
    public void addAction(Action entry) {

        if (CollectionUtils.isEmpty(this.actions)) {
            this.actions = new TreeSet<>();
        }

        this.actions.add(entry);
        entry.setJob(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link Action}
     */
    public void removeEntry(Action entry) {
        if (CollectionUtils.isNotEmpty(this.actions)) {
            Set<Action> newActions = new TreeSet<>(this.actions);
            newActions.remove(entry);
            this.actions = newActions;
            entry.setJob(null);
        }
    }
}
