package de.slag.base.tools.mapping;

import java.util.function.Supplier;

public interface FieldMapper<T> extends Supplier<T> {

	Class<T> outputClass();

	Class<?> inputClass();

	default T map(Object o) {
		if (o == null) {
			return get();
		}
		return outputClass().cast(o);
	}
}
