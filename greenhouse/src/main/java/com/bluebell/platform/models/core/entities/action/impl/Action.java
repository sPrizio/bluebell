package com.bluebell.platform.models.core.entities.action.impl;

import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.models.core.entities.action.GenericAction;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.ActionPerformable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Represents something that a job does or performs
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Slf4j
@Getter
@Entity
@Table(name = "actions", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "priority"}))
@NoArgsConstructor
@AllArgsConstructor
public class Action implements GenericAction, Comparable<Action> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String actionId = UUID.randomUUID().toString();

    @Setter
    @Column(nullable = false)
    @Min(1)
    @Max(99)
    private int priority;

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private ActionStatus status = ActionStatus.NOT_STARTED;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;

    @Setter
    @Transient
    private ActionPerformable performableAction;


    //  CONSTRUCTORS

    @Builder
    private Action(final int priority, final String name, final Job job, final ActionPerformable performableAction) {
        this.priority = priority;
        this.name = name;
        this.job = job;
        this.performableAction = performableAction;
    }


    //  METHODS

    @Override
    public int compareTo(final @NonNull Action o) {
        return Integer.compare(this.priority, o.priority);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        if (StringUtils.isEmpty(this.actionId)) return false;

        Action action = (Action) object;
        if (StringUtils.isEmpty(action.getActionId())) return false;
        return this.actionId.equals(action.actionId);
    }

    @Override
    public int hashCode() {
        return this.actionId.hashCode();
    }
}
