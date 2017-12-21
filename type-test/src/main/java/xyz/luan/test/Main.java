package xyz.luan.test;

import xyz.luan.reflection.tclass.TypedClass;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private @Value("outer") List<@Value("inner") String> strs = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		TypedClass<?> c = TypedClass.create(Main.class.getDeclaredField("strs"));
		System.out.println(((Value) c.getAnnotations().get(0)).value());

		List<Annotation> list = c.asList().getComponent().getAnnotations();
		System.out.println(((Value) list.get(0)).value());
	}
}
