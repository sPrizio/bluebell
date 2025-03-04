package com.bluebell.planter.converters.system;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.system.PhoneNumberDTO;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Converter that converts {@link PhoneNumber}s into {@link PhoneNumberDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Component("phoneNumberDTOConverter")
public class PhoneNumberDTOConverter implements GenericDTOConverter<PhoneNumber, PhoneNumberDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public PhoneNumberDTO convert(final PhoneNumber entity) {

        if (entity == null) {
            return new PhoneNumberDTO();
        }

        final PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();

        phoneNumberDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        phoneNumberDTO.setPhoneType(entity.getPhoneType().name());
        phoneNumberDTO.setTelephoneNumber(entity.getTelephoneNumber());
        phoneNumberDTO.setCountryCode(entity.getCountryCode());
        phoneNumberDTO.setDisplay(getDisplayString(entity));

        return phoneNumberDTO;
    }


    //  HELPERS

    /**
     * Displays a phone number string
     *
     * @param entity {@link PhoneNumber}
     * @return {@link String}
     */
    private String getDisplayString(final PhoneNumber entity) {

        final String num = "+" + entity.getCountryCode() + entity.getTelephoneNumber();
        final Pattern pattern = Pattern.compile(CorePlatformConstants.Regex.PHONE_NUMBER_REGEX);
        final Matcher matcher = pattern.matcher(num);

        if (matcher.find()) {
            return "+" + matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3) + " " + matcher.group(4);
        }

        return StringUtils.EMPTY;
    }
}
