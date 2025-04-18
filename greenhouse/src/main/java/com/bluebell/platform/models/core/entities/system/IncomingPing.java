package com.bluebell.platform.models.core.entities.system;

import com.bluebell.platform.models.core.entities.GenericEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Class representation of a heartbeat style check-in with external services
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Getter
@Entity
@Builder
@Table(name = "incoming_pings", uniqueConstraints = @UniqueConstraint(name = "UniqueSystemName", columnNames = {"system_name"}))
@NoArgsConstructor
@AllArgsConstructor
public class IncomingPing implements GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column
    private LocalDateTime lastSignalReceived;

    @Setter
    @Column(name = "system_name", nullable = false)
    private String systemName;
}
