package de.slag.core.logic.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

import de.slag.root.base.BaseException;



public class PropertyGetter {

	private final Supplier<Object> getter;

	private final String name;

	private final Class<?> type;

	public PropertyGetter(String name, Method getterMethod, Object object, Class<?> type) {
		this.type = type;
		this.name = name;
		this.getter = new Supplier<Object>() {

			@Override
			public Object get() {
				try {
					return getterMethod.invoke(object);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new BaseException(e);
				}
			}
		};
	}

	public PropertyGetter(String name, Field field, Object object, Class<?> type) {
		this.type = type;
		this.name = name;
		this.getter = new Supplier<Object>() {

			@Override
			public Object get() {
				field.setAccessible(true);
				try {
					return field.get(object);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BaseException(e);
				} finally {
					field.setAccessible(false);
				}
			}
		};
	}

	public Object get() {
		return getter.get();
	}
	
	public Class<?> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
