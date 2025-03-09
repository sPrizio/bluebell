package com.bluebell.platform.models.core.entities.action.impl;

import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.models.core.entities.action.GenericAction;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.performable.ActionPerformable;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Represents something that a job does or performs
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Builder
@Table(name = "actions")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Action implements GenericAction, Comparable<Action> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String actionId = UUID.randomUUID().toString();

    @Setter
    @Column(nullable = false, unique = true)
    private int priority;

    @Setter
    @Column
    private String name;

    @Column
    private ActionStatus result = ActionStatus.NOT_STARTED;

    @Column(columnDefinition = "TEXT")
    private String logs = StringUtils.EMPTY;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;

    @Transient
    private ActionPerformable performableAction;


    //  CONSTRUCTORS

    @Builder
    private Action(final int priority, final String name, final Job job) {
        this.priority = priority;
        this.name = name;
        this.job = job;
    }


    //  METHODS

    @Override
    public ActionResult performAction() {
        //TODO: implement this
        return null;
    }

    @Override
    public int compareTo(final @NonNull Action o) {
        return Integer.compare(this.priority, o.priority);
    }
}
