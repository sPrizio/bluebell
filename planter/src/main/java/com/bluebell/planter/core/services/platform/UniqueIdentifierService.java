package com.bluebell.planter.core.services.platform;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.models.entities.GenericEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service for handling computations regarding unique identifiers that are typical used on {@link GenericDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Service
public class UniqueIdentifierService {


    //  METHODS

    /**
     * Generates the uid from the given {@link GenericEntity}
     *
     * @param entity {@link GenericEntity}
     * @return uid
     */
    public String generateUid(final GenericEntity entity) {
        validateParameterIsNotNull(entity, "entity cannot be null");
        return Base64.encodeBase64String(entity.getId().toString().getBytes());
    }

    /**
     * Retrieves the id for an entity from the given {@link GenericDTO}
     *
     * @param uid unique identifier
     * @return id
     */
    public long retrieveId(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);

        if (StringUtils.isEmpty(uid)) {
            throw new UnsupportedOperationException("uid is missing");
        }

        return Long.parseLong(new String(Base64.decodeBase64(uid), StandardCharsets.UTF_8));
    }

    /**
     * Retrieves the id for an entity from the given uid
     *
     * @param uid uid
     * @return id
     */
    public long retrieveIdForUid(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.DataIntegrity.UID_CANNOT_BE_NULL);
        return Long.parseLong(new String(Base64.decodeBase64(uid), StandardCharsets.UTF_8));
    }
}
