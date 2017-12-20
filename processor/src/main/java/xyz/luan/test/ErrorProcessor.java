package xyz.luan.test;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class ErrorProcessor extends AbstractProcessor {

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
				.forEach(e -> {
					String name = e.getSimpleName().toString();
					System.out.println(name);
				});
		return true;
	}
}
