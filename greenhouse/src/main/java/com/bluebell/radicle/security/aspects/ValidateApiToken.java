package com.bluebell.radicle.security.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bluebell.platform.models.core.entities.security.User;

/**
 * Annotation used to secure endpoints. Here, secure means to ensure that a valid API-token is provided and if so, attach the correct {@link User} to the request
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateApiToken {
}
