module com.bluebell.planterannotationprocessor {
    exports com.bluebell.planterannotationprocessor.annotations;
    exports com.bluebell.planterannotationprocessor.api.examples;
    requires com.google.auto.service;
    requires com.squareup.javapoet;
    requires org.slf4j;
    requires java.compiler;
}