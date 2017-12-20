package xyz.luan.test;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class ErrorProcessor extends AbstractProcessor {

	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		messager = processingEnv.getMessager();
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return new HashSet<>(Collections.singletonList(Error.class.getCanonicalName()));
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		roundEnv.getElementsAnnotatedWith(Error.class)
				.stream()
				.filter(e -> e.getKind() == ElementKind.CLASS)
				.flatMap(e -> e.getEnclosedElements().stream())
				.filter(e -> e.getKind() == ElementKind.FIELD)
				.filter(e -> !e.getModifiers().contains(Modifier.STATIC) && !e.getModifiers().contains(Modifier.FINAL))
				.forEach(e -> {
					String name = e.getSimpleName().toString();
					if (!isCamelCase(name)) {
						messager.printMessage(Diagnostic.Kind.ERROR, "Invalid field name; must be camelCase; found: " + name, e);
					}
				});
		return true;
	}

	private boolean isCamelCase(String name) {
		return Character.isLowerCase(name.charAt(0));
	}
}
