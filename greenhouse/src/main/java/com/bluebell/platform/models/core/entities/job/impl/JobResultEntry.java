package com.bluebell.platform.models.core.entities.job.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an entry inside a {@link JobResult} which maps semantically to an {@link ActionResult}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Table(name = "job_result_entries")
@NoArgsConstructor
@AllArgsConstructor
public class JobResultEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String data;

    @Column(columnDefinition = "TEXT")
    private String logs;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private JobResult jobResult;


    //  CONSTRUCTORS

    @Builder
    private JobResultEntry(final boolean success, final String data, final String logs) {
        this.success = success;
        this.data = data;
        this.logs = logs;
    }
}
