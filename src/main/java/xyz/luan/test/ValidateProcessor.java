package xyz.luan.test;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.singletonList;

@AutoService(Processor.class)
public class ValidateProcessor extends AbstractProcessor {

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        messager = processingEnvironment.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new LinkedHashSet<>(singletonList(Validate.class.getCanonicalName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Validate.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                element.getEnclosedElements().stream()
                        .filter(e -> e.getKind() == ElementKind.FIELD)
                        .forEach(f -> {
                            String fieldName = f.getSimpleName().toString();
                            boolean constant = f.getModifiers().contains(Modifier.STATIC) || f.getModifiers().contains(Modifier.FINAL);
                            if (!constant && !isCamelCase(fieldName)) {
                                messager.printMessage(Diagnostic.Kind.ERROR, "Invalid field name: " + fieldName);
                            }
                        });
            }
        }
        return true;
    }

    private boolean isCamelCase(String value) {
        return Character.isLowerCase(value.charAt(0));
    }
}
