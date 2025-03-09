package com.bluebell.platform.models.core.entities.action;

import com.bluebell.platform.enums.action.ActionResult;
import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.job.Job;
import jakarta.persistence.*;
import lombok.*;

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
public class Action implements GenericEntity, Comparable<Action> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String actionId;

    @Setter
    @Column(nullable = false, unique = true)
    private int priority;

    @Setter
    @Column
    private String name;

    @Setter
    @Column
    private ActionResult result;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String logs;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Job job;


    //  METHODS

    @Override
    public int compareTo(final @NonNull Action o) {
        return Integer.compare(this.priority, o.priority);
    }
}
