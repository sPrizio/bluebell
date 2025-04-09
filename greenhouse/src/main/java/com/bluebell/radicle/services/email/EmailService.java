package com.bluebell.radicle.services.email;


import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sends an email using the email system to the specified recipient
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
public interface EmailService {

    /**
     * Sends an email to the recipient at the given to address
     *
     * @param to recipient
     * @param subject email subject
     * @param emailTemplate {@link EmailTemplate}
     */
    void sendEmail(final String to, final String subject, final EmailTemplate emailTemplate);


    //  HELPERS

    /**
     * Interpolates the string with the given values
     *
     * @param emailTemplate {@link EmailTemplate}
     * @return updated string
     */
    default String interpolate(final EmailTemplate emailTemplate) {

        Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z_]+)}");
        Matcher matcher = pattern.matcher(emailTemplate.getTemplate());
        StringBuilder result = new StringBuilder();
        Set<String> missingKeys = new HashSet<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            if (!emailTemplate.getQueryParams().containsKey(key)) {
                missingKeys.add(matcher.group(0));
                continue;
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(emailTemplate.getQueryParams().get(key)));
        }

        matcher.appendTail(result);
        if (!missingKeys.isEmpty()) {
            throw new IllegalParameterException("Missing replacements for placeholders: " + missingKeys);
        }

        return result.toString();
    }
}
