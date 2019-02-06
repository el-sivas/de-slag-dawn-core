package de.slag.dawn.core.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class ReflectionUtils {

	public static Optional<Field> getField(Object o, String fieldname) {
		final Collection<Field> fields = collectFields(o.getClass());
		final Stream<Field> filter = fields.stream().filter(f -> f.getName().equals(fieldname));
		return filter.findFirst();
	}

	public static Collection<Method> getAllGetterMethodsOf(Class<? extends Object> c) {
		final List<Method> asList = Arrays.asList(c.getDeclaredMethods());
		final List<Method> getterMethods = asList.stream().filter(m -> {
			final String methodName = m.getName().toLowerCase();
			return methodName.startsWith("get") || methodName.startsWith("is");
		}).collect(Collectors.toList());
		return getterMethods;
	}

	public static void set(Object obj, String fieldname, Object value) {
		final Collection<Field> collectFields = collectFields(obj);
		for (Field field : collectFields) {
			if (fieldname.equals(field.getName())) {
				field.setAccessible(true);
				try {
					field.set(obj, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BaseException(e);
				}
				field.setAccessible(false);
			}
		}
	}

	public static Collection<Field> collectFields(Object o) {
		return collectFields(o.getClass());
	}

	public static Collection<Field> collectFields(Class<?> clazz) {
		final Class<?> superclass = clazz.getSuperclass();
		final Collection<Field> fields = new ArrayList<>();
		if (superclass != null) {
			fields.addAll(collectFields(superclass));
		}

		final List<Field> fieldsOfThisClass = Arrays.asList(clazz.getDeclaredFields());
		fields.addAll(fieldsOfThisClass);
		return fields;
	}

	/**
	 * Properties are {@link Field}s and setter represented Properties of schema
	 * 'setXyz' -> 'xyz'.
	 */
	public static Collection<String> determinePropertyNames(Object object) {
		final Class<?> type = object.getClass();
		final Collection<String> properties = new TreeSet<>();

		properties.addAll(collectFields(type).stream().map(f -> f.getName()).collect(Collectors.toList()));

		final List<Method> setterMethods = Arrays.asList(type.getMethods())
				.stream()
				.filter(m -> m.getName().startsWith("set"))
				.collect(Collectors.toList());

		final List<String> collect = setterMethods.stream()
				.map(m -> m.getName().substring(3, m.getName().length()))
				.collect(Collectors.toList());

		properties.addAll(collect);
		return properties;
	}

	public static Collection<PropertySetter> determineSetter(Object object) {
		final Class<?> type = object.getClass();
		final Collection<PropertySetter> setters = new TreeSet<>(new Comparator<PropertySetter>() {

			@Override
			public int compare(PropertySetter o1, PropertySetter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final Collection<Field> collectFields = collectFields(type);
		collectFields.forEach(f -> setters.add(new PropertySetter(f.getName(), f, object, f.getType())));

		final List<Method> setterMethods = Arrays.asList(type.getMethods())
				.stream()
				.filter(m -> m.getName().startsWith("set"))
				.collect(Collectors.toList());

		for (Method setterMethod : setterMethods) {
			final String name = setterMethod.getName();
			final String propertyName = name.substring(3, name.length());
			setters.add(new PropertySetter(propertyName, setterMethod, object,
					Arrays.asList(setterMethod.getParameterTypes()).get(0)));
		}

		return setters;
	}

	public static Collection<PropertyGetter> determineGetter(Object object) {
		final Class<?> type = object.getClass();
		final Collection<PropertyGetter> getters = new TreeSet<>(new Comparator<PropertyGetter>() {

			@Override
			public int compare(PropertyGetter o1, PropertyGetter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final Collection<Field> collectFields = collectFields(type);
		collectFields.forEach(f -> getters.add(new PropertyGetter(f.getName(), f, object, f.getType())));

		final List<Method> getterMethods = Arrays.asList(type.getMethods())
				.stream()
				.filter(m -> m.getName().startsWith("set") || m.getName().startsWith("is"))
				.collect(Collectors.toList());

		for (Method getterMethod : getterMethods) {
			final String name = getterMethod.getName();
			final String propertyName;

			if (name.startsWith("get")) {
				propertyName = name.substring(3, name.length());
			} else {
				propertyName = name.substring(2, name.length());
			}

			getters.add(new PropertyGetter(propertyName, getterMethod, object, getterMethod.getReturnType()));
		}

		return getters;
	}
}
