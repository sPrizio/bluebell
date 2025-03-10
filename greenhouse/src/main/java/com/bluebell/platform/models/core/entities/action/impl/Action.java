package com.bluebell.platform.models.core.entities.action.impl;

import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.models.core.entities.action.GenericAction;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.performable.ActionPerformable;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Represents something that a job does or performs
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
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
    private ActionStatus status = ActionStatus.NOT_STARTED;

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

        //TODO: extrapolate to ActionService performAction(Action)
        LOGGER.info("Performing action {}", this.name);

        final ActionResult result = ActionResult
                .builder()
                .status(ActionStatus.IN_PROGRESS)
                .build();

        final ActionData actionData = this.performableAction.perform();
        if (actionData != null && actionData.isSuccess()) {
            result.setData(actionData);
            result.setStatus(ActionStatus.SUCCESS);
            LOGGER.info("Action {} completed successfully", this.name);
        } else {
            result.setStatus(ActionStatus.FAILURE);
            LOGGER.info("Action {} failed. Consult logs for further information", this.name);
        }

        return result;
    }

    @Override
    public int compareTo(final @NonNull Action o) {
        return Integer.compare(this.priority, o.priority);
    }
}
