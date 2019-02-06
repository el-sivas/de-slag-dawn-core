package de.slag.dawn.core.utils;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ClassUtils {

	private static final Log LOG = LogFactory.getLog(ClassUtils.class);

	private static final Map<String, Collection<Class>> CACHE = new HashMap<>();

	public static Collection<Class> getAllSubclassesOf(Class superclass) {
		return getAllSubclassesOf(superclass, "de/slag");
	}

	public static Collection<Class> getAllSubclassesOf(Class superclass, String basePackage) {
		final String key = key(superclass, basePackage);
		if (!CACHE.containsKey(key)) {
			CACHE.put(key, find(superclass, basePackage));
		}
		return CACHE.get(key);
	}

	private static Collection<Class> find(Class superclass, String basePackage) {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);

		provider.addIncludeFilter(new AssignableTypeFilter(superclass));

		final Set<BeanDefinition> components = provider.findCandidateComponents(basePackage);
		final Collection<Class> r = new ArrayList<>();
		for (BeanDefinition component : components) {
			String beanClassName = component.getBeanClassName();
			try {
				r.add(Class.forName(beanClassName));
			} catch (ClassNotFoundException e) {
				LOG.error(e);
			}
		}
		return r;
	}

	private static String key(Class superclass, String basePackage) {
		return superclass.getName() + "|" + basePackage;
	}

}
