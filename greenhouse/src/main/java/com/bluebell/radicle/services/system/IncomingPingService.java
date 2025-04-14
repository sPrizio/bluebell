package com.bluebell.radicle.services.system;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.system.IncomingPing;
import com.bluebell.radicle.repositories.system.IncomingPingRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link IncomingPing}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Service
public class IncomingPingService {

    @Resource(name = "incomingPingRepository")
    private IncomingPingRepository incomingPingRepository;


    //  METHODS

    /**
     * Looks for the {@link IncomingPing} for the given system name
     *
     * @param systemName system name
     * @return {@link Optional} {@link IncomingPing}
     */
    public Optional<IncomingPing> findBySystemName(final String systemName) {
        validateParameterIsNotNull(systemName, CorePlatformConstants.Validation.System.IncomingPing.SYSTEM_NAME_CANNOT_BE_NULL);
        return Optional.ofNullable(this.incomingPingRepository.findIncomingPingBySystemName(systemName));
    }

    /**
     * Acknowledges the incoming ping and saves it to the database
     *
     * @param systemName system name
     * @return true if ping received and acknowledged
     */
    public boolean acknowledgeIncomingPing(final String systemName) {
        validateParameterIsNotNull(systemName, CorePlatformConstants.Validation.System.IncomingPing.SYSTEM_NAME_CANNOT_BE_NULL);

        final Optional<IncomingPing> ping = findBySystemName(systemName);
        if (ping.isEmpty()) {
            this.incomingPingRepository.save(
                    IncomingPing
                            .builder()
                            .systemName(systemName)
                            .lastSignalReceived(LocalDateTime.now())
                            .build()
            );
        } else {
            final IncomingPing incomingPing = ping.get();
            incomingPing.setLastSignalReceived(LocalDateTime.now());
            this.incomingPingRepository.save(incomingPing);
        }

        return true;
    }
}
