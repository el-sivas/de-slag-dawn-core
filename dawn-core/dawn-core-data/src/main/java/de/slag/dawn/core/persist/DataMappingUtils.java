package de.slag.dawn.core.persist;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.slag.root.base.BaseException;

public final class DataMappingUtils {

	private static final Log LOG = LogFactory.getLog(DataMappingUtils.class);

	private static final Collection<String> NO_MAPPING_PROPERTIES = Arrays.asList("ID", "CLASS");

	public static void map(Object from, Object to) {

		final List<Method> getterMethods = getterMethods(from);
		final Map<String, Method> propertyMappedGetterMethods = new HashMap<>();

		getterMethods.forEach(m -> propertyMappedGetterMethods.put(property(m), m));
		NO_MAPPING_PROPERTIES.forEach(p -> propertyMappedGetterMethods.remove(p));

		for (String property : propertyMappedGetterMethods.keySet()) {
			final Optional<Method> potentialSetterMethod = setterMethod(to, property);
			if (!potentialSetterMethod.isPresent()) {
				LOG.warn("no setter method found for: " + property);
				continue;
			}
			final Method setterMethod = potentialSetterMethod.get();
			final Method getterMethod = propertyMappedGetterMethods.get(property);
			
			try {
				setterMethod.invoke(to, getterMethod.invoke(from));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new BaseException(e);
			}
			propertyMappedGetterMethods.remove(property);	
		}

	}

	private static Optional<Method> setterMethod(Object to, String property) {
		final List<Method> asList = Arrays.asList(to.getClass().getDeclaredMethods());
		return asList.stream().filter(m -> {
			final String name = m.getName();
			final String upperCase = name.toUpperCase();
			return upperCase.equals("SET" + property);
		}).findAny();
	}

	private static String property(Method m) {
		final String name = m.getName();
		return name.substring(3, name.length()).toUpperCase();
	}

	private static List<Method> getterMethods(Object to) {
		final Class<? extends Object> class1 = to.getClass();

		final List<Method> asList = Arrays.asList(class1.getMethods());

		return asList.stream().filter(m -> m.getName().startsWith("get")).collect(Collectors.toList());
	}

}
