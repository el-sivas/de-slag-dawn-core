package de.slag.dawn.base.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;



public class PropertySetter {

	private final Consumer<Object> setter;

	private final String name;

	private final Class<?> type;

	public PropertySetter(String name, Method setterMethod, Object object, Class<?> type) {
		this.type = type;
		this.name = name;
		this.setter = new Consumer<Object>() {

			@Override
			public void accept(Object value) {
				try {
					setterMethod.invoke(object, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new BaseException(e);
				}
			}
		};
	}

	public PropertySetter(String name, Field field, Object object, Class<?> type) {
		this.type = type;
		this.name = name;
		this.setter = new Consumer<Object>() {

			@Override
			public void accept(Object value) {
				field.setAccessible(true);
				try {
					field.set(object, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new BaseException(e);
				}
				field.setAccessible(false);
			}
		};
	}

	public void set(Object value) {
		setter.accept(value);
	}

	public Class<?> getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
