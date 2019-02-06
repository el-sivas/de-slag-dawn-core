package de.slag.dawn.core.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FieldMapperFactory {

	public static <T> FieldMapper<T> create(Class<?> from, Class<T> to, T defaultValue) {
		return new FieldMapper<T>() {

			@Override
			public T get() {
				return defaultValue;
			}

			@Override
			public Class<T> outputClass() {
				return to;
			}

			@Override
			public Class<?> inputClass() {
				return from;
			}
		};

	}

	private static <S, T> Collection<FieldMapper<?>> createViceVersa(Class<S> from, Class<T> to, Object defaultValue) {
		return Arrays.asList(create(from, to, to.cast(defaultValue)), create(to, from, from.cast(defaultValue)));
	}

	public static Collection<FieldMapper<?>> createPrimitiveMapper() {
		final Collection<FieldMapper<?>> primitives = new ArrayList<>();

		primitives.addAll(createViceVersa(Short.class, short.class, 0));
		primitives.addAll(createViceVersa(Byte.class, byte.class, 0));		
		primitives.addAll(createViceVersa(Integer.class, int.class, 0));
		primitives.addAll(createViceVersa(Long.class, long.class, 0));

		primitives.addAll(createViceVersa(Double.class, double.class, 0));
		primitives.addAll(createViceVersa(Float.class, float.class, 0));
		
		primitives.addAll(createViceVersa(Character.class, char.class, ' '));
		
		primitives.addAll(createViceVersa(Boolean.class, boolean.class, false));		


		return primitives;

	}
}
