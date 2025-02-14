package com.bluebell.planterannotationprocessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that will generate API examples for use within @ExampleObjects
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface GenerateApiExamples {
}
