package de.slag.dawn.core.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FieldMappingUtils {

	private static final Log LOG = LogFactory.getLog(FieldMappingUtils.class);

	private static final Map<Class<?>, Class<?>> COMPATIBLES = new HashMap<>();

	static {
		COMPATIBLES.put(byte.class, Byte.class);
		COMPATIBLES.put(short.class, Short.class);
		COMPATIBLES.put(int.class, Integer.class);
		COMPATIBLES.put(long.class, Long.class);

		COMPATIBLES.put(char.class, Character.class);

		COMPATIBLES.put(float.class, Float.class);
		COMPATIBLES.put(double.class, Double.class);

		COMPATIBLES.put(boolean.class, Boolean.class);
	}

	public static void map(Object sourceObject, Object targetObject, Collection<String> skipFields, boolean tolerant) {
		final Collection<Field> fields = ReflectionUtils.collectFields(sourceObject.getClass());
		final List<Field> relevantFields = fields.stream().filter(f -> !skipFields.contains(f.getName()))
				.collect(Collectors.toList());

		final Collection<Field> targetFields = ReflectionUtils.collectFields(targetObject.getClass());
		for (final Field sourceField : relevantFields) {
			final String sourceFieldName = sourceField.getName();
			final Optional<Field> target = targetFields.stream().filter(f -> sourceFieldName.equals(f.getName()))
					.findFirst();
			if (!target.isPresent()) {
				if (!tolerant) {
					throw new BaseException("target field not found: " + sourceFieldName);
				}
				continue;
			}
			final Field targetField = target.get();
			final Class<?> sourceFieldType = sourceField.getType();

			final Object fieldValue = getValue(sourceObject, sourceField);

			final Class<?> targetFieldType = targetField.getType();
			if (!sourceFieldType.equals(targetFieldType)) {
				final MessageCacheable msgCache = MessageCacheable.creating();
				msgCache.append("Field: '" + sourceFieldName + "'");
				if (!isCompatible(sourceFieldType, targetFieldType, fieldValue, msgCache)) {

					if (!tolerant) {
						throw new BaseException("types not compatible: " + sourceFieldType + ", " + targetFieldType
								+ ", with value: " + fieldValue);
					}
					continue;
				}
			}

			setValue(targetObject, targetField, fieldValue);
		}

	}

	private static void setValue(final Object targetObject, final Field targetField, final Object fieldValue) {
		targetField.setAccessible(true);
		try {
			targetField.set(targetObject, fieldValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		} finally {
			targetField.setAccessible(false);
		}
	}

	private static Object getValue(final Object sourceObject, final Field sourceField) {
		sourceField.setAccessible(true);
		try {
			return sourceField.get(sourceObject);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new BaseException(e);
		} finally {
			sourceField.setAccessible(false);
		}
	}

	private static boolean isCompatible(final Class<?> sourceType, final Class<?> targetType, final Object fieldValue,
			MessageCacheable msgCache) {

		for (Class<?> primitiveType : COMPATIBLES.keySet()) {
			final Class<?> complexType = COMPATIBLES.get(primitiveType);

			// primitive is never NULL, so it can be written in all cases.
			if (primitiveType.equals(sourceType) && complexType.equals(targetType)) {
				msgCache.append("'" + sourceType + "' > '" + targetType + "': OK");
				LOG.info(msgCache);
				return true;
			}

			// complex can be NULl, primitive must not! It only can be written when value is
			// not NULL.
			if (complexType.equals(sourceType) && primitiveType.equals(targetType)) {
				if (fieldValue != null) {
					final String message = "'" + sourceType + "' > '" + targetType + "', value: '" + fieldValue
							+ "': OK";
					msgCache.append(message);
					LOG.info(msgCache);
					return true;
				}
			}

		}
		msgCache.append("'" + sourceType + "' > '" + targetType + "', value: '" + fieldValue + "': not OK!");
		LOG.warn(msgCache);
		return false;
	}
}
