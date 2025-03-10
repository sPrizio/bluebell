package com.bluebell.platform.models.core.entities.job.impl;

import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a result of executing a {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Entity
@Table(name = "job_results")
@NoArgsConstructor
@AllArgsConstructor
public class JobResult implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id", referencedColumnName = "id")
    private Job job;

    @OneToMany(mappedBy = "job_result", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobResultEntry> entries;


    //  CONSTRUCTORS

    @Builder
    private JobResult(final List<JobResultEntry> entries) {
        this.entries = entries;
    }
}
