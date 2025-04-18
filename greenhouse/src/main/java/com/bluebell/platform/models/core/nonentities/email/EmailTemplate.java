package com.bluebell.platform.models.core.nonentities.email;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * Defines a template for sending emails, including body structure and required params
 *
 * @param template email template
 * @param queryParams interpolation variables
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Builder
public record EmailTemplate(
        @Getter String template,
        @Getter Map<String, String> queryParams
) { }
