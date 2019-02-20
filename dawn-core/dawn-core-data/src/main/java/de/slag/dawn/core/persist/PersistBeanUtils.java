package de.slag.dawn.core.persist;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PersistBeanUtils {

	public static String setElseAttribute(String elseAttributes, String key, String value, String sep, String kvSep) {
		final String elseAttributesInternal = assertString(elseAttributes);

		final List<String> asList = Arrays.asList(elseAttributesInternal.split(sep));
		final StringBuilder sb = new StringBuilder();
		String formerValue = null;
		for (final String keyValue : asList) {
			if (StringUtils.isBlank(keyValue)) {
				continue;
			}
			String[] split = keyValue.split(kvSep);
			final String attributeKey = split[0];
			final String attributeValue = split[1];
			if (key.equals(attributeKey)) {
				formerValue = attributeValue;
				sb.append(attributeKey + kvSep + value);
			} else {
				sb.append(attributeKey + kvSep + attributeValue);
			}
			sb.append(sep);
		}
		final boolean isFormerValue = (formerValue != null);
		if (!isFormerValue) {
			sb.append(key + kvSep + value);
		}
		return sb.toString();
	}

	public static String getElseAttribute(String key, String elseAttributes, String sep, String kvSep) {
		final String elseAttributesInternal = assertString(elseAttributes);

		final List<String> asList = Arrays.asList(elseAttributesInternal.split(sep));
		for (final String keyValue : asList) {
			if (StringUtils.isBlank(keyValue)) {
				continue;
			}
			String[] split = keyValue.split(kvSep);
			final String attributeKey = split[0];
			final String attributeValue = split[1];
			if (key.equals(attributeKey)) {
				return attributeValue;
			}
		}
		return null;
	}

	private static String assertString(String string) {
		if (string == null) {
			return StringUtils.EMPTY;
		}
		return string;
	}

}
