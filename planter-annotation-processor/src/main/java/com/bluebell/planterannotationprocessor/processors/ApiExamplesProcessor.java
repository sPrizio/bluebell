package com.bluebell.planterannotationprocessor.processors;

import com.bluebell.system.Currency;
import com.bluebell.system.PairEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * TODO
 * @author Stephen Prizio
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("com.bluebell.planterannotationprocessor.annotations.GenerateApiExamples")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class ApiExamplesProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExamplesProcessor.class);


    //  METHODS

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            FieldSpec currencyExample =
                    FieldSpec
                            .builder(String.class, "CURRENCY_EXAMPLE")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                            .initializer("$S", new ObjectMapper().writeValueAsString(Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList()))
                            .build();

            TypeSpec accountApiExamples =
                    TypeSpec
                            .classBuilder("AccountApiExamples")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addField(currencyExample)
                            .build();

            // Generate the file
            JavaFile javaFile =
                    JavaFile
                            .builder("com.bluebell.planterannotationprocessor.api.examples", accountApiExamples)
                            .build();

            javaFile.writeTo(this.processingEnv.getFiler());
            LOGGER.info("Generated Api Examples.");
        } catch (IOException e) {
            if (!(e instanceof FilerException)) {
                LOGGER.error("Code generation failed : {}", e.getMessage());
                LOGGER.error(e.getMessage(), e);
            }
        }

        return true;
    }
}
