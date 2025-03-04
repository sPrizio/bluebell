package com.bluebell.radicle.security.aspects.impl;

import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.security.exceptions.InvalidApiTokenException;
import com.bluebell.radicle.security.services.ApiTokenService;
import com.bluebell.radicle.services.security.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation of the {@link ValidateApiToken} aspect. Here, the presence of the api token in the {@link HttpServletRequest} headers is checked and, if valid, the associated {@link User}
 * is attached and the request is allowed to proceed. Otherwise, the request is failed immediately
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Aspect
@Component
public class ValidateApiTokenAspect {

    @Value("${toggle.security}")
    private boolean toggleSecurity;

    @Value("${base.api.controller.endpoint}")
    private String baseApiUrl;

    @Resource(name = "apiTokenService")
    private ApiTokenService apiTokenService;

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS

    /**
     * Runs around the execution of methods where this annotation is used. Looks for the presence of an {@link HttpServletRequest} and
     * ensures that it contains the expected api token header. It then extracts said header and verifies whether the value contained
     * is a valid api token. Once validated, the associated {@link User} is injected into the request for use by the calling code
     *
     * @param proceedingJoinPoint {@link ProceedingJoinPoint}
     * @return the method's return value
     * @throws Throwable can throw any exception
     */
    @Around("@annotation(com.bluebell.radicle.security.aspects.ValidateApiToken)")
    public Object verifyApiToken(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        if (isTest()) {
            return proceedingJoinPoint.proceed();
        }

        final Object[] args = proceedingJoinPoint.getArgs();
        final ServletRequest request = getRequest(args);

        if (isNotSecured(request)) {
            return proceedingJoinPoint.proceed();
        } else if (request instanceof HttpServletRequest req) {
            if (StringUtils.isNotEmpty(req.getHeader(SecurityConstants.API_KEY_TOKEN))) {
                final String token = req.getHeader(SecurityConstants.API_KEY_TOKEN);
                final String email = this.apiTokenService.getEmailForApiToken(token);

                if (StringUtils.isNotEmpty(email)) {
                    final Optional<User> user = this.userService.findUserByEmail(email);
                    if (user.isPresent()) {
                        request.setAttribute(SecurityConstants.USER_REQUEST_KEY, user.get());
                        args[getIndexOfServletRequest(args)] = request;

                        return proceedingJoinPoint.proceed(args);
                    }
                } else {
                    throw new InvalidApiTokenException("The API token was not valid");
                }
            } else {
                throw new InvalidApiTokenException("The API token was not present in the request");
            }
        } else {
            throw new InvalidApiTokenException("Invalid or malformed request");
        }

        throw new InvalidApiTokenException("Invalid or malformed request");
    }


    //  HELPERS

    /**
     * Determines whether the incoming request should be secured
     *
     * @param request {@link ServletRequest}
     * @return true if it is part of the base api
     */
    private boolean isNotSecured(final ServletRequest request) {

        if (!this.toggleSecurity) {
            return true;
        }

        final String path = ((HttpServletRequest) request).getRequestURL().toString();
        return !path.contains(this.baseApiUrl);
    }

    /**
     * Obtains the {@link HttpServletRequest} from the method's parameters
     *
     * @param args method arguments
     * @return {@link HttpServletRequest}
     */
    private HttpServletRequest getRequest(final Object[] args) {
        final Object request = Arrays.stream(args).filter(HttpServletRequest.class::isInstance).findFirst().orElseThrow(() -> new UnsupportedOperationException("Servlet request was not included in the method parameters"));
        return (HttpServletRequest) request;
    }

    /**
     * Obtains the first index of an {@link HttpServletRequest}
     *
     * @param args method arguments
     * @return index of {@link HttpServletRequest}
     */
    private int getIndexOfServletRequest(final Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof HttpServletRequest) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns true if the calling method & class of this aspect are from the testing suite. Authorization should not take place
     * when testing
     *
     * @return true if code is executed from the testing suite
     */
    private boolean isTest() {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        return Arrays.stream(stackTrace).anyMatch(el -> el.getMethodName().startsWith("test")) &&
                Arrays.stream(stackTrace).anyMatch(el -> StringUtils.isNotEmpty(el.getFileName()) && el.getFileName().contains("Test.java"));
    }
}
