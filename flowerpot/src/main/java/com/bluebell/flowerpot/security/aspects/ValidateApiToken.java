package com.bluebell.flowerpot.security.aspects;

import com.bluebell.flowerpot.core.models.entities.security.User;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to secure endpoints. Here, secure means to ensure that a valid API-token is provided and if so, attach the correct {@link User} to the request
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateApiToken {
}
