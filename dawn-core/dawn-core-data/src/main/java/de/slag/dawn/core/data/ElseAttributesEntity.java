package de.slag.dawn.core.data;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import de.slag.root.base.KeyValueUtils;

public interface ElseAttributesEntity {

	MathContext DEFAULT_MC = new MathContext(7, RoundingMode.HALF_UP);

	String DEFAULT_SDF = "yyyy.MM.dd HH:mm:ss";

	String BLANK = " ";

	// String
	default String getElseAttribute(String key) {
		Map<String, String> split = KeyValueUtils.split(getElseAttributes());
		return split.getOrDefault(key, null);
	}

	default void setElseAttribute(String key, String value) {
		Map<String, String> split = KeyValueUtils.split(getElseAttributes());
		split.put(key, value);
		setElseAttributes(KeyValueUtils.join(split));
	}

	// Long
	default Long getElseLong(String key) {
		return Long.valueOf(getElseAttribute(key));
	}

	default void setElseLong(String key, Long value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseAttribute(key, value.toString());
	}

	// Double
	default Double getElseDouble(String key) {
		return Double.valueOf(getElseAttribute(key));
	}

	default void setElseLong(String key, Double value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseAttribute(key, value.toString());
	}

	// Date
	default Date getElseDate(String key) {
		final String elseProperty = getElseAttribute(key);
		if (elseProperty == null) {
			return null;
		}
		try {
			return new SimpleDateFormat(DEFAULT_SDF).parse(elseProperty);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	default void setElseDate(String key, Date value) {
		if (value == null) {
			setNull(key);
			return;
		}
		setElseAttribute(key, new SimpleDateFormat(DEFAULT_SDF).format(value));
	}

	default void setNull(String key) {
		setElseAttribute(key, null);
	}

	void setElseAttributes(String join);

	String getElseAttributes();

}
