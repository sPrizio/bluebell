package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.entities.system.IncomingPing;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.system.IncomingPingService;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Implementation of {@link ActionPerformable} that looks for the given system name and obtains
 * the most recent ping (if it exists). If the ping does not exist or is older thant the lookback period
 * this action will fail and the job will notify the system that a ping has not been received
 * in a timely manner
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Component("pingMiddlewareActionPerformable")
@Scope("prototype")
public class PingMiddlewareActionPerformable implements ActionPerformable {

    @Setter
    private String systemName;

    @Value("${bluebell.incoming.ping.acknowledgement.lookback}")
    private String lookback;

    @Resource(name = "incomingPingService")
    private IncomingPingService incomingPingService;


    //  METHODS

    @Override
    public ActionData perform() {

        final Optional<IncomingPing> ping = this.incomingPingService.findBySystemName(this.systemName);
        if (ping.isEmpty()) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(String.format("No ping found for system name %s", this.systemName))
                    .build();
        }

        final IncomingPing incomingPing = ping.get();
        if (incomingPing.getLastSignalReceived() == null) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(String.format("No signal received from system %s", this.systemName))
                    .build();
        }

        final long lookbackPeriod = Long.parseLong(this.lookback);
        if (Math.abs(ChronoUnit.SECONDS.between(incomingPing.getLastSignalReceived(), LocalDateTime.now())) > lookbackPeriod) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(String.format("System %s has not phoned home within the allotted time.", this.systemName))
                    .build();
        }

        return ActionData
                .builder()
                .success(true)
                .logs(String.format("Healthy signal received from %s", this.systemName))
                .build();
    }
}
