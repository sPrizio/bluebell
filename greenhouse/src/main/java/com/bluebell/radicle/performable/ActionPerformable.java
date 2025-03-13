package com.bluebell.radicle.performable;

import com.bluebell.platform.models.core.nonentities.action.ActionData;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Describes the process of performing an action, i.e. doing something
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface ActionPerformable {

    /**
     * Performs the performable action
     *
     * @return {@link ActionData}
     */
    ActionData perform();

    /**
     * Turns an exception's stack trace into a formatted string
     *
     * @param e {@link Throwable}
     * @return string
     */
    default String getStackTraceAsString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
