package xyz.luan.test;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

public class ValidateProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(singletonList(Validate.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (TypeElement element : set) {
            if (element.getKind() == ElementKind.CLASS) {
                element.getEnclosedElements().stream()
                        .filter(e -> e.getKind() == ElementKind.FIELD)
                        .forEach(f -> {
                            String fieldName = f.getSimpleName().toString();
                            messager.printMessage(Diagnostic.Kind.ERROR, "Invalid field name: " + fieldName);
                        });
            }
        }
        return true;
    }
}
