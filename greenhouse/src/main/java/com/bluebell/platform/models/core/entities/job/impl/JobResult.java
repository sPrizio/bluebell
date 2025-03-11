package com.bluebell.platform.models.core.entities.job.impl;

import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "jobResult", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JobResultEntry> entries;


    //  CONSTRUCTORS

    @Builder
    private JobResult(final List<JobResultEntry> entries) {
        this.entries = entries;
    }


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link JobResultEntry}
     */
    public void addJobResultEntry(final JobResultEntry entry) {

        if (CollectionUtils.isEmpty(this.entries)) {
            this.entries = new ArrayList<>();
        }

        this.entries.add(entry);
        entry.setJobResult(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link JobResultEntry}
     */
    public void removeJobResultEntry(final JobResultEntry entry) {
        if (CollectionUtils.isNotEmpty(this.entries)) {
            List<JobResultEntry> jobResultEntries = new ArrayList<>(this.entries);
            jobResultEntries.remove(entry);
            this.entries = jobResultEntries;
            entry.setJobResult(null);
        }
    }
}
